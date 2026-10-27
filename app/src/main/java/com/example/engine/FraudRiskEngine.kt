package com.example.engine

import com.example.model.DetectedSignal
import com.example.model.EvaluationResult
import com.example.model.RiskLevel
import com.example.model.SignalType
import java.util.Locale

object FraudRiskEngine {

    // Sensitive Action Triggers (English, Hindi, Hinglish)
    private val sensitiveActionPatterns = listOf(
        Pair("otp", "Requesting One-Time Password"),
        Pair("one time password", "Requesting OTP credential"),
        Pair("pin", "Requesting Security PIN"),
        Pair("cvv", "Requesting Card Verification Value (CVV)"),
        Pair("upi pin", "Requesting UPI Authorization PIN"),
        Pair("atm pin", "Requesting ATM Debit Card PIN"),
        Pair("password", "Requesting Account Security Password"),
        Pair("netbanking", "Probing for Online Banking Credentials"),
        Pair("transfer money", "Coercing Victim to Transfer Funds"),
        Pair("send money", "Requesting Monetary Payment"),
        Pair("safety account", "Directing Funds to Suspicious 'Verification/Safety' Account"),
        Pair("deposit money", "Requesting Financial Deposit"),
        Pair("anydesk", "Coercing Installation of Remote Desktop App (AnyDesk)"),
        Pair("teamviewer", "Coercing Installation of Remote Desktop App (TeamViewer)"),
        Pair("quicksupport", "Coercing Installation of Remote Screen Sharing"),
        Pair("install apk", "Instructing Download of Malicious Third-Party APK"),
        Pair("click the link", "Phishing Link Access Request"),
        Pair("ओटीपी", "Requesting OTP (Hindi)"),
        Pair("पिन", "Requesting Secret PIN (Hindi)"),
        Pair("पासवर्ड", "Requesting Password (Hindi)"),
        Pair("पैसे भेजो", "Directing Money Transfer (Hindi)"),
        Pair("ट्रांसफर करो", "Directing Fund Transfer (Hindi)"),
        Pair("खाते में डालो", "Directing Deposit to Account (Hindi)"),
        Pair("एनीडेस्क", "Instructing Remote Control Tool Installation (Hindi)"),
        Pair("ऐप डाउनलोड", "Instructing Unknown App Installation (Hindi)"),
        Pair("otp batao", "Requesting OTP in Hinglish"),
        Pair("pin batao", "Requesting PIN in Hinglish"),
        Pair("paise bhejo", "Demanding Money Transfer in Hinglish")
    )

    // Manipulation & Psychological Coercion Triggers
    private val manipulationPatterns = listOf(
        Pair("digital arrest", "Threatening Unlawful 'Digital Arrest' Coercion"),
        Pair("arrest warrant", "Threat of Imminent Police Arrest"),
        Pair("narcotics", "Fabricating Drug Seizure Allegation"),
        Pair("customs seized", "Fabricating Customs Parcel Seizure"),
        Pair("account blocked", "Fear Induction: Claim of Account Suspension"),
        Pair("account freeze", "Fear Induction: Asset Freezing Threat"),
        Pair("kyc expired", "Urgency Pressure: Claim of KYC Expiry"),
        Pair("power disconnect", "Urgency Pressure: Electricity Disconnection Threat"),
        Pair("disconnected", "Urgency: Claim of Service Disconnection"),
        Pair("disconnection", "Urgency: Utility Cutoff Threat"),
        Pair("power cut", "Urgency: Imminent Power Cut"),
        Pair("electricity", "Urgency: Utility Service Disruption"),
        Pair("electricity cut", "Urgency Pressure: Immediate Utility Cutoff"),
        Pair("within 10 minutes", "High Urgency Pressure Window"),
        Pair("immediately", "Extreme Urgency Pressure"),
        Pair("immediate action", "Pressure to Act Without Verification"),
        Pair("urgent", "Urgency Pressure"),
        Pair("do not disconnect", "Isolation Tactic: Forbidding Victim to Hang Up"),
        Pair("stay on the line", "Isolation Tactic: Continuous Voice Control"),
        Pair("do not tell anyone", "Isolation Tactic: Enforcing Secrecy"),
        Pair("confidential", "Isolation Tactic: Claiming Official Secrecy"),
        Pair("legal action", "Legal Intimidation"),
        Pair("fir registered", "Intimidation with Criminal Case/FIR"),
        Pair("डिजिटल अरेस्ट", "Digital Arrest Threat (Hindi)"),
        Pair("गिरफ्तारी", "Arrest Threat (Hindi)"),
        Pair("वारंट", "Warrant Intimidation (Hindi)"),
        Pair("खाता बंद", "Threat of Bank Account Termination (Hindi)"),
        Pair("केवाईसी", "KYC Suspension Pressure (Hindi)"),
        Pair("बिजली कट", "Power Disconnection Urgency (Hindi)"),
        Pair("तुरंत", "Extreme Urgency Pressure (Hindi)"),
        Pair("फोन मत काटना", "Enforced Line Holding / Isolation (Hindi)"),
        Pair("किसी को मत बताना", "Secrecy Coercion (Hindi)")
    )

