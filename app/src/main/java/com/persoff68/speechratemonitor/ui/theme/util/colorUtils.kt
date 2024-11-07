package com.persoff68.speechratemonitor.ui.theme.util

import androidx.compose.ui.graphics.Color
import com.persoff68.speechratemonitor.ui.theme.GreenColor
import com.persoff68.speechratemonitor.ui.theme.Transparent
import com.persoff68.speechratemonitor.ui.theme.YellowColor
import kotlin.math.max
import kotlin.math.min

fun interpolateColor(startColor: Color, endColor: Color, fraction: Float): Color {
    val startRed = startColor.red
    val startGreen = startColor.green
    val startBlue = startColor.blue

    val endRed = endColor.red
    val endGreen = endColor.green
    val endBlue = endColor.blue

    val interpolatedRed = startRed + (endRed - startRed) * fraction
    val interpolatedGreen = startGreen + (endGreen - startGreen) * fraction
    val interpolatedBlue = startBlue + (endBlue - startBlue) * fraction

    return Color(interpolatedRed, interpolatedGreen, interpolatedBlue, 1f)
}

fun extractRatioColorFromValue(
    relativeRatio: Float,
    value: Float
): Color {
    val ratio = max(0f, min(value, 1f))

    val (startColor, endColor, localRatio) = if (ratio < relativeRatio) {
        Triple(Transparent, GreenColor, ratio / relativeRatio)
    } else {
        Triple(
            GreenColor,
            YellowColor,
            (ratio - relativeRatio) / (1 - relativeRatio)
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