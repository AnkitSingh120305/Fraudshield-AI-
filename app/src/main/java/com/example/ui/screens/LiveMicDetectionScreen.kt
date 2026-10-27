package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.engine.FraudRiskEngine
import com.example.model.RiskLevel
import com.example.ui.FraudShieldViewModel
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.RealTimeThreatMeter
import com.example.ui.components.ThreatBadge
import com.example.ui.theme.CyberNavyBorder
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.ThreatCaution
import com.example.ui.theme.ThreatDanger
import com.example.ui.theme.ThreatSafe

@Composable
fun LiveMicDetectionScreen(
    viewModel: FraudShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechHelper = viewModel.speechHelper
    val isListening by speechHelper.isListening.collectAsState()
    val liveTranscript by speechHelper.liveTranscript.collectAsState()
    val errorMessage by speechHelper.errorMessage.collectAsState()

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (isGranted) {
            speechHelper.startListening()
        }
    }

    // Live real-time classification of what was spoken
    val evaluation = remember(liveTranscript) {
        FraudRiskEngine.evaluateConversation(liveTranscript)
    }

    BackHandler {
        speechHelper.stopListening()
        viewModel.navigateBack()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark)
            .testTag("live_mic_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        speechHelper.stopListening()
                        viewModel.navigateBack()
                    },
                    modifier = Modifier.testTag("live_mic_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Live Microphone Analyzer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Real-time speech-to-text with multi-vector fraud scoring",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Live Mic Control Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AudioWaveformVisualizer(
                        isActive = isListening,
                        color = if (isListening) CyberTeal else Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Big Mic Button
                    IconButton(
                        onClick = {
                            if (isListening) {
                                speechHelper.stopListening()
                            } else {
                                if (hasAudioPermission) {
                                    speechHelper.startListening()
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(if (isListening) ThreatDanger else CyberTeal)
                            .testTag("record_mic_button")
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Microphone",
                            tint = if (isListening) Color.White else Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isListening) "Listening... Speak now" else "Tap microphone to speak",
                        fontWeight = FontWeight.Bold,
                        color = if (isListening) ThreatDanger else Color.White,
                        fontSize = 15.sp
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = ThreatCaution,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Real-Time Threat Meter
        item {
            RealTimeThreatMeter(
                score = evaluation.score,
                riskLevel = evaluation.riskLevel
            )
        }

        // Live Transcript Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavyElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE SPEECH TRANSCRIPTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTeal,
                            letterSpacing = 0.8.sp
                        )

                        if (liveTranscript.isNotEmpty()) {
                            IconButton(
                                onClick = { speechHelper.clearTranscript() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF09121F))
                            .border(1.dp, Color(0xFF1E2E42), RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = if (liveTranscript.isBlank()) "Transcript will appear here as you speak or tap test scripts below..." else liveTranscript,
                            color = if (liveTranscript.isBlank()) Color.Gray else Color.White,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // Quick Instant Test Phrases
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "QUICK TEST PHRASES (INSTANT SPEECH INJECTION)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.8.sp
                )

                TestPhraseButton(
                    label = "🚨 Digital Arrest & Money Transfer (High Risk)",
                    phrase = "This is Inspector Vikram from CBI Mumbai. You are placed under digital arrest right now. Transfer money to our safety account within 10 minutes or police will raid your house.",
                    onClick = { speechHelper.appendSampleText("This is Inspector Vikram from CBI Mumbai. You are placed under digital arrest right now. Transfer money to our safety account within 10 minutes or police will raid your house.") }
                )

                TestPhraseButton(
                    label = "🚨 SBI Account Block & KYC (Hindi High Risk)",
                    phrase = "मैं स्टेट बैंक से सीनियर मैनेजर बोल रहा हूँ। आपका खाता बंद कर दिया जाएगा। तुरंत वो ओटीपी बताओ वरना खाता ब्लॉक हो जाएगा।",
                    onClick = { speechHelper.appendSampleText("मैं स्टेट बैंक से सीनियर मैनेजर बोल रहा हूँ। आपका खाता बंद कर दिया जाएगा। तुरंत वो ओटीपी बताओ वरना खाता ब्लॉक हो जाएगा।") }
                )

                TestPhraseButton(
                    label = "🚨 Electricity Disconnection & AnyDesk (High Risk)",
                    phrase = "Your electricity will be disconnected tonight at 8 PM. Download AnyDesk app and enter your UPI PIN immediately.",
                    onClick = { speechHelper.appendSampleText("Your electricity will be disconnected tonight at 8 PM. Download AnyDesk app and enter your UPI PIN immediately.") }
                )

                TestPhraseButton(
                    label = "🛡️ Legitimate Courier Delivery (Low Risk Baseline)",
                    phrase = "Hello sir, delivery agent at your gate with Amazon parcel. Please share the delivery OTP to hand over your box.",
                    onClick = { speechHelper.appendSampleText("Hello sir, delivery agent at your gate with Amazon parcel. Please share the delivery OTP to hand over your box.") }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TestPhraseButton(
    label: String,
    phrase: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberNavyElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyberTeal)
            Spacer(modifier = Modifier.height(4.dp))
            Text("\"$phrase\"", fontSize = 12.sp, color = Color(0xFFB0BEC5), lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A5F)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("Inject Phrase", fontSize = 11.sp, color = Color.White)
            }
        }
    }
}
