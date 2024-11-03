package com.persoff68.speechratemonitor.ui.shared.waveform

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun WaveformPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Waveform(FloatArray(128) { Random.nextFloat() * 2f - 1f })
        }
    }
}

@Composable
fun Waveform(audioData: FloatArray) {

    val animatedAudioData = remember { audioData.map { Animatable(it) } }
    AnimateAudioData(audioData, animatedAudioData)
    val interpolatedAudioData = animatedAudioData.map { it.value }.toFloatArray()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f)
    ) {
        WaveformIndicator(interpolatedAudioData)
    }
}

@Composable
private fun WaveformIndicator(
    audioData: FloatArray,
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val settings = WaveformSettings(size, audioData)

        audioData.dropLast(1).forEachIndexed { i, _ ->
            val startX = i * settings.stepWidth
            val startY = settings.centerY - (audioData[i] * settings.centerY)
            val endX = (i + 1) * settings.stepWidth
            val endY = settings.centerY - (audioData[i + 1] * settings.centerY)

            drawWaveformGlow(startX, startY, endX, endY, settings)
            drawWaveformLine(startX, startY, endX, endY, settings)
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
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    settings: WaveformSettings
) {
    for (glowLayer in 1..5) {
        val alpha = 0.005f * (6 - glowLayer) * settings.volumeLevel
        val glowWidth = settings.strokeWidth * (glowLayer * 3)

        drawLine(
            brush = settings.brush,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = glowWidth,
            cap = StrokeCap.Round,
            alpha = alpha
        )
    }
}

private fun DrawScope.drawWaveformLine(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    settings: WaveformSettings
) {
    drawLine(
        brush = settings.brush,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = settings.strokeWidth,
        cap = StrokeCap.Round,
        alpha = (0.4f + settings.volumeLevel).coerceIn(0f, 1f)
    )
}