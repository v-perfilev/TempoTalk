package com.persoff68.speechratemonitor.ui.shared.roundbutton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.ui.shared.modifier.roundShadow
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun RoundButtonPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            RoundButton(
                primaryColor = Color(0xFF29672B),
                primaryIcon = Icons.Default.Check,
                buttonSize = 40.dp
            ) { }
        }
    }
}

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    buttonSize: Dp,
    primaryColor: Color,
    primaryIcon: ImageVector,
    pressedColor: Color = primaryColor,
    pressedIcon: ImageVector = primaryIcon,
    rippleSize: Dp = buttonSize * 1.5f,
    iconSize: Dp = buttonSize * 0.5f,
    description: String = "",
    isPressed: Boolean = false,
    onClick: () -> Unit
) {
    var showRipple by remember { mutableStateOf(false) }
    var showPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed != showPressed) {
            if (showRipple) delay(500)
            showPressed = isPressed
        }
    }

    val color by animateColorAsState(
        targetValue = if (showPressed) pressedColor else primaryColor,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Box(
        modifier = modifier.size(rippleSize),
        contentAlignment = Alignment.Center

    ) {
        if (showRipple) {
            RippleEffect(color, buttonSize, rippleSize) {
                showRipple = false
            }
        }

        Button(
            modifier = Modifier
                .roundShadow(color = Color.White.copy(alpha = 0.3f), blurRadius = buttonSize / 4)
                .size(buttonSize)
                .background(color, shape = RoundedCornerShape(100))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.15f)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ), shape = RoundedCornerShape(100)
                ),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(100),
            onClick = {
                showRipple = true
                onClick()
            }
        ) {
            Icon(
                imageVector = if (showPressed) pressedIcon else primaryIcon,
                contentDescription = description,
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
private fun RippleEffect(
    color: Color,
    buttonSize: Dp,
    rippleSize: Dp,
    onAnimationEnd: () -> Unit
) {
    val buttonRadius = with(LocalDensity.current) { buttonSize.toPx() / 2 }
    val rippleRadius = with(LocalDensity.current) { rippleSize.toPx() / 2 }

    val rippleRadius1 = remember { Animatable(buttonRadius) }
    val rippleAlpha1 = remember { Animatable(0.5f) }

    val rippleRadius2 = remember { Animatable(buttonRadius) }
    val rippleAlpha2 = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        val rippleRadius1Launch = launch {
            rippleRadius1.animateTo(
                targetValue = rippleRadius,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )
            rippleAlpha1.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )
        }

        val rippleRadius2Launch = launch {
            delay(200)
            rippleRadius2.animateTo(
                targetValue = rippleRadius,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
            rippleAlpha2.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )

        }

        rippleRadius1Launch.join()
        rippleRadius2Launch.join()
        delay(200)
        onAnimationEnd()
    }

    Canvas(modifier = Modifier.size(rippleSize)) {
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