package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CallScenario
import com.example.model.DialogueTurn
import com.example.model.RiskLevel
import com.example.model.Speaker
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
fun SimulatedCallScreen(
    scenario: CallScenario,
    viewModel: FraudShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isIncoming by viewModel.isCallIncoming.collectAsState()
    val isActive by viewModel.isCallActive.collectAsState()
    val callDuration by viewModel.callDurationSec.collectAsState()
    val dialogueHistory by viewModel.dialogueHistory.collectAsState()
    val evaluation by viewModel.currentEvaluation.collectAsState()
    val stallingAdviceVisible by viewModel.stallingAdviceVisible.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val isSpeaker by viewModel.isSpeaker.collectAsState()

    BackHandler {
        viewModel.endCall(byShield = false)
        viewModel.navigateBack()
    }

    if (isIncoming) {
        // Incoming Call Screen Layout
        IncomingCallView(
            scenario = scenario,
            onAnswer = { viewModel.answerCall() },
            onDecline = {
                viewModel.endCall(byShield = false)
                viewModel.navigateBack()
            }
        )
    } else {
        // Active Call Analysis View
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(CyberNavyDark)
                .testTag("active_call_screen")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Header: Back Button, Caller Title, and Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            viewModel.endCall(byShield = false)
                            viewModel.navigateBack()
                        },
                        modifier = Modifier.testTag("call_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = scenario.callerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        val minutes = callDuration / 60
                        val seconds = callDuration % 60
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            fontSize = 13.sp,
                            color = CyberTeal
                        )
                    }

                    ThreatBadge(riskLevel = evaluation.riskLevel)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Audio Waveform Visualizer
                AudioWaveformVisualizer(
                    isActive = isActive,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = when (evaluation.riskLevel) {
                        RiskLevel.LOW -> CyberTeal
                        RiskLevel.MEDIUM -> ThreatCaution
                        RiskLevel.HIGH -> ThreatDanger
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Real-time Threat Meter
                RealTimeThreatMeter(
                    score = evaluation.score,
                    riskLevel = evaluation.riskLevel
                )

                Spacer(modifier = Modifier.height(12.dp))

                // HIGH RISK PROACTIVE INTERVENTION BANNER
                AnimatedVisibility(
                    visible = evaluation.riskLevel == RiskLevel.HIGH,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("critical_fraud_alert_banner"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A0008)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, ThreatDanger)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = ThreatDanger,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "PROACTIVE FRAUD INTERVENTION",
                                    color = ThreatDanger,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Suspected Scam: ${evaluation.scamType}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Do NOT share OTP, PIN, passwords or transfer money. The caller is applying coercive pressure.",
                                color = Color(0xFFFFCDD2),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.endCall(byShield = true) },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThreatDanger),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("shield_disconnect_button")
                                ) {
                                    Icon(Icons.Default.CallEnd, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cut & Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.toggleStallingAdvice() },
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberTeal),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("stall_caller_button")
                                ) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = CyberTeal, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Counter Script", fontSize = 12.sp, color = CyberTeal)
                                }
                            }
                        }
                    }
                }

                // Streaming Transcript Stream
                Text(
                    text = "LIVE SPEECH TRANSCRIPTION (MULTILINGUAL NLP)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CyberNavySurface)
                        .border(1.dp, CyberNavyBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                        .testTag("transcript_stream_list"),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (dialogueHistory.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = CyberTeal, modifier = Modifier.size(28.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Connecting audio stream & speech recognition...",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(dialogueHistory) { turn ->
                            TranscriptBubble(turn = turn)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Call Controls: Mute, Speaker, End Call
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute Button
                    IconButton(
                        onClick = { viewModel.toggleMute() },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isMuted) ThreatDanger.copy(alpha = 0.2f) else CyberNavyElevated)
                            .testTag("mute_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            tint = if (isMuted) ThreatDanger else Color.White
                        )
                    }

                    // End Call Primary Button
                    IconButton(
                        onClick = { viewModel.endCall(byShield = false) },
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ThreatDanger)
                            .testTag("hangup_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Speaker Button
                    IconButton(
                        onClick = { viewModel.toggleSpeaker() },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isSpeaker) CyberTeal.copy(alpha = 0.2f) else CyberNavyElevated)
                            .testTag("speaker_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isSpeaker) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Speaker",
                            tint = if (isSpeaker) CyberTeal else Color.White
                        )
                    }
                }
            }
        }
    }

    // Stalling Advice Modal
    if (stallingAdviceVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleStallingAdvice() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = CyberTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Counter-Manipulation Scripts", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Read any of these phrases aloud to neutralize high-pressure manipulation:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    CounterScriptItem(
                        title = "Verify in person",
                        script = "\"I am ending this call to visit my nearest police station / bank branch in person right now.\""
                    )
                    CounterScriptItem(
                        title = "Digital Arrest challenge",
                        script = "\"Per Supreme Court and I4C rules, there is no legal concept of 'Digital Arrest' over phone or video call.\""
                    )
                    CounterScriptItem(
                        title = "Demand official ID",
                        script = "\"Please provide your official police station landline and officer badge ID for my advocate to call.\""
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.toggleStallingAdvice() }) {
                    Text("Close", color = CyberTeal)
                }
            },
            containerColor = CyberNavyElevated
        )
    }
}

@Composable
fun CounterScriptItem(title: String, script: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavySurface)
            .border(1.dp, CyberNavyBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyberTeal)
            Spacer(modifier = Modifier.height(2.dp))
            Text(script, fontSize = 12.sp, color = Color.White)
        }
    }
}

@Composable
fun TranscriptBubble(turn: DialogueTurn) {
    val isCaller = turn.speaker == Speaker.CALLER
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = if (isCaller) Alignment.Start else Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (isCaller) "SUSPECT / CALLER" else "VICTIM / YOU",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCaller) ThreatDanger else CyberTeal
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isCaller) Color(0xFF1E2638) else Color(0xFF13324E))
                .border(
                    1.dp,
                    if (turn.highlightWords.isNotEmpty()) ThreatDanger.copy(alpha = 0.6f) else CyberNavyBorder,
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = turn.text,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun IncomingCallView(
    scenario: CallScenario,
    onAnswer: () -> Unit,
    onDecline: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberNavyDark)
            .padding(24.dp)
            .testTag("incoming_call_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Caller Identity & Security Badge
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(CyberNavyElevated)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = CyberTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FRAUDSHIELD ACTIVE MONITORING",
                        color = CyberTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Avatar Icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(CyberNavyElevated)
                        .border(2.dp, CyberTeal.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = scenario.callerName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = scenario.callerNumber,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Incoming Simulated Audio Stream...",
                    fontSize = 13.sp,
                    color = CyberTeal
                )
            }

            // Bottom Section: Answer / Decline Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Decline Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = onDecline,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(ThreatDanger)
                            .testTag("decline_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "Decline",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Decline", color = Color.White, fontSize = 13.sp)
                }

                // Answer Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = onAnswer,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(ThreatSafe)
                            .testTag("answer_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Answer",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Answer & Protect", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
