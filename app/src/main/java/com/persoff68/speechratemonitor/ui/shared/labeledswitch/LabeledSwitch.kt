package com.persoff68.speechratemonitor.ui.shared.labeledswitch

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun LabeledSwitchPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            LabeledSwitch(
                label = "Slider label",
                value = true,
                onValueChange = {},
            )
        }
    }
}

@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    isEnabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 15.dp),
            text = "$label:",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Spacer(Modifier.weight(1f))

        Switch(
            checked = value,
            onCheckedChange = { onValueChange(it) },
            enabled = isEnabled,
            colors = SwitchDefaults.colors(
                uncheckedBorderColor = MaterialTheme.colorScheme.onBackground,
                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}