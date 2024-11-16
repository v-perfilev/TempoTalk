package com.persoff68.speechratemonitor.ui.shared.gauge

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.LocalBrushes
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun GaugePreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Gauge(value = 20, minValue = 0, maxValue = 20)
        }
    }
}

@Composable
fun Gauge(
    modifier: Modifier = Modifier,
    value: Int,
    maxValue: Int,
    minValue: Int = 0,
    onClick: () -> Unit = {}
) {
    val normalizedValue = (value.toFloat() - minValue) / (maxValue - minValue)

    val interpolatedValue by animateFloatAsState(
        targetValue = normalizedValue.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700, easing = EaseOut),
        label = ""
    )

    val animationState = createGaugeAnimationState()

    Box(
        modifier = modifier
            .widthIn(max = 500.dp)
            .aspectRatio(1.4f)
            .scale(animationState.scale)
    ) {
        GaugeIndicator(
            value = interpolatedValue,
            animation = animationState
        ) { onClick() }
        GaugeTempoValue(value, animationState)
    }
}

@Composable
private fun GaugeTempoValue(value: Int, animationState: GaugeAnimationState) {
    Column(
        Modifier
            .fillMaxSize()
            .alpha(animationState.tempoValue),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = value.toString()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(R.string.syllables_per_second)
        )
    }
}

@Composable
private fun GaugeIndicator(
    value: Float,
    animation: GaugeAnimationState,
    onClick: () -> Unit
) {
    val brushes = LocalBrushes.current
    var params by remember { mutableStateOf(GaugeParams()) }

    val startAngle = 90f + (360f - params.arcAngle) / 2

    val textureBrush = brushes.textureBrush()
    val backgroundBrush = brushes.gaugeBackgroundGradientBrush()
    val gaugeBrush = brushes.gaugeGradientBrush(params.size.center, params.arcAngle)
    val needleBrush = brushes.needleGradientBrush(params.size.center, params.needleLength)

    val tickColor = MaterialTheme.colorScheme.onSurface
    val strokeColor = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .offset(y = (params.arcWidth / 3).dp)
        ) {
            Canvas(
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxSize()
                    .onSizeChanged { params = GaugeParams(it) }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onClick() }
                        )
                    }
            ) {
                rotate(startAngle) {
                    drawGlowArc(params, value, gaugeBrush)
                    drawTicks(params, animation.ticks, tickColor)
                    drawStrokeArc(params, animation.arc, strokeColor)
                    drawTextureArc(params, animation.arc, textureBrush)
                    drawBackgroundArc(params, animation.arc, backgroundBrush)
                    drawValueArc(params, value, gaugeBrush)
                    drawNeedle(params, animation.needle, value, needleBrush)
                }
            }
        }
    }
}

private fun DrawScope.drawTicks(params: GaugeParams, animation: Float, color: Color) {
    val tickAngle = (params.arcAngle + params.tickAngle * 2) / params.tickCount
    for (i in 0..(params.tickCount * animation).toInt()) {
        rotate(degrees = 90f - params.tickAngle + i * tickAngle) {
            drawLine(
                color = color,
                start = Offset(
                    params.size.width / 2,
                    params.arcWidth + params.strokeWidth / 2 - 1f
                ),
                end = Offset(
                    params.size.width / 2,
                    params.arcWidth + params.strokeWidth / 2 + params.tickLength
                ),
                strokeWidth = params.tickWidth,
                alpha = 0.6f
            )
        }
    }
}

private fun DrawScope.drawGlowArc(params: GaugeParams, value: Float, brush: Brush) {
    for (i in 0..20) {
        drawArc(
            brush = brush,
            startAngle = 0f,
            sweepAngle = value * params.arcAngle,
            useCenter = false,
            topLeft = Offset(params.arcWidth / 2, params.arcWidth / 2),
            size = Size(size.width - params.arcWidth, size.height - params.arcWidth),
            style = Stroke(
                width = params.arcWidth + (20 - i) * params.glowParameter,
                cap = StrokeCap.Round
            ),
            alpha = i / 400f
        )
    }
}

private fun DrawScope.drawStrokeArc(params: GaugeParams, animation: Float, color: Color) {
    drawArc(
        color = color,
        startAngle = -params.strokeAngle * animation,
        sweepAngle = params.arcAngle * animation + params.strokeAngle * 2 * animation,
        useCenter = false,
        topLeft = Offset(params.arcWidth / 2, params.arcWidth / 2),
        size = Size(
            params.size.width - params.arcWidth,
            params.size.height - params.arcWidth
        ),
        style = Stroke(width = params.arcWidth + params.strokeWidth, cap = StrokeCap.Butt),
        alpha = 0.6f
    )
}


private fun DrawScope.drawTextureArc(
    params: GaugeParams,
    animation: Float,
    brush: Brush,
) {
    drawArc(
        brush = brush,
        startAngle = 0f,
        sweepAngle = params.arcAngle * animation,
        useCenter = false,
        topLeft = Offset(params.arcWidth / 2, params.arcWidth / 2),
        size = Size(
            params.size.width - params.arcWidth,
            params.size.height - params.arcWidth
        ),
        style = Stroke(width = params.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.95f
    )
}

private fun DrawScope.drawBackgroundArc(
    params: GaugeParams,
    animation: Float,
    brush: Brush
) {
    drawArc(
        brush = brush,
        startAngle = 0f,
        sweepAngle = params.arcAngle * animation,
        useCenter = false,
        topLeft = Offset(params.arcWidth / 2, params.arcWidth / 2),
        size = Size(
            params.size.width - params.arcWidth,
            params.size.height - params.arcWidth
        ),
        style = Stroke(width = params.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.05f
    )
}

private fun DrawScope.drawValueArc(params: GaugeParams, value: Float, brush: Brush) {
    drawArc(
        brush = brush,
        startAngle = 0f,
        sweepAngle = value * params.arcAngle,
        useCenter = false,
        topLeft = Offset(params.arcWidth / 2, params.arcWidth / 2),
        size = Size(
            params.size.width - params.arcWidth,
            params.size.height - params.arcWidth
        ),
        style = Stroke(width = params.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.95f
    )
}

private fun DrawScope.drawNeedle(
    params: GaugeParams,
    animation: Float,
    value: Float,
    brush: Brush
) {
    val needleAngle = value * params.arcAngle

    rotate(degrees = needleAngle) {

        val path = Path().apply {
            moveTo(params.size.center.x, center.y - params.needleWidth / 2)
            lineTo(params.size.center.x, center.y + params.needleWidth / 2)
            lineTo(
                params.size.center.x + params.needleLength * animation - 20f * animation,
                center.y + params.needleWidth / 6
            )
            lineTo(params.size.center.x + params.needleLength * animation, center.y)
            lineTo(
                params.size.center.x + params.needleLength * animation - 20f * animation,
                center.y - params.needleWidth / 6
            )
            close()
        }

        drawPath(
            path = path,
            brush = brush,
        )
    }
}