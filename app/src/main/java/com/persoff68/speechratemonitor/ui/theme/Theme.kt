package com.persoff68.speechratemonitor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = darkColorScheme(
    primary = GreenColor,
    onPrimary = WhiteColor,
    secondary = RedColor,
    onSecondary = WhiteColor,
    surface = DarkColor2,
    onSurface = WhiteColor,
    onSurfaceVariant = LightColor2,
    background = DarkColor1,
    onBackground = LightColor1,
)

@Composable
fun SpeechRateMonitorAppTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}