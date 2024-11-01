package com.persoff68.speechratemonitor.ui.main

import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.DarkGradient
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun GaugeScreenPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            GaugeScreen(Modifier)
        }
    }
}

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>) {
    animation.animateTo(0f, keyframes {
        durationMillis = 5000
        0f at 0 using CubicBezierEasing(0f, 1.5f, 0.8f, 1f)
        0.6f at 1000 using CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.8f at 2000 using CubicBezierEasing(0.2f, -1.8f, 0f, 1f)
        0.6f at 4000 using LinearOutSlowInEasing
    })
}

@Composable
fun GaugeScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val value = remember { Animatable(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkGradient),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(modifier = Modifier)
        GaugeIndicator(value.value) { coroutineScope.launch { startAnimation(value) } }
        Box(modifier = Modifier)
    }
}


@Composable
fun GaugeIndicator(value: Float, onClick: () -> Unit) {
    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 200, easing = EaseOut),
        label = ""
    )

    val animationState = createGaugeAnimationState()

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        CircularGaugeIndicator(
            animatedValue,
            animationState,
            onClick
        )
    }
}

@Composable
private fun CircularGaugeIndicator(
    value: Float,
    animationState: GaugeAnimationState,
    onClick: () -> Unit
) {
    val arcBackgroundBitmap = ImageBitmap.imageResource(id = R.drawable.gauge_arc_texture)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() }
                )
            }
    ) {
        val settings = GaugeSettings(size, arcBackgroundBitmap)
        val startAngle = 90f + (360f - settings.arcAngle) / 2

        rotate(startAngle) {
            drawGlowArc(value, settings)
            drawTicks(settings, animationState.ticks)
            drawStrokeArc(settings, animationState.arc)
            drawBackgroundArc(settings, animationState.arc)
            drawValueArc(value, settings)
            drawNeedle(value, settings, animationState.needle)

        }
    }
}

private fun DrawScope.drawTicks(settings: GaugeSettings, animationValue: Float) {
    val tickAngle = (settings.arcAngle - settings.strokeAngle * 2) / settings.tickCount
    for (i in 0..(settings.tickCount * animationValue).toInt()) {
        rotate(degrees = 90f + settings.strokeAngle + i * tickAngle) {
            drawLine(
                color = settings.tickColor,
                start = Offset(
                    settings.size.width / 2,
                    settings.arcWidth + settings.strokeWidth / 2
                ),
                end = Offset(
                    settings.size.width / 2,
                    settings.arcWidth + settings.strokeWidth / 2 + settings.tickLength
                ),
                strokeWidth = settings.tickWidth,
            )
        }
    }
}

private fun DrawScope.drawGlowArc(value: Float, settings: GaugeSettings) {
    for (i in 0..20) {
        drawArc(
            brush = settings.arcGradientBrush,
            startAngle = 0f,
            sweepAngle = value * settings.arcAngle,
            useCenter = false,
            topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
            size = Size(size.width - settings.arcWidth, size.height - settings.arcWidth),
            style = Stroke(
                width = settings.arcWidth + (20 - i) * settings.glowParameter,
                cap = StrokeCap.Round
            ),
            alpha = i / 800f,
        )
    }
}

private fun DrawScope.drawStrokeArc(settings: GaugeSettings, animationValue: Float) {
    drawArc(
        color = settings.strokeColor,
        startAngle = -settings.strokeAngle * animationValue,
        sweepAngle = settings.arcAngle * animationValue + settings.strokeAngle * 2 * animationValue,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth + settings.strokeWidth, cap = StrokeCap.Butt),
    )
}

private fun DrawScope.drawBackgroundArc(settings: GaugeSettings, animationValue: Float) {
    drawArc(
        brush = settings.arcTextureBrush,
        startAngle = 0f,
        sweepAngle = settings.arcAngle * animationValue,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth, cap = StrokeCap.Butt),
    )
    drawArc(
        brush = settings.arcBackgroundBrush,
        startAngle = 0f,
        sweepAngle = settings.arcAngle * animationValue,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.5f
    )
}

private fun DrawScope.drawValueArc(value: Float, settings: GaugeSettings) {
    drawArc(
        brush = settings.arcGradientBrush,
        startAngle = 0f,
        sweepAngle = value * settings.arcAngle,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.5f
    )
}

private fun DrawScope.drawNeedle(
    value: Float,
    settings: GaugeSettings,
    animationValue: Float
) {
    val needleAngle = value * settings.arcAngle

    rotate(degrees = needleAngle) {

        val path = Path().apply {
            moveTo(settings.center.x, center.y - settings.needleWidth / 2)
            lineTo(settings.center.x, center.y + settings.needleWidth / 2)
            lineTo(
                settings.center.x + settings.needleLength * animationValue - 20f * animationValue,
                center.y + settings.needleWidth / 6
            )
            lineTo(settings.center.x + settings.needleLength * animationValue, center.y)
            lineTo(
                settings.center.x + settings.needleLength * animationValue - 20f * animationValue,
                center.y - settings.needleWidth / 6
            )
            close()
        }

        drawPath(
            path = path,
            brush = settings.needleBrush,
        )
    }
}

@Composable
fun createGaugeAnimationState(): GaugeAnimationState {
    val coroutineScope = rememberCoroutineScope()
    val isInPreview = LocalInspectionMode.current

    if (isInPreview) {
        return GaugeAnimationState(
            arc = 1f,
            ticks = 1f,
            needle = 1f
        )
    }

    val arcAnimationParameter = remember { Animatable(0f) }
    val ticksAnimationParameter = remember { Animatable(-1f) }
    val needleAnimationParameter = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val arcAnimation = launch {
                delay(500)
                arcAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = Ease)
                )
            }
            val ticksAnimation = launch {
                delay(1500)
                ticksAnimationParameter.animateTo(0f)
                ticksAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = LinearEasing)
                )
            }
            val needleAnimation = launch {
                delay(2000)
                needleAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = EaseInOut)
                )
            }

            arcAnimation.join()
            ticksAnimation.join()
            needleAnimation.join()
        }
    }

    return GaugeAnimationState(
        arc = arcAnimationParameter.value,
        ticks = ticksAnimationParameter.value,
        needle = needleAnimationParameter.value
    )
}

class GaugeSettings(val size: Size, arcBackgroundBitmap: ImageBitmap) {
    val center: Offset = Offset(size.width / 2, size.height / 2)

    val arcAngle: Float = 210f
    val arcWidth: Float = 120f

    val glowParameter: Float = 15f

    val strokeWidth: Float = 4f
    val strokeAngle: Float = 0.3f
    val strokeColor: Color = Color(0xFFFFFFFF)

    val tickCount: Int = 8
    val tickLength: Float = 20f
    val tickWidth: Float = 5f
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

class GaugeAnimationState(
    val arc: Float = 1f,
    val ticks: Float = 1f,
    val needle: Float = 1f
)