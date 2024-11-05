package com.persoff68.speechratemonitor.ui.shared.iconbutton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.ui.shared.modifier.roundShadow
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun IconButtonPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            IconButton(
                icon = Icons.Default.Check,
                size = 40.dp,
                primaryColor = Color.White
            ) { }
        }
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    size: Dp,
    icon: ImageVector,
    primaryColor: Color,
    pressedColor: Color = primaryColor,
    description: String = "",
    isPressed: Boolean = false,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isPressed) pressedColor else primaryColor,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Button(
        modifier = modifier
            .roundShadow(color = color.copy(alpha = 0.3f), blurRadius = size)
            .size(size),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = color,
            modifier = Modifier.size(size)
        )
    }
}