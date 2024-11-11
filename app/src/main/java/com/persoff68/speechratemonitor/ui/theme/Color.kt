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

val GreenColor = Color(0xFF32BD37)
val OrangeColor = Color(0xFFFFC600)
val YellowColor = Color(0xFFFFEF00)
val RedColor = Color(0xFFC62828)
val WhiteColor = Color(0xFFFFFFFF)
val GrayColor100 = Color(0xFFF5F5F5)
val GrayColor200 = Color(0xFFEEEEEE)
val GrayColor300 = Color(0xFFE0E0E0)
val GrayColor400 = Color(0xFFBDBDBD)
val GrayColor500 = Color(0xFF9E9E9E)
val GrayColor600 = Color(0xFF757575)
val GrayColor700 = Color(0xFF616161)
val GrayColor800 = Color(0xFF424242)
val GrayColor900 = Color(0xFF212121)
val LightColor1 = Color(0xFFF3F3F3)
val LightColor2 = Color(0xFFEEEEEE)
val DarkColor1 = Color(0xFF151925)
val DarkColor2 = Color(0xFF101421)
val Transparent = Color(0x00000000)


fun textureBrush(bitmap: ImageBitmap, sx: Float = 1f, sy: Float = 1f): Brush = ShaderBrush(
    BitmapShader(
        bitmap.asAndroidBitmap(),
        Shader.TileMode.REPEAT,
        Shader.TileMode.REPEAT
    ).apply { setLocalMatrix(Matrix().apply { setScale(sx, sy) }) }
)


fun darkBackgroundGradientBrush() = Brush.verticalGradient(
    colors = listOf(DarkColor1, DarkColor2)
)

fun darkLabelGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GreenColor, OrangeColor, RedColor)
)

fun darkGaugeBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GrayColor400, Transparent),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun darkIndicatorBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(Transparent, GrayColor400),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun darkGaugeGradientBrush(center: Offset, arcAngle: Float): Brush = Brush.sweepGradient(
    colorStops = arrayOf(
        0f * arcAngle / 360f to Transparent,
        0.4f * arcAngle / 360f to GreenColor,
        0.65f * arcAngle / 360f to OrangeColor,
        0.9f * arcAngle / 360f to RedColor,
        1f to Transparent
    ),
    center = center
)

fun darkNeedleGradientBrush(center: Offset, needleLength: Float): Brush = Brush.linearGradient(
    colors = listOf(Transparent, GrayColor500, WhiteColor),
    start = Offset(center.x, 0f),
    end = Offset(center.x + needleLength, 0f)
)

fun darkWaveformGradientBrush(volumeLevel: Float): Brush = Brush.verticalGradient(
    colors = listOf(
        OrangeColor,
        interpolateColor(
            startColor = WhiteColor,
            endColor = GreenColor,
            fraction = volumeLevel
        ),
        OrangeColor,
    )
)


fun lightBackgroundGradientBrush() = Brush.verticalGradient(
    colors = listOf(LightColor1, LightColor2)
)

fun lightLabelGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GreenColor, OrangeColor, RedColor)
)

fun lightGaugeBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GrayColor500, Transparent),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun lightIndicatorBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(Transparent, GrayColor500),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun lightGaugeGradientBrush(center: Offset, arcAngle: Float): Brush = Brush.sweepGradient(
    colorStops = arrayOf(
        0f * arcAngle / 360f to Transparent,
        0.4f * arcAngle / 360f to GreenColor,
        0.65f * arcAngle / 360f to OrangeColor,
        0.9f * arcAngle / 360f to RedColor,
        1f to Transparent
    ),
    center = center
)

fun lightNeedleGradientBrush(center: Offset, needleLength: Float): Brush = Brush.linearGradient(
    colors = listOf(Transparent, RedColor, RedColor),
    start = Offset(center.x, 0f),
    end = Offset(center.x + needleLength, 0f)
)

fun lightWaveformGradientBrush(volumeLevel: Float): Brush = Brush.verticalGradient(
    colors = listOf(
        OrangeColor,
        interpolateColor(
            startColor = GrayColor800,
            endColor = GreenColor,
            fraction = volumeLevel
        ),
        OrangeColor,
    )
)