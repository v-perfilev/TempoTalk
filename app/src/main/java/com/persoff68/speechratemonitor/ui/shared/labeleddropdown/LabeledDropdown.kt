package com.persoff68.speechratemonitor.ui.shared.labeleddropdown

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.shared.util.NoRippleInteractionSource
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun LabeledSwitchPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            LabeledDropdown(
                label = "Dropdown label",
                value = "Value 1",
                values = listOf("Value 1", "Value 2"),
                valueFormatter = { "Value 1" },
                onValueChange = {},
                description = "Preview"
            )
        }
    }
}

@Composable
fun LabeledDropdown(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    values: List<String>,
    onValueChange: (String) -> Unit,
    valueFormatter: (String) -> String,
    isEnabled: Boolean = true,
    description: String
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 15.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            text = "$label:"
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier.semantics { contentDescription = description }
        ) {
            CurrentValueBox(
                expanded = expanded,
                value = value,
                isEnabled = isEnabled,
                valueFormatter = valueFormatter
            ) { if (isEnabled) expanded = true }
            DropdownBox(
                expanded = expanded,
                values = values,
                valueFormatter = valueFormatter,
                onValueChange = onValueChange
            ) { expanded = false }
        }
    }
}

@Composable
private fun CurrentValueBox(
    expanded: Boolean,
    value: String,
    isEnabled: Boolean,
    valueFormatter: (String) -> String,
    openDropdownBox: () -> Unit
) {
    val iconDegree by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "Dropdown icon rotation animation"
    )

    Row(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
            .clickable(
                NoRippleInteractionSource(), null
            ) { openDropdownBox() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 10.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (expanded || !isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onBackground,
            text = valueFormatter(value)
        )
        Icon(
            modifier = Modifier
                .size(15.dp)
                .rotate(iconDegree),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
            contentDescription = "",
            tint = if (expanded || !isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun DropdownBox(
    expanded: Boolean,
    values: List<String>,
    valueFormatter: (String) -> String,
    onValueChange: (String) -> Unit,
    closeDropdownBox: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            ),
        expanded = expanded,
        onDismissRequest = { closeDropdownBox() },
    ) {
        values.forEach { v ->
            DropdownMenuItem(
                onClick = {
                    onValueChange(v)
                    closeDropdownBox()
                },
                text = {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = valueFormatter(v)
                    )
                }
            )
        }
    }
}