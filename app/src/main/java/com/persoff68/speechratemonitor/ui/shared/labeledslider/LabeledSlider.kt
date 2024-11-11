package com.persoff68.speechratemonitor.ui.shared.labeledslider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun LabeledSliderPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            LabeledSlider(
                label = "Slider label",
                value = 15f,
                onValueChange = {},
                valueRange = 1f..20f,
                unit = "min."
            )
        }
    }
}

@Composable
fun LabeledSlider(
    modifier: Modifier = Modifier,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    isEnabled: Boolean = true,
    unit: String? = null,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 15.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                text = "${label}:"
            )
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = value.toInt().toString()
            )
            unit?.let {
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    text = unit
                )
            }
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            enabled = isEnabled,
            steps = steps,
            colors = SliderDefaults.colors(
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface,
            )
        )

    }
}