package com.persoff68.speechratemonitor.ui.shared.iconbutton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.ui.shared.util.roundShadow
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun IconButtonPreview() {
    SpeechRateMonitorAppTheme {
        IconButton(
            icon = Icons.Default.Check,
            size = 40.dp,
            primaryColor = Color.Black
        ) { }
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    primaryColor: Color,
    pressedColor: Color = primaryColor,
    size: Dp = 20.dp,
    description: String = "",
    isPressed: Boolean = false,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isPressed) pressedColor else primaryColor,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val shadowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.3f else 0.1f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Button(
        modifier = modifier
            .size(size)
            .roundShadow(color = color.copy(alpha = shadowAlpha), blurRadius = size),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = icon,
            contentDescription = description,
            tint = color,
        )
    }
}