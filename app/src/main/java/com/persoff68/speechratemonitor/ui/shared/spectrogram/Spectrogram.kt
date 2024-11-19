package com.persoff68.speechratemonitor.ui.shared.spectrogram

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import com.persoff68.speechratemonitor.ui.theme.util.extractRatioColorFromValue
import kotlinx.coroutines.delay
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
    modifier: Modifier = Modifier,
    spectrogramData: Array<FloatArray>,
    isRecording: Boolean
) {
    val animatedSpectrogramData = remember { mutableStateListOf(*Config.DEFAULT_SPECTROGRAM) }
    LaunchSpectrogramAnimation(spectrogramData, animatedSpectrogramData, isRecording)

    val description = stringResource(R.string.main_spectrogram_label)
    Box(modifier = modifier
        .fillMaxWidth()
        .semantics { contentDescription = description }) {
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
        delayInMs = Config.FRAME_DURATION_MS / accumulator.size

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
    var settings by remember { mutableStateOf(SpectrogramSettings()) }

    fun extractColor(value: Float): Color =
        extractRatioColorFromValue(settings.relativeRatio, value)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { settings = SpectrogramSettings(it.toSize()) }
    ) {
        for (i in spectrogramData.indices) {
            for (j in spectrogramData[i].indices) {
                val value = spectrogramData[i][j]
                val startX = i * settings.cellWidth
                val startY = j * settings.cellHeight
                drawSpectrogramRect(settings, startX, startY, value, ::extractColor)
            }
        }
    }
}

private fun DrawScope.drawSpectrogramRect(
    settings: SpectrogramSettings,
    startX: Float,
    startY: Float,
    value: Float,
    extractColor: (value: Float) -> Color
) {
    val color = extractColor(value)
    drawOval(
        color = color,
        topLeft = Offset(startX + 0.1f * settings.cellWidth, startY + 0.1f * settings.cellHeight),
        size = Size(settings.cellWidth * 0.8f, settings.cellHeight * 0.8f)
    )
}


