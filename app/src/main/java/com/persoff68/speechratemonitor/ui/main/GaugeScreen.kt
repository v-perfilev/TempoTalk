package com.persoff68.speechratemonitor.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.audio.AudioModule
import com.persoff68.speechratemonitor.audio.manager.PermissionManager
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.ui.shared.gauge.Gauge
import com.persoff68.speechratemonitor.ui.shared.roundbutton.RoundButton
import com.persoff68.speechratemonitor.ui.shared.spectrogram.Spectrogram
import com.persoff68.speechratemonitor.ui.shared.waveform.Waveform
import com.persoff68.speechratemonitor.ui.theme.DarkGradient

@Composable
fun GaugeScreen(
    modifier: Modifier = Modifier,
    audioState: AudioState,
    audioModule: AudioModule,
    permissionManager: PermissionManager
) {
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
        Gauge(
            value = tempo,
            minValue = Config.MIN_GAUGE_VALUE,
            maxValue = Config.MAX_GAUGE_VALUE,
        )
        Box(Modifier)
        Waveform(buffer)
        Box(Modifier)
        Spectrogram(spectrogram, isRecording)
        RoundButton(
            primaryColor = Color(0xFF295E2B),
            primaryIcon = Icons.Default.Home,
            pressedColor = Color(0xFF701A1A),
            pressedIcon = Icons.Default.Clear,
            isPressed = isRecording,
            onClick = {
                if (!isRecording) {
                    permissionManager.checkAndRequestPermissions({ audioModule.start() })
                } else {
                    audioModule.stop()
                }
            }
        )
    }
}