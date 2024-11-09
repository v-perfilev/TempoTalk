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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.persoff68.speechratemonitor.R
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
    isEnabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 15.dp),
            text = "$label:",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Spacer(Modifier.weight(1f))

        Box {
            CurrentValueBox(
                expanded = expanded,
                value = value,
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
    valueFormatter: (String) -> String,
    openDropdownBox: () -> Unit
) {
    val iconDegree by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Row(
        modifier = Modifier
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { openDropdownBox() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 15.dp),
            text = valueFormatter(value),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
        Icon(
            modifier = Modifier
                .size(15.dp)
                .rotate(iconDegree),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
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
                1.dp,
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
                text = { Text(text = valueFormatter(v)) }
            )
        }
    }
}