    // Impersonation of Authorities & Institutions
    private val impersonationPatterns = listOf(
        Pair("mumbai police", "Impersonating Mumbai Police Officer"),
        Pair("delhi police", "Impersonating Police Department"),
        Pair("cbi", "Impersonating Central Bureau of Investigation"),
        Pair("customs officer", "Impersonating Indian Customs Authority"),
        Pair("reserve bank", "Impersonating Reserve Bank of India (RBI)"),
        Pair("rbi verification", "Fake RBI Authority Claim"),
        Pair("state bank", "Impersonating SBI Branch Official"),
        Pair("electricity department", "Impersonating State Electricity Board (DISCOM)"),
        Pair("trai", "Impersonating Telecom Regulatory Authority of India"),
        Pair("supreme court", "Fraudulent Judicial Reference"),
        Pair("साइबर सेल", "Impersonating Cyber Crime Department (Hindi)"),
        Pair("सीबीआई", "Impersonating CBI Officer (Hindi)"),
        Pair("बिजली विभाग", "Impersonating Electricity Dept (Hindi)"),
        Pair("पुलिस", "Impersonating Police Authority (Hindi)")
    )

    // Legitimate Context Dampeners (e.g. legitimate delivery or normal inquiries)
    private val legitimateDampenerPatterns = listOf(
        "delivery agent", "parcel delivery", "at your gate", "swiggy", "zomato",
        "amazon delivery", "flipkart", "credit card upgrade offer", "polite routine call",
        "पार्सल डिलीवरी", "गेट पर हूँ", "डिलिवरी बॉय"
    )

