package com.persoff68.speechratemonitor.ui.shared.indicatorbackground

import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.persoff68.speechratemonitor.R
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
    val backgroundBitmap = ImageBitmap.imageResource(id = R.drawable.display_texture)

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawBackground(backgroundBitmap)
        }
    }
}

private fun DrawScope.drawBackground(arcBackgroundBitmap: ImageBitmap) {

    val arcTextureBrush = ShaderBrush(
        BitmapShader(
            arcBackgroundBitmap.asAndroidBitmap(),
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        ).apply { setLocalMatrix(Matrix().apply { setScale(0.8f, 0.8f) }) }
    )

    val arcBackgroundBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF606060), Color(0xFF303030)),
        start = Offset(Float.POSITIVE_INFINITY, 0f),
        end = Offset(0f, 0f)
    )

    drawRect(
        brush = arcTextureBrush,
        topLeft = Offset(0f, 0f),
        size = size,
        alpha = 0.5f
    )
    drawRect(
        brush = arcBackgroundBrush,
        topLeft = Offset(0f, 0f),
        size = size,
        alpha = 0.1f
    )

    drawRect(
        color = Color.White,
        topLeft = Offset(-10f, 0f),
        size = Size(size.width + 20f, size.height),
        style = Stroke(width = 5f, cap = StrokeCap.Butt),
        alpha = 0.8f
    )
}