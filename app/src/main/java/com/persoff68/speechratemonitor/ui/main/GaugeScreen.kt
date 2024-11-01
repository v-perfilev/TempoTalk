package com.persoff68.speechratemonitor.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.persoff68.speechratemonitor.ui.shared.gauge.Gauge
import com.persoff68.speechratemonitor.ui.shared.roundbutton.RoundButton
import com.persoff68.speechratemonitor.ui.theme.DarkGradient
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
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

@Composable
fun GaugeScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val tempo = remember { Animatable(0f) }
    val started = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkGradient),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(modifier = Modifier)
        Gauge(
            value = tempo.value.toInt(),
            minValue = 0,
            maxValue = 20,
        )
        Box(modifier = Modifier)
        RoundButton(
            primaryColor = Color(0xFF295E2B),
            primaryIcon = Icons.Default.Home,
            pressedColor = Color(0xFF701A1A),
            pressedIcon = Icons.Default.Clear,
            isPressed = started.value,
            onClick = {
                coroutineScope.launch {
                    started.value = true
                    startAnimation(tempo)
                    started.value = false

                }
            }
        )
        Box(modifier = Modifier)
    }
}

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>) {
    animation.animateTo(0f, keyframes {
        durationMillis = 5000
        0f at 0 using CubicBezierEasing(0f, 1.5f, 0.8f, 1f)
        12f at 1000 using CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        16f at 2000 using CubicBezierEasing(0.2f, -1.8f, 0f, 1f)
        14f at 4000 using LinearOutSlowInEasing
    })
}



