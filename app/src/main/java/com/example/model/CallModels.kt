package com.example.model

enum class RiskLevel(val label: String) {
    LOW("Safe / Low Risk"),
    MEDIUM("Caution / Suspicious"),
    HIGH("Critical Fraud Alert")
}

enum class Speaker {
    CALLER,
    VICTIM
}

enum class SignalType(val title: String) {
    SENSITIVE_ACTION("Sensitive Credential Request"),
    MANIPULATION_CUE("Psychological Coercion / Urgency"),
    IMPERSONATION("Authority Impersonation"),
    PRESSURE_VELOCITY("Conversation Pressure Escalation")
}

data class DetectedSignal(
    val type: SignalType,
    val phrase: String,
    val weight: Int,
    val explanation: String
)

data class DialogueTurn(
    val speaker: Speaker,
    val text: String,
    val timestampOffsetSec: Int = 0,
    val highlightWords: List<String> = emptyList()
)

data class CallScenario(
    val id: String,
    val title: String,
    val callerName: String,
    val callerNumber: String,
    val category: String,
    val language: String,
    val expectedRisk: RiskLevel,
    val description: String,
    val dialogue: List<DialogueTurn>,
    val tacticsOverview: List<String>
)

data class EvaluationResult(
    val riskLevel: RiskLevel,
    val score: Int,
    val scamType: String,
    val detectedSignals: List<DetectedSignal>,
    val reasons: List<String>,
    val actionRecommendation: String,
    val isUrgentAlertTriggered: Boolean
)

data class IncidentReport(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val callerName: String,
    val callerNumber: String,
    val scamCategory: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val transcripts: List<String>,
    val flaggedPhrases: List<String>,
    val complaintDraftText: String,
    val reportingPortalUrl: String = "https://cybercrime.gov.in",
    val helplineNumber: String = "1930"
)
