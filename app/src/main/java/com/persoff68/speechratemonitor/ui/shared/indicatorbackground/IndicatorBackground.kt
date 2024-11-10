package com.persoff68.speechratemonitor.ui.shared.indicatorbackground

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.persoff68.speechratemonitor.ui.theme.Brushes
import com.persoff68.speechratemonitor.ui.theme.LocalBrushes
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun IndicatorBackgroundPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            IndicatorBackground()
        }
    }
}

@Composable
fun IndicatorBackground(modifier: Modifier = Modifier) {
    val brushes = LocalBrushes.current
    val backgroundBrush = brushes.indicatorBackgroundGradientBrush()
    val bordersColor = MaterialTheme.colorScheme.onSurface
    val settings by remember { mutableStateOf(IndicatorBackgroundSettings()) }

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawStroke(settings, bordersColor)
            drawTexture(brushes)
            drawBackground(backgroundBrush)
        }
    }
}

private fun DrawScope.drawStroke(settings: IndicatorBackgroundSettings, color: Color) {
    drawRect(
        color = color,
        topLeft = Offset(0f, -settings.strokeWidth),
        size = Size(size.width, size.height + settings.strokeWidth * 2),
        alpha = 0.6f
    )
}

private fun DrawScope.drawTexture(brushes: Brushes) {
    val textureBrush = brushes.textureBrush()
    drawRect(
        brush = textureBrush,
        topLeft = Offset(0f, 0f),
        size = size,
        alpha = 0.95f
    )
}

private fun DrawScope.drawBackground(brush: Brush) {
    drawRect(
        brush = brush,
        topLeft = Offset(0f, 0f),
        size = size,
        alpha = 0.05f
    )
}