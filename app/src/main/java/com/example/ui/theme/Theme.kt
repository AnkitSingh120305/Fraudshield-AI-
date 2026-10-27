package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CyberTeal,
    onPrimary = Color(0xFF00363D),
    primaryContainer = CyberNavyElevated,
    onPrimaryContainer = CyberTeal,
    secondary = CyberBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1A3560),
    onSecondaryContainer = Color(0xFFD1E4FF),
    tertiary = ThreatCaution,
    onTertiary = Color.Black,
    error = ThreatDanger,
    onError = Color.White,
    errorContainer = ThreatDangerContainer,
    onErrorContainer = Color(0xFFFFDAD6),
    background = CyberNavyDark,
    onBackground = TextPrimary,
    surface = CyberNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberNavyElevated,
    onSurfaceVariant = TextSecondary,
    outline = CyberNavyBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006874),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF97F0FF),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF1A5694),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E4FF),
    onSecondaryContainer = Color(0xFF001D36),
    tertiary = Color(0xFF785900),
    onTertiary = Color.White,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    background = LightSurface,
    onBackground = Color(0xFF191C1D),
    surface = LightSurfaceElevated,
    onSurface = Color(0xFF191C1D),
    surfaceVariant = Color(0xFFE0E3E3),
    onSurfaceVariant = Color(0xFF3F484A),
    outline = LightBorder
)

@Composable
fun FraudShieldTheme(
    darkTheme: Boolean = true, // Default to sleek cybersecurity dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
