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
val YellowColor = Color(0xFFE5B400)
val RedColor = Color(0xFFC62828)
val GrayColor1 = Color(0xFFEEEEEE)
val GrayColor2 = Color(0xFFDDDDDD)
val GrayColor3 = Color(0xFFAAAAAA)
val GrayColor4 = Color(0xFF505050)
val GrayColor5 = Color(0xFF151925)
val GrayColor6 = Color(0xFF101421)
val Transparent = Color(0x00000000)
val WhiteColor = Color(0xFFFFFFFF)


fun textureBrush(bitmap: ImageBitmap, sx: Float = 1f, sy: Float = 1f): Brush = ShaderBrush(
    BitmapShader(
        bitmap.asAndroidBitmap(),
        Shader.TileMode.REPEAT,
        Shader.TileMode.REPEAT
    ).apply { setLocalMatrix(Matrix().apply { setScale(sx, sy) }) }
)


fun darkBackgroundGradientBrush() = Brush.verticalGradient(
    colors = listOf(GrayColor5, GrayColor6)
)

fun darkLabelGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GreenColor, YellowColor, RedColor)
)

fun darkGaugeBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GrayColor3, Transparent),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun darkIndicatorBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(Transparent, GrayColor3),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun darkGaugeGradientBrush(center: Offset, arcAngle: Float): Brush = Brush.sweepGradient(
    colorStops = arrayOf(
        0f * arcAngle / 360f to Transparent,
        0.4f * arcAngle / 360f to GreenColor,
        0.65f * arcAngle / 360f to YellowColor,
        0.9f * arcAngle / 360f to RedColor,
        1f to Transparent
    ),
    center = center
)

fun darkNeedleGradientBrush(center: Offset, needleLength: Float): Brush = Brush.linearGradient(
    colors = listOf(Transparent, GrayColor3, WhiteColor),
    start = Offset(center.x, 0f),
    end = Offset(center.x + needleLength, 0f)
)

fun darkWaveformGradientBrush(volumeLevel: Float): Brush = Brush.verticalGradient(
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


fun lightBackgroundGradientBrush() = Brush.verticalGradient(
    colors = listOf(GrayColor1, GrayColor2)
)

fun lightLabelGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GreenColor, YellowColor, RedColor)
)

fun lightGaugeBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(GrayColor4, Transparent),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun lightIndicatorBackgroundGradientBrush(): Brush = Brush.linearGradient(
    colors = listOf(Transparent, GrayColor3),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

fun lightGaugeGradientBrush(center: Offset, arcAngle: Float): Brush = Brush.sweepGradient(
    colorStops = arrayOf(
        0f * arcAngle / 360f to Transparent,
        0.4f * arcAngle / 360f to GreenColor,
        0.65f * arcAngle / 360f to YellowColor,
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
        YellowColor,
        interpolateColor(
            startColor = GrayColor5,
            endColor = GreenColor,
            fraction = volumeLevel
        ),
        YellowColor,
    )
)