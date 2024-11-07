package com.persoff68.speechratemonitor.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainScreenAnimationState(
    val parameter: Float = 1f,
)

@Composable
fun createMainScreenAnimationState(): MainScreenAnimationState {
    val animationParameter = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            delay(1500)
            animationParameter.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = EaseOut)
            )
        }
    }

    return MainScreenAnimationState(
        parameter = animationParameter.value,
    )
}