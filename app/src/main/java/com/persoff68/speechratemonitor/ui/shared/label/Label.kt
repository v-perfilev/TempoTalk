package com.persoff68.speechratemonitor.ui.shared.label

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Label(modifier: Modifier = Modifier) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .alpha(scale.value)
                .align(Alignment.CenterStart),
            text = "SpeechRateMonitor",
            style = TextStyle(
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Green, Color.Yellow, Color.Red)
                ),
                shadow = Shadow(
                    color = Color.White,
                    offset = Offset(5f, 0f),
                    blurRadius = 30f,

                    )
            )
        )
    }
}