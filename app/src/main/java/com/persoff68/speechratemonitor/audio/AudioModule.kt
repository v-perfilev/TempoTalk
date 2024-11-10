package com.persoff68.speechratemonitor.audio

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.persoff68.speechratemonitor.audio.service.AudioService
import com.persoff68.speechratemonitor.audio.state.AudioState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioModule @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioState: AudioState,
) {
    private var audioService: AudioService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? AudioService.LocalBinder
            audioService = localBinder?.getService()
            audioService?.startRecording()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
        }
    }

    fun start() {
        if (audioService == null) {
            val intent = Intent(context, AudioService::class.java)
            ContextCompat.startForegroundService(context, intent)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            if (!audioState.isRecordingState.value) {
                audioService?.startRecording()
            }
        }
    }

    fun stop() {
        if (audioState.isRecordingState.value) {
            audioService?.stopRecording()
        }
    }
}
