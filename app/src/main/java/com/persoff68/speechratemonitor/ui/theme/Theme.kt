package com.persoff68.speechratemonitor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.settings.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = GreenColor,
    onPrimary = WhiteColor,
    secondary = RedColor,
    onSecondary = WhiteColor,
    surface = GrayColor2,
    surfaceVariant = GrayColor2,
    onSurface = GrayColor4,
    onSurfaceVariant = GrayColor3,
    background = GrayColor1,
    onBackground = GrayColor4
)

private val DarkColorScheme = darkColorScheme(
    primary = GreenColor,
    onPrimary = WhiteColor,
    secondary = RedColor,
    onSecondary = WhiteColor,
    surface = GrayColor5,
    onSurface = GrayColor2,
    surfaceVariant = GrayColor5,
    onSurfaceVariant = GrayColor4,
    background = GrayColor6,
    onBackground = GrayColor2,
)

fun darkBrushes(imageBitmap: ImageBitmap): Brushes = Brushes(
    textureBrush = { textureBrush(imageBitmap, 0.8f, 0.8f) },
    backgroundGradientBrush = ::darkBackgroundGradientBrush,
    labelGradientBrush = ::darkLabelGradientBrush,
    gaugeBackgroundGradientBrush = ::darkGaugeBackgroundGradientBrush,
    indicatorBackgroundGradientBrush = ::darkIndicatorBackgroundGradientBrush,
    gaugeGradientBrush = ::darkGaugeGradientBrush,
    needleGradientBrush = ::darkNeedleGradientBrush,
    waveformGradientBrush = ::darkWaveformGradientBrush
)

fun lightBrushes(imageBitmap: ImageBitmap): Brushes = Brushes(
    textureBrush = { textureBrush(imageBitmap, 0.8f, 0.8f) },
    backgroundGradientBrush = ::lightBackgroundGradientBrush,
    labelGradientBrush = ::lightLabelGradientBrush,
    gaugeBackgroundGradientBrush = ::lightGaugeBackgroundGradientBrush,
    indicatorBackgroundGradientBrush = ::lightIndicatorBackgroundGradientBrush,
    gaugeGradientBrush = ::lightGaugeGradientBrush,
    needleGradientBrush = ::lightNeedleGradientBrush,
    waveformGradientBrush = ::lightWaveformGradientBrush
)

val LocalBrushes = staticCompositionLocalOf { darkBrushes(ImageBitmap(1, 1)) }

val LocalDarkTheme = compositionLocalOf { false }


@Composable
fun SpeechRateMonitorAppTheme(
    theme: ThemeMode = ThemeMode.Light,
    content: @Composable () -> Unit
) {
    val darkTextureBitmap = ImageBitmap.imageResource(id = R.drawable.dark_display_texture)
    val lightTextureBitmap = ImageBitmap.imageResource(id = R.drawable.light_display_texture)

    val isDarkTheme = when (theme) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    val brushes = if (isDarkTheme) darkBrushes(darkTextureBitmap)
    else lightBrushes(lightTextureBitmap)

    CompositionLocalProvider(
        LocalBrushes provides brushes,
        LocalDarkTheme provides isDarkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

data class Brushes(
    val backgroundGradientBrush: () -> Brush,
    val labelGradientBrush: () -> Brush,
    val textureBrush: () -> Brush,
    val gaugeBackgroundGradientBrush: () -> Brush,
    val indicatorBackgroundGradientBrush: () -> Brush,
    val gaugeGradientBrush: (center: Offset, arcAngle: Float) -> Brush,
    val needleGradientBrush: (center: Offset, needleLength: Float) -> Brush,
    val waveformGradientBrush: (volumeLevel: Float) -> Brush
)