    fun evaluateConversation(
        accumulatedText: String,
        isDirectCallerOnly: Boolean = false
    ): EvaluationResult {
        val lowerText = accumulatedText.lowercase(Locale.ROOT)
        val detected = mutableListOf<DetectedSignal>()

        val isAdvisorySafetyStatement = lowerText.contains("never ask") || lowerText.contains("never share")

        // 1. Check Sensitive Actions
        var sensitiveCount = 0
        if (!isAdvisorySafetyStatement) {
            for ((pattern, desc) in sensitiveActionPatterns) {
                if (lowerText.contains(pattern)) {
                    sensitiveCount++
                    detected.add(DetectedSignal(SignalType.SENSITIVE_ACTION, pattern, 35, desc))
                }
            }
        }

        // 2. Check Manipulation Cues
        var manipulationCount = 0
        for ((pattern, desc) in manipulationPatterns) {
            if (lowerText.contains(pattern)) {
                manipulationCount++
                detected.add(DetectedSignal(SignalType.MANIPULATION_CUE, pattern, 25, desc))
            }
        }

        // 3. Check Authority Impersonation
        var impersonationCount = 0
        for ((pattern, desc) in impersonationPatterns) {
            if (lowerText.contains(pattern)) {
                impersonationCount++
                detected.add(DetectedSignal(SignalType.IMPERSONATION, pattern, 20, desc))
            }
        }

        // 4. Check Legitimate Dampeners
        var hasDampener = false
        for (dampener in legitimateDampenerPatterns) {
            if (lowerText.contains(dampener)) {
                hasDampener = true
                break
            }
        }

        // Calibrated Multi-Signal Scoring Engine
        // Rule 1: A legitimate delivery person mentioning OTP without threat or money transfer = Low Risk
        var rawScore = 0

        // Base weights from signal occurrences
        rawScore += (sensitiveCount * 28).coerceAtMost(55)
        rawScore += (manipulationCount * 24).coerceAtMost(48)
        rawScore += (impersonationCount * 22).coerceAtMost(44)

        // Multi-signal synergy multiplier (The hallmark of social engineering)
        if (sensitiveCount > 0 && manipulationCount > 0) {
            rawScore += 25 // Deadly combination: Threat/Urgency + Credential/Money Demand
        }
        if (impersonationCount > 0 && (manipulationCount > 0 || sensitiveCount > 0)) {
            rawScore += 20 // Authority claim + Demand/Fear
        }

        // Digital arrest explicit escalation
        if (lowerText.contains("digital arrest") || lowerText.contains("डिजिटल अरेस्ट")) {
            rawScore = rawScore.coerceAtLeast(85)
        }

        // Context dampener calibration
        if (isAdvisorySafetyStatement && !lowerText.contains("transfer") && !lowerText.contains("digital arrest")) {
            rawScore = 0
        } else if (hasDampener && manipulationCount == 0 && impersonationCount == 0 && !lowerText.contains("transfer") && !lowerText.contains("remote")) {
            // Legitimate delivery mentioning OTP for parcel receipt
            rawScore = (rawScore * 0.35f).toInt().coerceAtMost(25)
        }

        val finalScore = rawScore.coerceIn(0, 99)

        val riskLevel = when {
            finalScore >= 68 -> RiskLevel.HIGH
            finalScore >= 32 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        val primaryScamType = when {
            lowerText.contains("digital arrest") || lowerText.contains("cbi") || lowerText.contains("narcotics") ->
                "Digital Arrest & Law Enforcement Impersonation"
            lowerText.contains("kyc") || lowerText.contains("account blocked") || lowerText.contains("sbi") ->
                "Banking / KYC Account Suspension Phishing"
            lowerText.contains("power") || lowerText.contains("electricity") || lowerText.contains("बिजली") ->
                "Utility Bill Disconnection Scam"
            lowerText.contains("anydesk") || lowerText.contains("teamviewer") || lowerText.contains("apk") ->
                "Remote Access Trojan / Malicious App Fraud"
            sensitiveCount > 0 -> "Financial Credential Harvesting"
            hasDampener -> "Legitimate Service Verification"
            else -> "Routine Incoming Communication"
        }

        val reasons = mutableListOf<String>()
        if (impersonationCount > 0) reasons.add("Caller is impersonating official law enforcement or financial authority.")
        if (manipulationCount > 0) reasons.add("High psychological pressure, fear, or artificial urgency detected.")
        if (sensitiveCount > 0) reasons.add("Direct request for confidential PIN/OTP/Banking credentials or funds transfer.")
        if (reasons.isEmpty()) {
            if (hasDampener) {
                reasons.add("Normal delivery or service dialogue detected. No malicious coercion patterns observed.")
            } else {
                reasons.add("No malicious social engineering markers currently identified.")
            }
        }

        val recommendation = when (riskLevel) {
            RiskLevel.HIGH -> "CRITICAL: Do NOT share any OTP, PIN or transfer money. Terminate call immediately and report to 1930."
            RiskLevel.MEDIUM -> "CAUTION: Unverified urgency detected. Independently verify the organization before proceeding."
            RiskLevel.LOW -> "Normal conversation. Maintain vigilance and never share passwords or banking PINs."
        }

        return EvaluationResult(
            riskLevel = riskLevel,
            score = finalScore,
            scamType = primaryScamType,
            detectedSignals = detected.distinctBy { it.phrase },
            reasons = reasons,
            actionRecommendation = recommendation,
            isUrgentAlertTriggered = riskLevel == RiskLevel.HIGH
        )
    }
}
