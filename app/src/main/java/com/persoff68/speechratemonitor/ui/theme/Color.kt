package com.persoff68.speechratemonitor.ui.theme

import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asAndroidBitmap
import com.persoff68.speechratemonitor.ui.theme.util.interpolateColor

val GreenColor = Color(0xFF43A047)
val YellowColor = Color(0xFFFFEA00)
val RedColor = Color(0xFFC62828)
val LightColor1 = Color(0xFFAAAAAA)
val LightColor2 = Color(0xFF505050)
val DarkColor1 = Color(0xFF101421)
val DarkColor2 = Color(0xFF151925)
val Transparent = Color(0x00000000)
val WhiteColor = Color(0xFFFFFFFF)

fun backgroundGradientBrush() = Brush.verticalGradient(
    colors = listOf(DarkColor2, DarkColor1)
)

fun labelGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GreenColor, YellowColor, RedColor)
)

fun textureBrush(bitmap: ImageBitmap, sx: Float = 1f, sy: Float = 1f): Brush = ShaderBrush(
    BitmapShader(
        bitmap.asAndroidBitmap(),
        Shader.TileMode.REPEAT,
        Shader.TileMode.REPEAT
    ).apply { setLocalMatrix(Matrix().apply { setScale(sx, sy) }) }
)

fun gaugeBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(LightColor1, Transparent),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun indicatorBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(Transparent, LightColor1),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun gaugeGradientBrush(center: Offset, arcAngle: Float): Brush = Brush.sweepGradient(
    colorStops = arrayOf(
        0f * arcAngle / 360f to Transparent,
        0.4f * arcAngle / 360f to GreenColor,
        0.65f * arcAngle / 360f to YellowColor,
        0.9f * arcAngle / 360f to RedColor,
        1f to Transparent
    ),
    center = center
)

fun needleGradientBrush(center: Offset, needleLength: Float): Brush = Brush.linearGradient(
    colors = listOf(Transparent, LightColor1, WhiteColor),
    start = Offset(center.x, 0f),
    end = Offset(center.x + needleLength, 0f)
)

fun waveformGradientBrush(volumeLevel: Float): Brush = Brush.verticalGradient(
    colors = listOf(
        YellowColor,
        interpolateColor(
            startColor = WhiteColor,
            endColor = GreenColor,
            fraction = volumeLevel
        ),
        YellowColor,
    )
)
