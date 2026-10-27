package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FraudShieldViewModel
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberNavyBorder
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.ThreatCaution
import com.example.ui.theme.ThreatDanger
import com.example.ui.theme.ThreatSafe

data class SlideContent(
    val slideNumber: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val coreMessage: String,
    val bulletPoints: List<String>,
    val highlights: List<Pair<String, String>>
)

@Composable
fun PitchArchitectureScreen(
    viewModel: FraudShieldViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSlideIndex by remember { mutableIntStateOf(0) }

    val slides = listOf(
        SlideContent(
            slideNumber = 1,
            title = "PROBLEM DEFINITION",
            subtitle = "The Scam Happens Before the Victim Realizes It",
            icon = Icons.Default.Warning,
            coreMessage = "Current security identifies suspicious numbers or helps report fraud after the fact, but social engineering happens live during seemingly legitimate conversations.",
            bulletPoints = listOf(
                "Fraudsters impersonate banks, police, CBI, government officials, delivery agents, or support staff.",
                "They exert extreme urgency, threats, fear of arrest, and demand immediate OTPs, PINs, or UPI transfers.",
                "Victims panic and disclose sensitive credentials before ever recognizing the manipulation."
            ),
            highlights = listOf(
                "₹1,750+ Cr" to "Lost to Digital Arrest scams alone in India (2024 I4C Data)",
                "70,000+" to "Daily cyber fraud distress calls to 1930 Helpline"
            )
        ),
        SlideContent(
            slideNumber = 2,
            title = "PROBLEM GAP",
            subtitle = "Why Awareness Alone Is Not Enough",
            icon = Icons.Default.Lightbulb,
            coreMessage = "A citizen can know about cyber fraud in theory and still be manipulated under high psychological pressure.",
            bulletPoints = listOf(
                "Caller-ID apps cannot determine what is being said during the conversation.",
                "Number-based blocking fails on spoofed lines or newly generated VoIP numbers.",
                "When a caller threatens an immediate warrant or power cutoff, adrenaline overrides rational judgment.",
                "Target Beneficiaries: Elderly citizens, rural digital payment adopters, students, and working professionals."
            ),
            highlights = listOf(
                "Gap Identified" to "Conversation-level risk detection and timely intervention before disclosure",
                "Fatal Vulnerability" to "Post-event reporting is too late once money leaves the account"
            )
        ),
        SlideContent(
            slideNumber = 3,
            title = "SOLUTION OVERVIEW",
            subtitle = "Introducing FraudShield AI",
            icon = Icons.Default.Shield,
            coreMessage = "A proactive, conversation-aware fraud prevention assistant designed to intervene before a victim shares sensitive information.",
            bulletPoints = listOf(
                "Analyzes simulated and live speech audio in real time.",
                "Transcribes conversation across Hindi, English, and Hinglish.",
                "Detects social engineering patterns: coercive threats, impersonation, and credential harvesting.",
                "Displays immediate high-visibility warnings with contextual reasoning.",
                "Generates a pre-filled, user-approved complaint draft for cybercrime.gov.in & 1930."
            ),
            highlights = listOf(
                "USP" to "Not 'Who is calling?' but 'What is the caller trying to make you do?'",
                "Protection Mode" to "Active intervention before the victim can execute a transfer"
            )
        ),
        SlideContent(
            slideNumber = 4,
            title = "TECHNICAL ARCHITECTURE",
            subtitle = "From Conversation to Prevention Pipeline",
            icon = Icons.Default.Memory,
            coreMessage = "End-to-end multimodal pipeline combining speech recognition, multilingual heuristic classification, and risk-calibrated intervention.",
            bulletPoints = listOf(
                "1. Audio Pipeline: Live speech streaming / simulated call playback.",
                "2. Speech-to-Text: Continuous multilingual transcription (Hindi + English + Hinglish).",
                "3. Three-Vector Risk Engine: Sensitive Action Requests (35%), Manipulation/Threat Cues (25%), Authority Impersonation (20%), plus Synergy Multiplier.",
                "4. Calibrated Policy: Low (0-31%) = Silent monitoring; Medium (32-67%) = Caution alert; High (68-100%) = Urgent siren overlay + Disconnect advice."
            ),
            highlights = listOf(
                "Rule Calibration" to "Delivery courier saying 'OTP' without threats = Zero false alarm",
                "Digital Arrest Rule" to "Direct escalation to Critical Threat due to legal impossibility"
            )
        ),
        SlideContent(
            slideNumber = 5,
            title = "IMPACT & SCALABILITY",
            subtitle = "Prevent Harm Before It Happens",
            icon = Icons.Default.TrendingUp,
            coreMessage = "Measure prevention performance and victim intervention success, not just the volume of scams logged.",
            bulletPoints = listOf(
                "Precision: Minimizing false positive alerts on legitimate courier or bank calls.",
                "Recall: High detection across varied coercion scripts (Digital Arrest, KYC block, electricity cutoff).",
                "Low Latency: Real-time keyword & pattern tagging in under 500ms of speech chunk arrival.",
                "Scalability: Ready for additional regional languages (Odia, Bengali, Tamil, Telugu) and carrier API integrations."
            ),
            highlights = listOf(
                "Primary Metric" to "Victim intervention success rate before financial action",
                "Expansion Path" to "Integration with banking apps & national cybercell feeds"
            )
        ),
        SlideContent(
            slideNumber = 6,
            title = "TEAM & IMPLEMENTATION",
            subtitle = "A Working Prototype in 72 Hours",
            icon = Icons.Default.Group,
            coreMessage = "Execution strategy delivering a functioning live Android prototype within a 72-hour hackathon timeframe.",
            bulletPoints = listOf(
                "Role 1 (AI & NLP): Speech recognition, multilingual keyword lexicon, threat weighting engine.",
                "Role 2 (Architecture & Systems): Real-time pipeline, incident report generator, 1930 & portal integration.",
                "Role 3 (Frontend & Pitch): Modern Jetpack Compose UI, simulated call engine, pitch presentation mode.",
                "72-Hour Plan: Day 1 (Engine & NLP), Day 2 (UI & Alerts), Day 3 (Benchmarking against legitimate vs fraudulent calls)."
            ),
            highlights = listOf(
                "Native Stack" to "Kotlin + Jetpack Compose + Android SpeechRecognizer",
                "Readiness" to "Simulated call benchmarked & verified on Android"
            )
        ),
        SlideContent(
            slideNumber = 7,
            title = "RESPONSIBLE AI & DISCLOSURE",
            subtitle = "Build Trust Into Protection",
            icon = Icons.Default.Lock,
            coreMessage = "Fraud prevention must rigorously protect user privacy and respect ethical legal boundaries.",
            bulletPoints = listOf(
                "Privacy First: Audio evaluated on-device or with user-consented test clips; zero unauthorized audio retention.",
                "Human Oversight: Recommends protective actions; does not unilaterally disconnect without user control.",
                "Accurate Reporting: Generates draft complaints for user review; does NOT falsely impersonate legal authorities.",
                "Honest Boundary: Positioned as conversation assistant and simulated prototype, acknowledging cellular OS call-interception sandbox limits."
            ),
            highlights = listOf(
                "Data Privacy" to "Local, consent-based, privacy-first processing",
                "Core Stance" to "Moving cyber protection from after-the-loss to before-the-disclosure"
            )
        )
    )

    BackHandler {
        viewModel.navigateBack()
    }

    val currentSlide = slides[selectedSlideIndex]

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark)
            .testTag("pitch_architecture_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("pitch_back_button")
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
                        text = "Hackathon Pitch & Architecture",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Official 7-Slide Competition Sequence",
                        fontSize = 12.sp,
                        color = CyberTeal
                    )
                }
            }
        }

        // Slide Selector Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedSlideIndex,
                containerColor = CyberNavySurface,
                contentColor = CyberTeal,
                edgePadding = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                slides.forEachIndexed { index, slide ->
                    Tab(
                        selected = selectedSlideIndex == index,
                        onClick = { selectedSlideIndex = index },
                        text = {
                            Text(
                                text = "Slide ${slide.slideNumber}",
                                fontWeight = if (selectedSlideIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }
        }

        // Active Slide Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("slide_content_card_${currentSlide.slideNumber}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(CyberTeal.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = currentSlide.icon,
                                    contentDescription = null,
                                    tint = CyberTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "SLIDE ${currentSlide.slideNumber} · ${currentSlide.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTeal,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentSlide.subtitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Core Message Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberNavyElevated)
                            .border(1.dp, CyberTeal.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "CORE MESSAGE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTeal,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentSlide.coreMessage,
                                fontSize = 13.sp,
                                color = Color(0xFFE0E0E0),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "KEY STRATEGY & INSIGHTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    currentSlide.bulletPoints.forEach { point ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CyberTeal,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Text(
                                text = point,
                                fontSize = 13.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Highlights Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        currentSlide.highlights.forEach { (stat, label) ->
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF091424)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = stat,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp,
                                        color = CyberTeal
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Navigation Controls (Previous / Next Slide)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (selectedSlideIndex > 0) selectedSlideIndex--
                    },
                    enabled = selectedSlideIndex > 0,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Previous")
                }

                Text(
                    text = "${selectedSlideIndex + 1} of ${slides.size}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = {
                        if (selectedSlideIndex < slides.size - 1) selectedSlideIndex++
                    },
                    enabled = selectedSlideIndex < slides.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberTeal, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Next", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
