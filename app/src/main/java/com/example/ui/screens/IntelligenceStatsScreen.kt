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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun IntelligenceStatsScreen(
    viewModel: FraudShieldViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberNavyDark)
            .testTag("intelligence_stats_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("stats_back_button")
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
                        text = "Cybercrime Intelligence",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "I4C & NCRB Official Data on Indian Social Engineering",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Key Statistics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        value = "₹1,750+ Cr",
                        label = "Lost to Digital Arrest & Impersonation (2024)",
                        color = ThreatDanger,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = "70,000+",
                        label = "Daily Calls to 1930 Cyber Fraud Helpline",
                        color = ThreatCaution,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        value = "< 15 Mins",
                        label = "Average Time from Panic to Money Transfer",
                        color = CyberTeal,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = "85%+",
                        label = "Calls Involve Authority Impersonation",
                        color = CyberBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Psychological Manipulation Lifecycle Arc (Slide 1)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "THE MANIPULATION LIFECYCLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTeal,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Why Victims Disclose Under Pressure",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LifecycleStep(
                        step = "1",
                        title = "Unknown Caller (Spoofed Identity)",
                        description = "Caller claims to be Police, CBI, Bank Manager, or DISCOM."
                    )
                    LifecycleStep(
                        step = "2",
                        title = "Psychological Terror & Urgency",
                        description = "Immediate arrest warrant, narcotics seizure, or account block within minutes."
                    )
                    LifecycleStep(
                        step = "3",
                        title = "Isolation & Line Holding",
                        description = "\"Do not disconnect this line or inform your family! National security investigation.\""
                    )
                    LifecycleStep(
                        step = "4",
                        title = "Irreversible Disclosure / Transfer",
                        description = "Victim transfers savings to 'Verification Escrow' or shares secret OTP."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ThreatSafe.copy(alpha = 0.15f))
                            .border(1.dp, ThreatSafe.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "🛡️ FraudShield Intervenes at Step 2 & 3 — before Step 4 happens!",
                            color = ThreatSafe,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Target Beneficiaries (Slide 2)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberNavyElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TARGET BENEFICIARIES (HIGH VULNERABILITY)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTeal,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BeneficiaryItem(
                        icon = Icons.Default.Elderly,
                        title = "Senior Citizens & Retirees",
                        reason = "Targeted by Digital Arrest scams due to lifetime pension savings and deference to authority."
                    )
                    BeneficiaryItem(
                        icon = Icons.Default.AccountBalance,
                        title = "Rural & First-Time Digital Payment Users",
                        reason = "Unfamiliar with difference between receiving and sending money via UPI QR codes."
                    )
                    BeneficiaryItem(
                        icon = Icons.Default.School,
                        title = "Students & Job Seekers",
                        reason = "Vulnerable to part-time Telegram rating tasks and advance-fee employment traps."
                    )
                    BeneficiaryItem(
                        icon = Icons.Default.Group,
                        title = "Working Professionals",
                        reason = "Distracted during busy work hours; panic over utility cutoff or courier parcel issues."
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 14.sp)
        }
    }
}

@Composable
fun LifecycleStep(step: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(CyberTeal.copy(alpha = 0.2f))
                .border(1.dp, CyberTeal, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(step, color = CyberTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
        }
    }
}

@Composable
fun BeneficiaryItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, reason: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = CyberTeal, modifier = Modifier.size(22.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Text(reason, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
        }
    }
}
