package com.persoff68.speechratemonitor.ui.shared.waveform

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

class WaveformSettings(size: Size, audioData: FloatArray) {
    val stepWidth = size.width / (audioData.size - 1)

    val centerY = size.height / 2

    val strokeWidth = 10f

    val volumeLevel = audioData.map { abs(it) / 0.3f }.average().toFloat().coerceIn(0f, 1f)

    val alpha = ((if (volumeLevel > 0) 0.4f else 0f) + volumeLevel).coerceIn(0f, 1f)

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.Yellow,
            interpolateColor(
                startColor = Color.White,
                endColor = Color.Green,
                fraction = volumeLevel
            ),
            Color.Yellow,
        )
    )
}

private fun interpolateColor(startColor: Color, endColor: Color, fraction: Float): Color {
    val startRed = startColor.red
    val startGreen = startColor.green
    val startBlue = startColor.blue

    val endRed = endColor.red
    val endGreen = endColor.green
    val endBlue = endColor.blue

    val interpolatedRed = startRed + (endRed - startRed) * fraction
    val interpolatedGreen = startGreen + (endGreen - startGreen) * fraction
    val interpolatedBlue = startBlue + (endBlue - startBlue) * fraction

    return Color(interpolatedRed, interpolatedGreen, interpolatedBlue, 1f)
}