package com.example.repository

import com.example.model.CallScenario
import com.example.model.DialogueTurn
import com.example.model.IncidentReport
import com.example.model.RiskLevel
import com.example.model.Speaker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object FraudShieldRepository {

    private val _incidentReports = MutableStateFlow<List<IncidentReport>>(emptyList())
    val incidentReports: StateFlow<List<IncidentReport>> = _incidentReports.asStateFlow()

    val scenarios: List<CallScenario> = listOf(
        CallScenario(
            id = "digital_arrest_scam",
            title = "CBI / Police 'Digital Arrest' Threat",
            callerName = "Inspector Vikram (CBI Cyber Cell)",
            callerNumber = "+91 98210 44391",
            category = "Impersonation of Law Enforcement & Digital Arrest",
            language = "English & Hindi",
            expectedRisk = RiskLevel.HIGH,
            description = "Fraudster intimidates victim claiming seized narcotics, orders fake 'Digital Arrest', forbids disconnecting, and demands emergency funds transfer.",
            tacticsOverview = listOf(
                "Fake Law Enforcement Identity (CBI)",
                "Digital Arrest Psychological Terror",
                "Isolation Tactic (Do not disconnect)",
                "Demand for 'RBI Escrow Verification' Money Transfer"
            ),
            dialogue = listOf(
                DialogueTurn(Speaker.CALLER, "Hello, this is Inspector Vikram Rathore from Mumbai Cyber Police & CBI Head Office. Am I speaking to the mobile registered holder?", 2),
                DialogueTurn(Speaker.VICTIM, "Yes, who is this? Why are you calling from CBI?", 5),
                DialogueTurn(Speaker.CALLER, "A seized cargo parcel containing 140 grams of MDMA narcotics and fraudulent documents in your name has been flagged at Mumbai Airport.", 8, listOf("narcotics", "customs seized")),
                DialogueTurn(Speaker.VICTIM, "What?! That's a mistake! I haven't dispatched any parcel to anywhere!", 12),
                DialogueTurn(Speaker.CALLER, "An urgent non-bailable arrest warrant has been registered against you. You are placed under immediate Digital Arrest right now.", 16, listOf("arrest warrant", "digital arrest")),
                DialogueTurn(Speaker.CALLER, "Do not disconnect this line or inform anyone! This is a confidential investigation. Local police team is standing by.", 20, listOf("do not disconnect", "confidential")),
                DialogueTurn(Speaker.VICTIM, "Please sir, I am innocent! Tell me what I need to do!", 24),
                DialogueTurn(Speaker.CALLER, "Per Supreme Court guidelines, you have exactly 15 minutes to transfer money to our official RBI Safety Account for financial clearance. Transfer Rs 85,000 now.", 28, listOf("within 10 minutes", "transfer money", "safety account"))
            )
        ),
        CallScenario(
            id = "sbi_kyc_threat",
            title = "SBI / HDFC KYC Block & OTP Phishing",
            callerName = "Ramesh Sharma (SBI Manager)",
            callerNumber = "+91 79032 18820",
            category = "Banking & KYC Suspension Phishing",
            language = "Hindi & Hinglish",
            expectedRisk = RiskLevel.HIGH,
            description = "Impersonator claims bank account will be permanently terminated tonight unless secret OTP is immediately disclosed.",
            tacticsOverview = listOf(
                "Bank Official Impersonation",
                "Account Termination Panic",
                "Immediate 5-Minute Time Limit",
                "Sensitive One-Time Password Coercion"
            ),
            dialogue = listOf(
                DialogueTurn(Speaker.CALLER, "नमस्ते सर, मैं स्टेट बैंक ऑफ इंडिया हेड ऑफिस से सीनियर मैनेजर रमेश शर्मा बात कर रहा हूँ।", 2, listOf("स्टेट बैंक")),
                DialogueTurn(Speaker.VICTIM, "हाँ बोलिए, क्या बात है?", 5),
                DialogueTurn(Speaker.CALLER, "सर आपका बैंक खाता आज रात स्थायी रूप से खाता बंद कर दिया जाएगा, क्योंकि आपका पैन और केवाईसी अपडेट अधूरा है।", 8, listOf("खाता बंद", "केवाईसी")),
                DialogueTurn(Speaker.VICTIM, "अरे, लेकिन मेरा खाता तो चालू है! इसको बंद मत कीजिए।", 12),
                DialogueTurn(Speaker.CALLER, "अगर आप अभी चालू रखना चाहते हैं, तो बैंक सर्वर से तुरंत लिंक किया जा रहा है। आपके मोबाइल पर अभी 6 डिजिट का वेरिफिकेशन ओटीपी भेजा गया है।", 16, listOf("ओटीपी", "तुरंत")),
                DialogueTurn(Speaker.CALLER, "तुरंत वो ओटीपी बताओ, वरना 5 मिनट में सर्वर खाता ब्लॉक करेगा और 5000 पेनल्टी लगेगी।", 20, listOf("ओटीपी", "खाता ब्लॉक", "तुरंत")),
                DialogueTurn(Speaker.VICTIM, "ओटीपी तो बैंक वाले मना करते हैं किसी को बताने के लिए...", 24),
                DialogueTurn(Speaker.CALLER, "सर हम बैंक हेड ऑफिस से हैं! जल्दी ओटीपी बताओ वरना खाता अभी फ्रीज़ हो जाएगा!", 28, listOf("ओटीपी", "खाता ब्लॉक"))
            )
        ),
        CallScenario(
            id = "electricity_bill_scam",
            title = "Urgent Power Disconnection at 8:30 PM",
            callerName = "DISCOM Power Board Grievance",
            callerNumber = "+91 91234 56789",
            category = "Utility Bill Disconnection & Remote Access Trojan",
            language = "Hinglish & English",
            expectedRisk = RiskLevel.HIGH,
            description = "Fake power company officer claims electricity will be disconnected within an hour unless AnyDesk is installed to enter UPI PIN.",
            tacticsOverview = listOf(
                "Utility Disconnection Urgency",
                "Malicious Remote Tool (AnyDesk) Push",
                "Screen Sharing Coercion",
                "UPI PIN Extraction"
            ),
            dialogue = listOf(
                DialogueTurn(Speaker.CALLER, "Urgent Notice from Electricity Board: Your power supply will be disconnected tonight at 8:30 PM due to unpaid balance.", 2, listOf("power disconnect", "electricity cut")),
                DialogueTurn(Speaker.VICTIM, "Wait, but I already paid my electricity bill online last week!", 6),
                DialogueTurn(Speaker.CALLER, "Sir your update failed on the government server. Disconnect order is already active on your meter.", 10),
                DialogueTurn(Speaker.CALLER, "To avoid blackout, open Play Store immediately and install AnyDesk remote app so our executive can clear your bill.", 15, listOf("anydesk", "install apk")),
                DialogueTurn(Speaker.VICTIM, "Why do I need AnyDesk to clear my electricity bill?", 20),
                DialogueTurn(Speaker.CALLER, "It is official remote meter portal sir. Start screen share, make 10 rupees token payment and enter your secret UPI PIN right now.", 25, listOf("upi pin", "immediate action"))
            )
        ),
        CallScenario(
            id = "telegram_task_scam",
            title = "Telegram 'Part-Time Job' Advance Fee",
            callerName = "Pooja HR (Global Rating Hub)",
            callerNumber = "+91 94321 00987",
            category = "Part-Time Task & Advance Fee Fraud",
            language = "English",
            expectedRisk = RiskLevel.HIGH,
            description = "Victim promised high daily earnings for rating hotels, then pressured into transferring cash to unlock task earnings.",
            tacticsOverview = listOf(
                "Unrealistic High Return Bait",
                "Advance Deposit Demand",
                "Merchant Wallet Transfer Coercion"
            ),
            dialogue = listOf(
                DialogueTurn(Speaker.CALLER, "Hello! You have been selected for part-time Google Maps hotel review tasks earning Rs 4,500 daily.", 2),
                DialogueTurn(Speaker.VICTIM, "How does it work? Do I need to pay anything?", 6),
                DialogueTurn(Speaker.CALLER, "First tasks are free. To unlock premium level payout with 200% profit, you must deposit money into our merchant account now.", 10, listOf("deposit money", "send money")),
                DialogueTurn(Speaker.VICTIM, "Can't you just deduct it from my earnings?", 14),
                DialogueTurn(Speaker.CALLER, "No sir! Rules require direct transfer money immediately to our verification wallet within 10 minutes or bonus expires.", 18, listOf("transfer money", "within 10 minutes"))
            )
        ),
        CallScenario(
            id = "legitimate_delivery",
            title = "Courier Parcel Handover (Legitimate)",
            callerName = "Raju (BlueDart Courier)",
            callerNumber = "+91 98450 11223",
            category = "Legitimate Service Verification",
            language = "Hindi & English",
            expectedRisk = RiskLevel.LOW,
            description = "Normal delivery boy at the gate asking for delivery confirmation OTP. Evaluated as LOW risk to prove zero false-positive disruption.",
            tacticsOverview = listOf(
                "No Coercion or Threats",
                "No Financial Transfer Demand",
                "Legitimate Parcel Receipt Verification"
            ),
            dialogue = listOf(
                DialogueTurn(Speaker.CALLER, "नमस्ते सर, मैं ब्लू डार्ट डिलीवरी बॉय बोल रहा हूँ। आपका अमेज़न पार्सल लेकर गेट पर खड़ा हूँ।", 2, listOf("पार्सल डिलीवरी")),
                DialogueTurn(Speaker.VICTIM, "हाँ भैया, मैं फर्स्ट फ्लोर पर हूँ, ऊपर आ जाइए।", 6),
                DialogueTurn(Speaker.CALLER, "जी सर, पार्सल हैंडओवर के लिए एक डिलीवरी ओटीपी आया होगा, वो गेट पर दिखा दीजिएगा।", 10, listOf("ओटीपी")),
                DialogueTurn(Speaker.VICTIM, "हाँ ठीक है, मैं नीचे आकर पार्सल ले लेता हूँ।", 14),
                DialogueTurn(Speaker.CALLER, "बहुत धन्यवाद सर, आराम से आइए।", 18)
            )
        ),
        CallScenario(
            id = "legitimate_bank_offer",
            title = "Bank Card Upgrade Offer (Legitimate)",
            callerName = "Priya (HDFC Credit Services)",
            callerNumber = "+91 99000 88776",
            category = "Routine Customer Outreach",
            language = "English",
            expectedRisk = RiskLevel.LOW,
            description = "Bank representative offering pre-approved credit card, explicitly reminding the customer never to share passwords or OTPs.",
            tacticsOverview = listOf(
                "Polite Routine Communication",
                "Proactive Anti-Fraud Warning Given by Caller",
                "No Urgency or Intimidation"
            ),
            dialogue = listOf(
                DialogueTurn(Speaker.CALLER, "Good morning sir, Priya calling from HDFC Bank regarding your pre-approved card upgrade.", 2),
                DialogueTurn(Speaker.VICTIM, "Are there any hidden joining charges?", 6),
                DialogueTurn(Speaker.CALLER, "No hidden fees. I can email you the details. Please remember HDFC never asks for your OTP, PIN, or netbanking passwords over call.", 10),
                DialogueTurn(Speaker.VICTIM, "Okay, please send it to my email ID, I'll review it.", 14),
                DialogueTurn(Speaker.CALLER, "Certainly sir, thank you for banking with HDFC. Have a great day.", 18)
            )
        )
    )

    init {
        // Pre-populate with a sample incident report for immediate inspection
        val initialReport = generateIncidentReport(
            callerName = "Inspector Vikram (CBI Cyber Cell)",
            callerNumber = "+91 98210 44391",
            scamCategory = "Digital Arrest & Law Enforcement Impersonation",
            riskScore = 94,
            riskLevel = RiskLevel.HIGH,
            transcripts = listOf(
                "[CALLER]: An urgent non-bailable arrest warrant has been registered against you. You are placed under immediate Digital Arrest right now.",
                "[CALLER]: Do not disconnect this line or inform anyone! This is a confidential investigation.",
                "[CALLER]: Supreme Court guidelines mandate you must transfer all funds to our RBI Safety Account for verification. Transfer Rs 85,000 now."
            ),
            flaggedPhrases = listOf("digital arrest", "arrest warrant", "transfer money", "safety account", "confidential")
        )
        _incidentReports.value = listOf(initialReport)
    }

    fun generateIncidentReport(
        callerName: String,
        callerNumber: String,
        scamCategory: String,
        riskScore: Int,
        riskLevel: RiskLevel,
        transcripts: List<String>,
        flaggedPhrases: List<String>
    ): IncidentReport {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateString = dateFormat.format(Date())

        val complaintDraft = buildString {
            appendLine("COMPLAINT DRAFT FOR NATIONAL CYBER CRIME REPORTING PORTAL (cybercrime.gov.in)")
            appendLine("==========================================================================")
            appendLine("Incident Type: $scamCategory")
            appendLine("Suspect Caller Name / Claimed Identity: $callerName")
            appendLine("Suspect Calling Number: $callerNumber")
            appendLine("Date & Time of Incident: $dateString")
            appendLine("Fraud Risk Confidence Score: $riskScore% ($riskLevel)")
            appendLine()
            appendLine("FACTS & EVIDENCE OF THE COMPLAINT:")
            appendLine("On $dateString, I received an unsolicited phone call from $callerNumber.")
            appendLine("The caller falsely represented themselves as '$callerName' and engaged in aggressive psychological coercion and social engineering.")
            appendLine()
            appendLine("Flagged Manipulation & Threat Indicators:")
            flaggedPhrases.forEach { appendLine(" - Detected threat/action phrase: \"$it\"") }
            appendLine()
            appendLine("Verbatim Dialogue Excerpts Captured by FraudShield AI:")
            transcripts.forEach { appendLine(" > $it") }
            appendLine()
            appendLine("REQUESTED ACTION:")
            appendLine("1. Trace and block the fraudulent mobile number: $callerNumber.")
            appendLine("2. Investigate the suspected digital arrest / financial extortion syndicate.")
            appendLine("3. Register under relevant sections of the Information Technology Act (Sec 66D - Cheating by Personation) and Bharatiya Nyaya Sanhita (BNS).")
            appendLine()
            appendLine("Victim Protection Assisted by: FraudShield AI Prevention Engine")
            appendLine("National Cyber Helpline: 1930")
        }

        val report = IncidentReport(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            callerName = callerName,
            callerNumber = callerNumber,
            scamCategory = scamCategory,
            riskScore = riskScore,
            riskLevel = riskLevel,
            transcripts = transcripts,
            flaggedPhrases = flaggedPhrases,
            complaintDraftText = complaintDraft
        )

        val updatedList = _incidentReports.value.toMutableList()
        updatedList.add(0, report)
        _incidentReports.value = updatedList
        return report
    }
}
