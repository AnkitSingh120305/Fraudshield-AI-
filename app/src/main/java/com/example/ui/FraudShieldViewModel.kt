package com.example.ui

import android.app.Application
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.FraudRiskEngine
import com.example.engine.SpeechRecognizerHelper
import com.example.model.CallScenario
import com.example.model.DialogueTurn
import com.example.model.EvaluationResult
import com.example.model.IncidentReport
import com.example.model.RiskLevel
import com.example.model.Speaker
import com.example.repository.FraudShieldRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class Screen {
    data object Home : Screen()
    data class SimulatedCall(val scenario: CallScenario) : Screen()
    data object LiveMicDetection : Screen()
    data class ReportDetail(val report: IncidentReport) : Screen()
    data object PitchArchitecture : Screen()
    data object IntelligenceStats : Screen()
    data object History : Screen()
}

class FraudShieldViewModel(application: Application) : AndroidViewModel(application) {

    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val currentScreen: StateFlow<Screen> = MutableStateFlow(Screen.Home)

    // Call Simulation State
    private val _activeScenario = MutableStateFlow<CallScenario?>(null)
    val activeScenario: StateFlow<CallScenario?> = _activeScenario.asStateFlow()

    private val _isCallIncoming = MutableStateFlow(false)
    val isCallIncoming: StateFlow<Boolean> = _isCallIncoming.asStateFlow()

    private val _isCallActive = MutableStateFlow(false)
    val isCallActive: StateFlow<Boolean> = _isCallActive.asStateFlow()

    private val _callDurationSec = MutableStateFlow(0)
    val callDurationSec: StateFlow<Int> = _callDurationSec.asStateFlow()

    private val _dialogueHistory = MutableStateFlow<List<DialogueTurn>>(emptyList())
    val dialogueHistory: StateFlow<List<DialogueTurn>> = _dialogueHistory.asStateFlow()

    private val _currentEvaluation = MutableStateFlow(
        FraudRiskEngine.evaluateConversation("")
    )
    val currentEvaluation: StateFlow<EvaluationResult> = _currentEvaluation.asStateFlow()

    private val _isCallTerminatedByShield = MutableStateFlow(false)
    val isCallTerminatedByShield: StateFlow<Boolean> = _isCallTerminatedByShield.asStateFlow()

    private val _stallingAdviceVisible = MutableStateFlow(false)
    val stallingAdviceVisible: StateFlow<Boolean> = _stallingAdviceVisible.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _isSpeaker = MutableStateFlow(true)
    val isSpeaker: StateFlow<Boolean> = _isSpeaker.asStateFlow()

    private val _autoPlaySimulation = MutableStateFlow(true)
    val autoPlaySimulation: StateFlow<Boolean> = _autoPlaySimulation.asStateFlow()

    val speechHelper = SpeechRecognizerHelper(application.applicationContext)

    val incidentReports: StateFlow<List<IncidentReport>> = FraudShieldRepository.incidentReports

    private var simulationJob: Job? = null
    private var timerJob: Job? = null

    val currentScreenState: StateFlow<Screen> get() {
        val flow = MutableStateFlow(_screenStack.value.lastOrNull() ?: Screen.Home)
        viewModelScope.launch {
            _screenStack.collect { stack ->
                flow.value = stack.lastOrNull() ?: Screen.Home
            }
        }
        return flow
    }

    fun navigateTo(screen: Screen) {
        val current = _screenStack.value.toMutableList()
        current.add(screen)
        _screenStack.value = current
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value.toMutableList()
        if (current.size > 1) {
            current.removeAt(current.size - 1)
            _screenStack.value = current
            return true
        }
        return false
    }

    fun startScenarioCall(scenario: CallScenario) {
        stopSimulation()
        _activeScenario.value = scenario
        _isCallIncoming.value = true
        _isCallActive.value = false
        _isCallTerminatedByShield.value = false
        _stallingAdviceVisible.value = false
        _callDurationSec.value = 0
        _dialogueHistory.value = emptyList()
        _currentEvaluation.value = FraudRiskEngine.evaluateConversation("")
        navigateTo(Screen.SimulatedCall(scenario))
    }

    fun answerCall() {
        _isCallIncoming.value = false
        _isCallActive.value = true
        startCallTimer()
        startScenarioDialogueFlow()
    }

    fun endCall(byShield: Boolean = false) {
        stopSimulation()
        _isCallIncoming.value = false
        _isCallActive.value = false
        _isCallTerminatedByShield.value = byShield

        // If it was a high risk fraud call with transcripts, automatically generate complaint draft
        val scenario = _activeScenario.value
        val history = _dialogueHistory.value
        val eval = _currentEvaluation.value

        if (scenario != null && history.isNotEmpty() && eval.riskLevel == RiskLevel.HIGH) {
            val report = FraudShieldRepository.generateIncidentReport(
                callerName = scenario.callerName,
                callerNumber = scenario.callerNumber,
                scamCategory = eval.scamType,
                riskScore = eval.score,
                riskLevel = eval.riskLevel,
                transcripts = history.map { "[${it.speaker}]: ${it.text}" },
                flaggedPhrases = eval.detectedSignals.map { it.phrase }
            )
            // Navigate to report
            navigateTo(Screen.ReportDetail(report))
        }
    }

    private fun startCallTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _isCallActive.value) {
                delay(1000)
                _callDurationSec.value += 1
            }
        }
    }

    private fun startScenarioDialogueFlow() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            val turns = _activeScenario.value?.dialogue ?: return@launch
            for (turn in turns) {
                if (!isActive || !_isCallActive.value) break
                delay((turn.timestampOffsetSec * 450L).coerceIn(1200L, 3500L))

                val currentTurns = _dialogueHistory.value.toMutableList()
                currentTurns.add(turn)
                _dialogueHistory.value = currentTurns

                // Re-evaluate cumulative text
                val fullText = currentTurns.joinToString(" ") { it.text }
                val eval = FraudRiskEngine.evaluateConversation(fullText)
                _currentEvaluation.value = eval

                // If high risk triggered, provide haptic warning alert!
                if (eval.riskLevel == RiskLevel.HIGH) {
                    triggerHapticAlert()
                }
            }
        }
    }

    fun triggerHapticAlert() {
        try {
            val context = getApplication<Application>().applicationContext
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Application.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Application.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 150, 100, 250), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(300)
            }
        } catch (ignored: Exception) {}
    }

    fun toggleStallingAdvice() {
        _stallingAdviceVisible.value = !_stallingAdviceVisible.value
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
    }

    fun toggleSpeaker() {
        _isSpeaker.value = !_isSpeaker.value
    }

    fun manualInjectDialogue(turn: DialogueTurn) {
        val currentTurns = _dialogueHistory.value.toMutableList()
        currentTurns.add(turn)
        _dialogueHistory.value = currentTurns

        val fullText = currentTurns.joinToString(" ") { it.text }
        val eval = FraudRiskEngine.evaluateConversation(fullText)
        _currentEvaluation.value = eval

        if (eval.riskLevel == RiskLevel.HIGH) {
            triggerHapticAlert()
        }
    }

    private fun stopSimulation() {
        simulationJob?.cancel()
        simulationJob = null
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopSimulation()
        speechHelper.stopListening()
    }
}
