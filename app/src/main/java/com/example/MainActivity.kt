package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FraudShieldViewModel
import com.example.ui.Screen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IncidentReportScreen
import com.example.ui.screens.IntelligenceStatsScreen
import com.example.ui.screens.LiveMicDetectionScreen
import com.example.ui.screens.PitchArchitectureScreen
import com.example.ui.screens.SimulatedCallScreen
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.FraudShieldTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FraudShieldViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FraudShieldTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: FraudShieldViewModel) {
    val currentScreen by viewModel.currentScreenState.collectAsState()

    // Determine whether to show the bottom navigation bar (only on root/parent destinations)
    val showBottomBar = currentScreen is Screen.Home ||
            currentScreen is Screen.PitchArchitecture ||
            currentScreen is Screen.IntelligenceStats ||
            currentScreen is Screen.History

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CyberNavySurface,
                    contentColor = CyberTeal,
                    tonalElevation = 4.dp,
                    modifier = Modifier.testTag("main_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen is Screen.Home,
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Shield", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberNavySurface,
                            selectedTextColor = CyberTeal,
                            indicatorColor = CyberTeal,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.PitchArchitecture,
                        onClick = { viewModel.navigateTo(Screen.PitchArchitecture) },
                        icon = { Icon(Icons.Default.Slideshow, contentDescription = "Pitch") },
                        label = { Text("7-Slides", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberNavySurface,
                            selectedTextColor = CyberTeal,
                            indicatorColor = CyberTeal,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_pitch")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.IntelligenceStats,
                        onClick = { viewModel.navigateTo(Screen.IntelligenceStats) },
                        icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Stats") },
                        label = { Text("Stats", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberNavySurface,
                            selectedTextColor = CyberTeal,
                            indicatorColor = CyberTeal,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_stats")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.History,
                        onClick = { viewModel.navigateTo(Screen.History) },
                        icon = { Icon(Icons.Default.Description, contentDescription = "Reports") },
                        label = { Text("Vault", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberNavySurface,
                            selectedTextColor = CyberTeal,
                            indicatorColor = CyberTeal,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_history")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.SimulatedCall -> SimulatedCallScreen(
                    scenario = screen.scenario,
                    viewModel = viewModel
                )
                is Screen.LiveMicDetection -> LiveMicDetectionScreen(viewModel = viewModel)
                is Screen.ReportDetail -> IncidentReportScreen(
                    report = screen.report,
                    viewModel = viewModel
                )
                is Screen.PitchArchitecture -> PitchArchitectureScreen(viewModel = viewModel)
                is Screen.IntelligenceStats -> IntelligenceStatsScreen(viewModel = viewModel)
                is Screen.History -> HistoryScreen(viewModel = viewModel)
            }
        }
    }
}
