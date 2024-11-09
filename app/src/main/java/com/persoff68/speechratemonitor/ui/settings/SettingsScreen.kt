package com.persoff68.speechratemonitor.ui.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.settings.IndicatorType
import com.persoff68.speechratemonitor.settings.SettingsViewModel
import com.persoff68.speechratemonitor.settings.ThemeMode
import com.persoff68.speechratemonitor.ui.shared.iconbutton.IconButton
import com.persoff68.speechratemonitor.ui.shared.labeleddropdown.LabeledDropdown
import com.persoff68.speechratemonitor.ui.shared.labeledslider.LabeledSlider
import com.persoff68.speechratemonitor.ui.shared.labeledswitch.LabeledSwitch
import com.persoff68.speechratemonitor.ui.shared.settingstitle.SettingsTitle
import com.persoff68.speechratemonitor.ui.shared.util.SetStatusBarTheme
import com.persoff68.speechratemonitor.ui.theme.backgroundGradientBrush

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current as Activity
    SetStatusBarTheme(Color.Transparent, isLightTheme = false)

    val backgroundBrush = backgroundGradientBrush()

    fun goBack() {
        context.finish()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_back),
                size = 25.dp,
                primaryColor = MaterialTheme.colorScheme.onBackground,
                onClick = { goBack() }
            )

            SettingsTitle(modifier = Modifier.padding(start = 30.dp))
        }

        SettingsInputs()
    }
}

@Composable
private fun SettingsInputs(viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier.padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Card(
            Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.height(10.dp))

            LabeledSlider(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_max_syllables),
                value = settings.maxSyllables.toFloat(),
                onValueChange = { viewModel.updateMaxSyllables(it.toInt()) },
                valueRange = 5f..10f,
                unit = stringResource(R.string.settings_syllables_per_second)
            )

            LabeledSlider(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_warning_threshold),
                value = settings.warningThreshold.toFloat(),
                onValueChange = { viewModel.updateWarningThreshold(it.toInt()) },
                valueRange = 3f..10f,
                unit = stringResource(R.string.settings_syllables_per_second)
            )

            LabeledSlider(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_auto_stop_timer),
                value = settings.autoStopTimer.toFloat(),
                onValueChange = { viewModel.updateAutoStopTimer(it.toInt()) },
                valueRange = 0f..30f,
                unit = stringResource(R.string.settings_minutes)
            )

            LabeledSwitch(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_sound_notification),
                value = settings.soundNotification,
                onValueChange = { viewModel.updateSoundNotification(it) },
            )

            LabeledSwitch(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_noise_suppression),
                value = settings.noiseSuppression,
                onValueChange = { viewModel.updateNoiseSuppression(it) },
            )

            Spacer(Modifier.height(10.dp))
        }

        Card(
            Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.height(10.dp))

            LabeledDropdown(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_default_indicator),
                value = settings.defaultIndicator.toString(),
                values = IndicatorType.entries.map { it.toString() },
                onValueChange = { viewModel.updateDefaultIndicator(IndicatorType.valueOf(it)) },
                valueFormatter = { it }
            )

            LabeledDropdown(
                modifier = Modifier.padding(15.dp),
                label = stringResource(R.string.settings_theme),
                value = settings.theme.toString(),
                values = ThemeMode.entries.map { it.toString() },
                onValueChange = { viewModel.updateTheme(ThemeMode.valueOf(it)) },
                valueFormatter = { it }
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}