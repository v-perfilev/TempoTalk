package com.persoff68.speechratemonitor.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.ui.shared.infodialog.InfoDialog
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun MainInfoDialogPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            MainInfoDialog(true) { }
        }
    }
}

@Composable
fun MainInfoDialog(show: Boolean, close: () -> Unit) {
    InfoDialog(
        show = show,
        close = { close() },
        title = stringResource(R.string.settings_information)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.main_dialog_title)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.main_dialog_content_1)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.main_dialog_content_2)
            )
        }

        Box(Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                text = stringResource(R.string.main_warning_title)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.main_warning_description)
            )
        }
    }
}