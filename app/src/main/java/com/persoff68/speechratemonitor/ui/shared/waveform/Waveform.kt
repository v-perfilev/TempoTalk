package com.persoff68.speechratemonitor.ui.shared.waveform

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import com.persoff68.speechratemonitor.ui.theme.waveformGradientBrush
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun WaveformPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Waveform(audioData = FloatArray(128) { Random.nextFloat() * 2f - 1f })
        }
    }
}

@Composable
fun Waveform(modifier: Modifier = Modifier, audioData: FloatArray) {

    val animatedAudioData = remember { audioData.map { Animatable(it) } }
    AnimateAudioData(audioData, animatedAudioData)
    val interpolatedAudioData = animatedAudioData.map { it.value }.toFloatArray()

    Box(modifier = modifier.fillMaxWidth()) {
        WaveformIndicator(interpolatedAudioData)
    }
}

@Composable
private fun WaveformIndicator(
    audioData: FloatArray,
) {
    var settings by remember { mutableStateOf(WaveformSettings()) }
    val volumeLevel = audioData.map { abs(it) / 0.3f }.average().toFloat().coerceIn(0f, 1f)
    val alpha = ((if (volumeLevel > 0) 0.4f else 0f) + volumeLevel).coerceIn(0f, 1f)
    val waveformBrush = waveformGradientBrush(volumeLevel)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { settings = WaveformSettings(it.toSize()) }
    ) {
        audioData.dropLast(1).forEachIndexed { i, _ ->
            val startX = i * size.width / (audioData.size - 1)
            val startY = settings.centerY - (audioData[i] * settings.centerY)
            val endX = (i + 1) * size.width / (audioData.size - 1)
            val endY = settings.centerY - (audioData[i + 1] * settings.centerY)

            drawWaveformGlow(settings, startX, startY, endX, endY, waveformBrush, volumeLevel)
            drawWaveformLine(settings, startX, startY, endX, endY, waveformBrush, alpha)
        }
    }
}

@Composable
private fun AnimateAudioData(
    audioData: FloatArray,
    animatedAudioData: List<Animatable<Float, AnimationVector1D>>,
) {
    LaunchedEffect(audioData) {
        animatedAudioData.forEachIndexed { i, value ->
            launch {
                value.animateTo(
                    targetValue = audioData[i],
                    animationSpec = tween(durationMillis = 100)
                )
            }
        }
    }
}

private fun DrawScope.drawWaveformGlow(
    settings: WaveformSettings,
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    brush: Brush,
    volumeLevel: Float
) {
    for (glowLayer in 1..5) {
        val alpha = 0.005f * (6 - glowLayer) * volumeLevel
        val glowWidth = settings.strokeWidth * (glowLayer * 3)

        drawLine(
            brush = brush,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = glowWidth,
            cap = StrokeCap.Round,
            alpha = alpha
        )
    }
}

private fun DrawScope.drawWaveformLine(
    settings: WaveformSettings,
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    brush: Brush,
    alpha: Float
) {
    drawLine(
        brush = brush,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = settings.strokeWidth,
        cap = StrokeCap.Round,
        alpha = alpha
    )
}