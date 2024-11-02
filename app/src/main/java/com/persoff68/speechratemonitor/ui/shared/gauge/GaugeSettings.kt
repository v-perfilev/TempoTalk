package com.persoff68.speechratemonitor.ui.shared.gauge

import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asAndroidBitmap

class GaugeSettings(val size: Size, arcBackgroundBitmap: ImageBitmap) {
    val center: Offset = Offset(size.width / 2, size.height / 2)

    val arcAngle: Float = 210f
    val arcWidth: Float = 120f

    val glowParameter: Float = 15f

    val strokeWidth: Float = 8f
    val strokeAngle: Float = 0.6f
    val strokeColor: Color = Color(0xFFFFFFFF)

    val tickCount: Int = 8
    val tickLength: Float = 20f
    val tickWidth: Float = 4f
    val tickAngle: Float = 0.2f
    val tickColor: Color = Color(0xFFFFFFFF)

    val needleLength: Float = size.minDimension / 2 - arcWidth - 40f
    val needleWidth: Float = 40f

    val arcTextureBrush = ShaderBrush(
        BitmapShader(
            arcBackgroundBitmap.asAndroidBitmap(),
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        ).apply { setLocalMatrix(Matrix().apply { setScale(0.8f, 0.8f) }) }
    )

    val arcBackgroundBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF606060), Color(0xFF303030)),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    val arcGradientBrush: Brush = Brush.sweepGradient(
        colorStops = arrayOf(
            0f * arcAngle / 360f to Color.Transparent,
            0.4f * arcAngle / 360f to Color.Green,
            0.65f * arcAngle / 360f to Color.Yellow,
            0.9f * arcAngle / 360f to Color.Red,
            1f to Color.Transparent
        ),
        center = center
    )

    val needleBrush: Brush = Brush.linearGradient(
        colors = listOf(Color.Transparent, Color.LightGray, Color.White),
        start = Offset(center.x, 0f),
        end = Offset(center.x + needleLength, 0f)
    )
}