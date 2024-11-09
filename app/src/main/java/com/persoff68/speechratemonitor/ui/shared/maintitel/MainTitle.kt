package com.persoff68.speechratemonitor.ui.shared.maintitel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import com.persoff68.speechratemonitor.ui.theme.labelGradientBrush

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun MainTitlePreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            MainTitle()
        }
    }
}

@Composable
fun MainTitle(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.main_activity_title)
    val scale = remember { Animatable(0f) }
    val labelBrush = labelGradientBrush()

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
            text = title,
            style = TextStyle(
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                brush = labelBrush,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.onSurface,
                    offset = Offset(5f, 0f),
                    blurRadius = 30f,
                )
            )
        )
    }
}