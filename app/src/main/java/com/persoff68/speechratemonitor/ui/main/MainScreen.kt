package com.persoff68.speechratemonitor.ui.main

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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.audio.AudioModule
import com.persoff68.speechratemonitor.audio.manager.PermissionManager
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.ui.shared.gauge.Gauge
import com.persoff68.speechratemonitor.ui.shared.iconbutton.IconButton
import com.persoff68.speechratemonitor.ui.shared.indicatorbackground.IndicatorBackground
import com.persoff68.speechratemonitor.ui.shared.label.Label
import com.persoff68.speechratemonitor.ui.shared.roundbutton.RoundButton
import com.persoff68.speechratemonitor.ui.shared.spectrogram.Spectrogram
import com.persoff68.speechratemonitor.ui.shared.waveform.Waveform
import com.persoff68.speechratemonitor.ui.theme.DarkGradient

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    audioState: AudioState,
    audioModule: AudioModule,
    permissionManager: PermissionManager,
    viewModel: MainViewModel = hiltViewModel()
) {
    val showWaveform by viewModel.showWaveform.observeAsState()
    val isRecording by audioState.isRecordingState.collectAsState(initial = false)
    val tempo by audioState.tempoState.collectAsState(initial = Config.DEFAULT_TEMPO)
    val buffer by audioState.bufferState.collectAsState(initial = Config.DEFAULT_BUFFER)
    val spectrogram by audioState.spectrogramState.collectAsState(initial = Config.DEFAULT_SPECTROGRAM)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkGradient),
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
            Label()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_info),
                    size = 25.dp,
                    primaryColor = Color.LightGray,
                    onClick = {}
                )
                Spacer(Modifier.width(25.dp))
                IconButton(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_cog),
                    size = 25.dp,
                    primaryColor = Color.LightGray,
                    onClick = {}
                )
            }
        }


        Gauge(
            value = tempo,
            minValue = Config.MIN_GAUGE_VALUE,
            maxValue = Config.MAX_GAUGE_VALUE,
        )

        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
        ) {
            IndicatorBackground()
            if (showWaveform != false) {
                Waveform(audioData = buffer)
            } else {
                Spectrogram(spectrogramData = spectrogram, isRecording = isRecording)
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_waveform),
                size = 40.dp,
                primaryColor = Color.Gray,
                pressedColor = Color.White,
                isPressed = showWaveform == true,
                onClick = {
                    if (showWaveform != true) {
                        viewModel.toggleIndicator()
                    }
                }
            )

            RoundButton(
                primaryColor = Color(0xFF295E2B),
                primaryIcon = ImageVector.vectorResource(id = R.drawable.ic_microphone),
                pressedColor = Color(0xFF701A1A),
                pressedIcon = ImageVector.vectorResource(id = R.drawable.ic_stop),
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
                primaryColor = Color.Gray,
                pressedColor = Color.White,
                isPressed = showWaveform == false,
                onClick = {
                    if (showWaveform != false) {
                        viewModel.toggleIndicator()
                    }
                }
            )
        }
    }
}

