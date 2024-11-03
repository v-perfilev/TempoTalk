package com.persoff68.speechratemonitor.ui.shared.gauge

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun GaugePreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Gauge(10, 0, 20)
        }
    }
}

@Composable
fun Gauge(value: Int, minValue: Int, maxValue: Int, onClick: () -> Unit = {}) {
    val normalizedValue = (value.toFloat() - minValue) / (maxValue - minValue)

    val interpolatedValue by animateFloatAsState(
        targetValue = normalizedValue.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500, easing = EaseOut),
        label = ""
    )

    val animationState = createGaugeAnimationState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
            .offset(y = 50.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            GaugeIndicator(
                interpolatedValue,
                animationState,
                onClick
            )
            GaugeTempoValue(value, animationState)
        }
    }
}

@Composable
private fun GaugeTempoValue(value: Int, animationState: GaugeAnimationState) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
            .alpha(animationState.tempoValue),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "syllables per second",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun GaugeIndicator(
    value: Float,
    animationState: GaugeAnimationState,
    onClick: () -> Unit
) {
    val arcBackgroundBitmap = ImageBitmap.imageResource(id = R.drawable.gauge_arc_texture)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
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
    val tickAngle = (settings.arcAngle + settings.tickAngle * 2) / settings.tickCount
    for (i in 0..(settings.tickCount * animationValue).toInt()) {
        rotate(degrees = 90f - settings.tickAngle + i * tickAngle) {
            drawLine(
                color = settings.tickColor,
                start = Offset(
                    settings.size.width / 2,
                    settings.arcWidth + settings.strokeWidth / 2 - 1f
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