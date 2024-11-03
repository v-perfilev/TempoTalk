package com.persoff68.speechratemonitor.audio.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.persoff68.speechratemonitor.audio.ai.SpeechDenoiser
import com.persoff68.speechratemonitor.audio.ai.SyllableCounter
import com.persoff68.speechratemonitor.audio.manager.NotificationManager
import com.persoff68.speechratemonitor.audio.processor.AudioProcessor
import com.persoff68.speechratemonitor.audio.recorder.AudioRecorder
import com.persoff68.speechratemonitor.audio.state.AudioState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AudioService : Service() {
    @Inject
    lateinit var audioState: AudioState

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var speechDenoiser: SpeechDenoiser

    @Inject
    lateinit var syllableCounter: SyllableCounter

    private var recorder: AudioRecorder? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager.createNotificationChannel()
        val notificationId = notificationManager.getNotificationId()
        val notification = notificationManager.getNotification()
        startForeground(notificationId, notification)

        audioState.reset()
        audioState.setRecording(true)

        val audioProcessor = AudioProcessor(audioState, speechDenoiser, syllableCounter)
        recorder = AudioRecorder(audioProcessor::run)
        recorder?.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        recorder?.stop()
        recorder = null

        audioState.reset()
        audioState.setRecording(false)

        stopForeground(STOP_FOREGROUND_DETACH)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
