package com.persoff68.speechratemonitor.ui.settings

import android.app.Activity
import android.media.audiofx.NoiseSuppressor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.settings.IndicatorType
import com.persoff68.speechratemonitor.settings.SettingsViewModel
import com.persoff68.speechratemonitor.settings.ThemeMode
import com.persoff68.speechratemonitor.ui.shared.iconbutton.IconButton
import com.persoff68.speechratemonitor.ui.shared.labeleddropdown.LabeledDropdown
import com.persoff68.speechratemonitor.ui.shared.labeledslider.LabeledSlider
import com.persoff68.speechratemonitor.ui.shared.labeledswitch.LabeledSwitch
import com.persoff68.speechratemonitor.ui.shared.util.SetStatusBarTheme
import com.persoff68.speechratemonitor.ui.theme.LocalBrushes

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    audioState: AudioState
) {
    val context = LocalContext.current as Activity
    val brushes = LocalBrushes.current
    val backgroundBrush = brushes.backgroundGradientBrush()

    var showInfoDialog by remember { mutableStateOf(false) }

    fun goBack() {
        context.finish()
    }

    SetStatusBarTheme()
    SettingsInfoDialog(show = showInfoDialog, close = { showInfoDialog = false })
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_back),
                primaryColor = MaterialTheme.colorScheme.onSurface,
                onClick = { goBack() }
            )

            SettingsTitle(modifier = Modifier.padding(start = 30.dp))

            Spacer(Modifier.weight(1f))

            IconButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_info),
                primaryColor = MaterialTheme.colorScheme.onSurface,
                onClick = { showInfoDialog = true }
            )
        }

        SettingsInputs(audioState)
    }
}

@Composable
fun SettingsTitle(modifier: Modifier = Modifier) {
    val brushes = LocalBrushes.current
    val labelBrush = brushes.labelGradientBrush()

    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.displayLarge.copy(brush = labelBrush),
            text = stringResource(R.string.settings_activity_title)
        )
    }
}

@Composable
private fun SettingsInputs(
    audioState: AudioState,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val settings by settingsViewModel.settings.collectAsState()

    val isRecording by audioState.isRecordingState.collectAsState(initial = false)
    val isNoiseSuppressionAvailable = NoiseSuppressor.isAvailable()


    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {

        Card(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LabeledSlider(
                    label = stringResource(R.string.settings_max_syllables),
                    value = settings.maxSyllables.toFloat(),
                    onValueChange = { settingsViewModel.updateMaxSyllables(it.toInt()) },
                    valueRange = Config.BOTTOM_MAX_SYLLABLES_VALUE..Config.TOP_MAX_SYLLABLES_VALUE,
                    unit = stringResource(R.string.settings_syllables_per_second),
                    isEnabled = !isRecording
                )

                LabeledSlider(
                    label = stringResource(R.string.settings_warning_threshold),
                    value = settings.warningThreshold.toFloat(),
                    onValueChange = { settingsViewModel.updateWarningThreshold(it.toInt()) },
                    valueRange = Config.BOTTOM_WARNING_THRESHOLD_VALUE..Config.TOP_WARNING_THRESHOLD_VALUE,
                    unit = stringResource(R.string.settings_syllables_per_second),
                    isEnabled = !isRecording
                )

                LabeledSlider(
                    label = stringResource(R.string.settings_auto_stop_timeout),
                    value = settings.autoStopTimer.toFloat(),
                    onValueChange = { settingsViewModel.updateAutoStopTimer(it.toInt()) },
                    valueRange = Config.BOTTOM_AUTO_STOP_TIMEOUT..Config.TOP_AUTO_STOP_TIMEOUT,
                    isEnabled = !isRecording,
                    valueFormatter = {
                        if (it == 0f) stringResource(R.string.settings_auto_stop_timeout_off)
                        else it.toInt().toString()
                    },
                    unitFormatter = {
                        if (it == 0f) ""
                        else stringResource(R.string.settings_minutes)
                    }
                )

                LabeledSwitch(
                    label = stringResource(R.string.settings_sound_notification),
                    value = settings.soundNotification,
                    onValueChange = { settingsViewModel.updateSoundNotification(it) },
                    isEnabled = !isRecording
                )

                LabeledSwitch(
                    label = stringResource(R.string.settings_noise_suppression),
                    value = settings.noiseSuppression,
                    onValueChange = { settingsViewModel.updateNoiseSuppression(it) },
                    isEnabled = !isRecording && isNoiseSuppressionAvailable
                )

            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LabeledDropdown(
                    label = stringResource(R.string.settings_default_indicator),
                    value = settings.defaultIndicator.toString(),
                    values = IndicatorType.entries.map { it.toString() },
                    onValueChange = {
                        settingsViewModel.updateDefaultIndicator(
                            IndicatorType.valueOf(it)
                        )
                    },
                    valueFormatter = { it },
                    isEnabled = !isRecording
                )

                LabeledDropdown(
                    label = stringResource(R.string.settings_theme),
                    value = settings.theme.toString(),
                    values = ThemeMode.entries.map { it.toString() },
                    onValueChange = { settingsViewModel.updateTheme(ThemeMode.valueOf(it)) },
                    valueFormatter = { it },
                    isEnabled = !isRecording
                )
            }
        }
    }
}