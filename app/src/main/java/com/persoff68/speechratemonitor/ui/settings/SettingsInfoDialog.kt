package com.persoff68.speechratemonitor.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun SettingsInfoDialogPreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            SettingsInfoDialog(true) { }
        }
    }
}

@Composable
fun SettingsInfoDialog(show: Boolean, close: () -> Unit) {
    InfoDialog(
        show = show,
        close = { close() },
        title = stringResource(R.string.settings_dialog_title),
        closeDescription = stringResource(R.string.settings_close_info_dialog_button_label)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_max_syllables)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_max_syllables_description)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_warning_threshold)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_warning_threshold_description)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_auto_stop_timeout)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_auto_stop_timeout_description)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_sound_notification)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_sound_notification_description)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_noise_suppression)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_noise_suppression_description)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_default_indicator)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_default_indicator_description)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.settings_theme)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.settings_theme_description)
            )
        }
    }
}