package com.persoff68.speechratemonitor.ui.shared.infodialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.shared.iconbutton.IconButton
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun InfoDialogPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            InfoDialog(
                show = true,
                close = {},
                title = "Dialog title"
            ) {
                Text(text = "Dialog content")
            }
        }
    }
}

@Composable
fun InfoDialog(
    show: Boolean,
    close: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    if (show) {
        Dialog(onDismissRequest = close) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            icon = ImageVector.vectorResource(id = R.drawable.ic_close),
                            size = 20.dp,
                            primaryColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { close() }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 5.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(8.dp),

                        ) {
                        content()
                    }
                }
            }
        }
    }
}