package com.persoff68.speechratemonitor.ui.main

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.audio.AudioModule
import com.persoff68.speechratemonitor.audio.manager.PermissionManager
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.ui.settings.SettingsActivity
import com.persoff68.speechratemonitor.ui.shared.gauge.Gauge
import com.persoff68.speechratemonitor.ui.shared.iconbutton.IconButton
import com.persoff68.speechratemonitor.ui.shared.indicatorbackground.IndicatorBackground
import com.persoff68.speechratemonitor.ui.shared.indicatorsubtitle.IndicatorSubtitle
import com.persoff68.speechratemonitor.ui.shared.maintitel.MainTitle
import com.persoff68.speechratemonitor.ui.shared.roundbutton.RoundButton
import com.persoff68.speechratemonitor.ui.shared.spectrogram.Spectrogram
import com.persoff68.speechratemonitor.ui.shared.util.SetStatusBarTheme
import com.persoff68.speechratemonitor.ui.shared.waveform.Waveform
import com.persoff68.speechratemonitor.ui.theme.backgroundGradientBrush

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    audioState: AudioState,
    audioModule: AudioModule,
    permissionManager: PermissionManager,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current as Activity
    SetStatusBarTheme(Color.Transparent, isLightTheme = false)

    val showWaveform by viewModel.showWaveform.observeAsState()
    val isRecording by audioState.isRecordingState.collectAsState(initial = false)
    val tempo by audioState.tempoState.collectAsState(initial = Config.DEFAULT_TEMPO)
    val buffer by audioState.bufferState.collectAsState(initial = Config.DEFAULT_BUFFER)
    val spectrogram by audioState.spectrogramState.collectAsState(initial = Config.DEFAULT_SPECTROGRAM)
    val backgroundBrush = backgroundGradientBrush()

    val animationState = createMainScreenAnimationState()

    fun goToSettings() {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(
                modifier = Modifier.alpha(animationState.parameter),
                icon = ImageVector.vectorResource(id = R.drawable.ic_info),
                size = 25.dp,
                primaryColor = MaterialTheme.colorScheme.onBackground,
                onClick = {}
            )

            MainTitle()

            IconButton(
                modifier = Modifier.alpha(animationState.parameter),
                icon = ImageVector.vectorResource(id = R.drawable.ic_cog),
                size = 25.dp,
                primaryColor = MaterialTheme.colorScheme.onBackground,
                onClick = { goToSettings() }
            )
        }

        Gauge(
            value = tempo,
            minValue = Config.MIN_GAUGE_VALUE,
            maxValue = Config.MAX_GAUGE_VALUE,
        )

        Spacer(Modifier)

        MainScreenIndicator(
            modifier = Modifier.alpha(animationState.parameter),
            buffer = buffer,
            spectrogram = spectrogram,
            showWaveform = showWaveform!!,
            isRecording = isRecording
        )

        Spacer(Modifier)

        MainScreenButtons(
            modifier = Modifier.alpha(animationState.parameter),
            audioModule = audioModule,
            permissionManager = permissionManager,
            showWaveform = showWaveform!!,
            isRecording = isRecording
        ) { viewModel.toggleIndicator() }
    }
}

@Composable
private fun MainScreenIndicator(
    modifier: Modifier = Modifier,
    buffer: FloatArray,
    spectrogram: Array<FloatArray>,
    showWaveform: Boolean,
    isRecording: Boolean
) {
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
        ) {
            IndicatorBackground()
            if (showWaveform) {
                Waveform(audioData = buffer)
            } else {
                Spectrogram(spectrogramData = spectrogram, isRecording = isRecording)
            }

        }
        Box(
            Modifier.padding(top = 15.dp)
        ) {
            IndicatorSubtitle(showWaveform)
        }
    }
}

@Composable
private fun MainScreenButtons(
    modifier: Modifier = Modifier,
    audioModule: AudioModule,
    permissionManager: PermissionManager,
    showWaveform: Boolean,
    isRecording: Boolean,
    toggleIndicator: () -> Unit
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically

    ) {
        IconButton(
            icon = ImageVector.vectorResource(id = R.drawable.ic_waveform),
            size = 40.dp,
            primaryColor = MaterialTheme.colorScheme.onSurfaceVariant,
            pressedColor = MaterialTheme.colorScheme.onSurface,
            isPressed = showWaveform,
            onClick = {
                if (!showWaveform) {
                    toggleIndicator()
                }
            }
        )

        RoundButton(
            primaryColor = MaterialTheme.colorScheme.primary,
            primaryIcon = ImageVector.vectorResource(id = R.drawable.ic_microphone),
            pressedColor = MaterialTheme.colorScheme.secondary,
            pressedIcon = ImageVector.vectorResource(id = R.drawable.ic_stop),
            iconColor = MaterialTheme.colorScheme.onPrimary,
            isPressed = isRecording,
            buttonSize = 80.dp,
            onClick = {
                if (!isRecording) {
                    permissionManager.checkAndRequestPermissions({ audioModule.start() })
                } else {
                    audioModule.stop()
                }
            }
        )

        IconButton(
            icon = ImageVector.vectorResource(id = R.drawable.ic_spectrogram),
            size = 40.dp,
            primaryColor = MaterialTheme.colorScheme.onSurfaceVariant,
            pressedColor = MaterialTheme.colorScheme.onSurface,
            isPressed = !showWaveform,
            onClick = {
                if (showWaveform) {
                    toggleIndicator()
                }
            }
        )
    }
}
