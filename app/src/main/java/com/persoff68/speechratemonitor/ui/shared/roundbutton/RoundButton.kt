package com.persoff68.speechratemonitor.ui.shared.roundbutton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun GaugePreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            RoundButton(
                primaryColor = Color(0xFF29672B),
                primaryIcon = Icons.Default.Check,
                pressedColor = Color(0xFF621C1C),
                pressedIcon = Icons.Default.Close,
                isPressed = true
            ) { }
        }
    }
}

@Composable
fun RoundButton(
    primaryColor: Color,
    pressedColor: Color = primaryColor,
    primaryIcon: ImageVector,
    pressedIcon: ImageVector = primaryIcon,
    description: String = "",
    isPressed: Boolean = false,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var showRipple by remember { mutableStateOf(false) }
    var showPressed by remember { mutableStateOf(false) }

    SideEffect {
        coroutineScope.launch {
            if (isPressed != showPressed) {
                if (showRipple) delay(600)
                showPressed = isPressed
            }
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (showPressed) pressedColor else primaryColor,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (showPressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val settings = RoundButtonSettings()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(settings.rippleSize.dp)
            .clickable {
                showRipple = true
                onClick()
            }
    ) {
        if (showRipple) {
            RippleEffect(backgroundColor, settings) {
                showRipple = false
            }
        }

        Box(
            modifier = Modifier
                .size(settings.buttonSize.dp)
                .clip(CircleShape)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .background(backgroundColor, shape = CircleShape)
                .clickable {
                    if (!showRipple) {
                        showRipple = true
                        onClick()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (showPressed) pressedIcon else primaryIcon,
                contentDescription = description,
                tint = settings.iconColor,
                modifier = Modifier.size(settings.iconSize.dp)
            )
        }
    }
}

@Composable
private fun RippleEffect(color: Color, settings: RoundButtonSettings, onAnimationEnd: () -> Unit) {
    val rippleRadius1 = remember { Animatable(settings.buttonSize * 0.9f) }
    val rippleAlpha1 = remember { Animatable(0.3f) }

    val rippleRadius2 = remember { Animatable(settings.buttonSize * 0.9f) }
    val rippleAlpha2 = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        val rippleRadius1Launch = launch {
            rippleRadius1.animateTo(
                targetValue = settings.rippleSize,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
            rippleAlpha1.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )
        }

        val rippleRadius2Launch = launch {
            delay(200)
            rippleRadius2.animateTo(
                targetValue = settings.rippleSize,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )
            rippleAlpha2.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )

        }

        rippleRadius1Launch.join()
        rippleRadius2Launch.join()
        onAnimationEnd()
    }

    Canvas(modifier = Modifier.size(settings.rippleSize.dp)) {
        drawCircle(
            color = color.copy(alpha = rippleAlpha1.value),
            radius = rippleRadius1.value,
        )
        drawCircle(
            color = color.copy(alpha = rippleAlpha2.value),
            radius = rippleRadius2.value,
        )
    }
}