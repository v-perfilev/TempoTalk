package com.persoff68.speechratemonitor.ui.shared.spectrogram

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun SpectrogramPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Spectrogram(
                spectrogramData = Array(40) { FloatArray(20) { Random.nextFloat() } },
                isRecording = true
            )
        }
    }
}

@Composable
fun Spectrogram(
    spectrogramData: Array<FloatArray>,
    isRecording: Boolean
) {
    val animatedSpectrogramData = remember { mutableStateListOf(*Config.DEFAULT_SPECTROGRAM) }
    LaunchSpectrogramAnimation(spectrogramData, animatedSpectrogramData, isRecording)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.05f to Color(0x15000000),
                        0.5f to Color(0x20000000),
                        0.95f to Color(0x15000000),
                        1f to Color.Transparent,
                    )
                )
            )
    ) {
        SpectrogramIndicator(animatedSpectrogramData.toTypedArray())
    }
}

@Composable
fun LaunchSpectrogramAnimation(
    spectrogramData: Array<FloatArray>,
    animatedSpectrogramData: MutableList<FloatArray>,
    isRecording: Boolean
) {
    val accumulator = remember { mutableStateListOf<FloatArray>() }
    var delayInMs by remember { mutableLongStateOf(0) }

    LaunchedEffect(isRecording) {
        if (!isRecording) {
            accumulator.clear()
            animatedSpectrogramData.clear()
            animatedSpectrogramData.addAll(Config.DEFAULT_SPECTROGRAM)
        }
    }

    LaunchedEffect(spectrogramData) {
        accumulator.addAll(spectrogramData)
        delayInMs = Config.FRAME_SIZE_IN_MS / accumulator.size

        while (accumulator.isNotEmpty()) {
            val item = accumulator.removeFirstOrNull() ?: break
            animatedSpectrogramData.add(item)
            animatedSpectrogramData.removeFirst()
            delay(delayInMs)
        }
    }
}


@Composable
private fun SpectrogramIndicator(
    spectrogramData: Array<FloatArray>
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val settings = SpectrogramSettings(size)

        for (i in spectrogramData.indices) {
            for (j in spectrogramData[i].indices) {
                val value = spectrogramData[i][j]
                val startX = i * settings.cellWidth
                val startY = j * settings.cellHeight
                drawSpectrogramRect(startX, startY, value, settings)
            }
        }
    }
}

private fun DrawScope.drawSpectrogramRect(
    startX: Float,
    startY: Float,
    value: Float,
    settings: SpectrogramSettings
) {
    val color = extractColorFromValue(value, settings)
    drawOval(
        color = color,
        topLeft = Offset(startX + 0.1f * settings.cellWidth, startY + 0.1f * settings.cellHeight),
        size = Size(settings.cellWidth * 0.8f, settings.cellHeight * 0.8f)
    )
}

fun extractColorFromValue(value: Float, settings: SpectrogramSettings): Color {
    val ratio = max(0f, min(value, 1f))

    val (startColor, endColor, localRatio) = if (ratio < settings.colorRatio) {
        Triple(settings.startColor, settings.middleColor, ratio / settings.colorRatio)
    } else {
        Triple(
            settings.middleColor,
            settings.endColor,
            (ratio - settings.colorRatio) / (1 - settings.colorRatio)
        )
    }

    val startR = startColor.red
    val startG = startColor.green
    val startB = startColor.blue
    val startAlpha = startColor.alpha

    val endR = endColor.red
    val endG = endColor.green
    val endB = endColor.blue
    val endAlpha = endColor.alpha

    val r = startR + (endR - startR) * localRatio
    val g = startG + (endG - startG) * localRatio
    val b = startB + (endB - startB) * localRatio
    val alpha = startAlpha + (endAlpha - startAlpha) * localRatio

    return Color(r, g, b, alpha)
}
