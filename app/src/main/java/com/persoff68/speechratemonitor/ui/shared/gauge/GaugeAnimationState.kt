package com.persoff68.speechratemonitor.ui.shared.gauge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GaugeAnimationState(
    val scale: Float = 1f,
    val arc: Float = 1f,
    val ticks: Float = 1f,
    val needle: Float = 1f,
    val tempoValue: Float = 1f,
)

@Composable
fun createGaugeAnimationState(): GaugeAnimationState {
    val isInPreview = LocalInspectionMode.current

    if (isInPreview) {
        return GaugeAnimationState(
            scale = 1f,
            arc = 1f,
            ticks = 1f,
            needle = 1f,
            tempoValue = 1f,
        )
    }

    val scaleAnimationParameter = remember { Animatable(0f) }
    val arcAnimationParameter = remember { Animatable(0f) }
    val ticksAnimationParameter = remember { Animatable(-1f) }
    val needleAnimationParameter = remember { Animatable(0f) }
    val tempoValueAnimationParameter = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            val scaleAnimation = launch {
                delay(200)
                scaleAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = EaseOut)
                )
            }
            val arcAnimation = launch {
                delay(200)
                arcAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = Ease)
                )
            }
            val ticksAnimation = launch {
                delay(500)
                ticksAnimationParameter.animateTo(0f)
                ticksAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = LinearEasing)
                )
            }
            val needleAnimation = launch {
                delay(1000)
                needleAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = EaseInOut)
                )
            }
            val tempoValueAnimation = launch {
                delay(1500)
                tempoValueAnimationParameter.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }

            scaleAnimation.join()
            arcAnimation.join()
            ticksAnimation.join()
            needleAnimation.join()
            tempoValueAnimation.join()
        }
    }

    return GaugeAnimationState(
        scale = scaleAnimationParameter.value,
        arc = arcAnimationParameter.value,
        ticks = ticksAnimationParameter.value,
        needle = needleAnimationParameter.value,
        tempoValue = tempoValueAnimationParameter.value
    )
}