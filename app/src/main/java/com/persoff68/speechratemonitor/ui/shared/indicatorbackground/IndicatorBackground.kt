package com.persoff68.speechratemonitor.ui.shared.indicatorbackground

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import com.persoff68.speechratemonitor.ui.theme.indicatorBackgroundGradientBrush
import com.persoff68.speechratemonitor.ui.theme.textureBrush

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
    val textureBitmap = ImageBitmap.imageResource(id = R.drawable.display_texture)
    val backgroundBrush = indicatorBackgroundGradientBrush()
    val bordersColor = MaterialTheme.colorScheme.onSurface

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            drawTexture(textureBitmap)
            drawBackground(backgroundBrush)
            drawBorders(bordersColor)
        }
    }
}

private fun DrawScope.drawTexture(textureBitmap: ImageBitmap) {
    val textureBrush = textureBrush(bitmap = textureBitmap, sx = 0.8f, sy = 0.8f)
    drawRect(
        brush = textureBrush,
        topLeft = Offset(0f, 0f),
        size = size,
        alpha = 0.3f
    )
}

private fun DrawScope.drawBackground(brush: Brush) {
    drawRect(
        brush = brush,
        topLeft = Offset(0f, 0f),
        size = size,
        alpha = 0.1f
    )
}

private fun DrawScope.drawBorders(color: Color) {
    drawRect(
        color = color,
        topLeft = Offset(-10f, 0f),
        size = Size(size.width + 20f, size.height),
        style = Stroke(width = 5f, cap = StrokeCap.Butt),
        alpha = 0.8f
    )
}