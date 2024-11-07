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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import com.persoff68.speechratemonitor.ui.theme.gaugeBackgroundGradientBrush
import com.persoff68.speechratemonitor.ui.theme.gaugeGradientBrush
import com.persoff68.speechratemonitor.ui.theme.needleGradientBrush
import com.persoff68.speechratemonitor.ui.theme.textureBrush

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun GaugePreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Gauge(value = 17, minValue = 0, maxValue = 20)
        }
    }
}

@Composable
fun Gauge(
    modifier: Modifier = Modifier,
    value: Int,
    minValue: Int,
    maxValue: Int,
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
            .fillMaxWidth()
            .scale(animationState.scale)
            .aspectRatio(1.4f)
            .offset(y = 15.dp),
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
    val mainTextColor = MaterialTheme.colorScheme.onSurface
    val subtitleTextColor = MaterialTheme.colorScheme.onBackground

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
            .alpha(animationState.tempoValue),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value",
            style = MaterialTheme.typography.displayMedium,
            color = mainTextColor,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.syllable_per_second),
            style = MaterialTheme.typography.headlineSmall,
            color = subtitleTextColor,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun GaugeIndicator(
    value: Float,
    animation: GaugeAnimationState,
    onClick: () -> Unit
) {
    var settings by remember { mutableStateOf(GaugeSettings()) }

    val startAngle = 90f + (360f - settings.arcAngle) / 2

    val textureBitmap = ImageBitmap.imageResource(id = R.drawable.display_texture)
    val textureBrush = textureBrush(bitmap = textureBitmap, sx = 0.8f, sy = 0.8f)
    val backgroundBrush = gaugeBackgroundGradientBrush()
    val gaugeBrush = gaugeGradientBrush(settings.size.center, settings.arcAngle)
    val needleBrush = needleGradientBrush(settings.size.center, settings.needleLength)

    val tickColor = MaterialTheme.colorScheme.onSurface
    val strokeColor = MaterialTheme.colorScheme.onSurface

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .onSizeChanged { settings = GaugeSettings(it) }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() }
                )
            }
    ) {
        rotate(startAngle) {
            drawGlowArc(settings, value, gaugeBrush)
            drawTicks(settings, animation.ticks, tickColor)
            drawStrokeArc(settings, animation.arc, strokeColor)
            drawTextureArc(settings, animation.arc, textureBrush)
            drawBackgroundArc(settings, animation.arc, backgroundBrush)
            drawValueArc(settings, value, gaugeBrush)
            drawNeedle(settings, animation.needle, value, needleBrush)
        }
    }
}

private fun DrawScope.drawTicks(settings: GaugeSettings, animation: Float, color: Color) {
    val tickAngle = (settings.arcAngle + settings.tickAngle * 2) / settings.tickCount
    for (i in 0..(settings.tickCount * animation).toInt()) {
        rotate(degrees = 90f - settings.tickAngle + i * tickAngle) {
            drawLine(
                color = color,
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

private fun DrawScope.drawGlowArc(settings: GaugeSettings, value: Float, brush: Brush) {
    for (i in 0..20) {
        drawArc(
            brush = brush,
            startAngle = 0f,
            sweepAngle = value * settings.arcAngle,
            useCenter = false,
            topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
            size = Size(size.width - settings.arcWidth, size.height - settings.arcWidth),
            style = Stroke(
                width = settings.arcWidth + (20 - i) * settings.glowParameter,
                cap = StrokeCap.Round
            ),
            alpha = i / 400f,
        )
    }
}

private fun DrawScope.drawStrokeArc(settings: GaugeSettings, animation: Float, color: Color) {
    drawArc(
        color = color,
        startAngle = -settings.strokeAngle * animation,
        sweepAngle = settings.arcAngle * animation + settings.strokeAngle * 2 * animation,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth + settings.strokeWidth, cap = StrokeCap.Butt),
        alpha = 0.6f
    )
}


private fun DrawScope.drawTextureArc(
    settings: GaugeSettings,
    animation: Float,
    textureBrush: Brush,
) {
    drawArc(
        brush = textureBrush,
        startAngle = 0f,
        sweepAngle = settings.arcAngle * animation,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.95f
    )
}

private fun DrawScope.drawBackgroundArc(
    settings: GaugeSettings,
    animation: Float,
    backgroundBrush: Brush
) {
    drawArc(
        brush = backgroundBrush,
        startAngle = 0f,
        sweepAngle = settings.arcAngle * animation,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.05f
    )
}

private fun DrawScope.drawValueArc(settings: GaugeSettings, value: Float, brush: Brush) {
    drawArc(
        brush = brush,
        startAngle = 0f,
        sweepAngle = value * settings.arcAngle,
        useCenter = false,
        topLeft = Offset(settings.arcWidth / 2, settings.arcWidth / 2),
        size = Size(
            settings.size.width - settings.arcWidth,
            settings.size.height - settings.arcWidth
        ),
        style = Stroke(width = settings.arcWidth, cap = StrokeCap.Butt),
        alpha = 0.9f
    )
}

private fun DrawScope.drawNeedle(
    settings: GaugeSettings,
    animation: Float,
    value: Float,
    brush: Brush
) {
    val needleAngle = value * settings.arcAngle

    rotate(degrees = needleAngle) {

        val path = Path().apply {
            moveTo(settings.size.center.x, center.y - settings.needleWidth / 2)
            lineTo(settings.size.center.x, center.y + settings.needleWidth / 2)
            lineTo(
                settings.size.center.x + settings.needleLength * animation - 20f * animation,
                center.y + settings.needleWidth / 6
            )
            lineTo(settings.size.center.x + settings.needleLength * animation, center.y)
            lineTo(
                settings.size.center.x + settings.needleLength * animation - 20f * animation,
                center.y - settings.needleWidth / 6
            )
            close()
        }

        drawPath(
            path = path,
            brush = brush,
        )
    }
}