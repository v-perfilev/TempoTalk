package com.persoff68.speechratemonitor.audio.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.persoff68.speechratemonitor.audio.ai.SpeechDenoiser
import com.persoff68.speechratemonitor.audio.ai.SyllableCounter
import com.persoff68.speechratemonitor.audio.manager.NotificationManager
import com.persoff68.speechratemonitor.audio.normalizer.AudioNormalizer
import com.persoff68.speechratemonitor.audio.processor.AudioProcessor
import com.persoff68.speechratemonitor.audio.recorder.AudioRecorder
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.settings.SettingsRepository
import com.persoff68.speechratemonitor.signal.SignalController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    @Inject
    lateinit var signalController: SignalController

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private var recorder: AudioRecorder? = null

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var autoStopJob: Job? = null

    private val lifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> startAutoStopTimer()
            Lifecycle.Event.ON_START -> autoStopJob?.cancel()
            else -> {}
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager.createNotificationChannel()
        val notificationId = notificationManager.getNotificationId()
        val notification = notificationManager.getNotification(false)
        startForeground(notificationId, notification)
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    fun startRecording() {
        notificationManager.updateNotification(true)
        audioState.setRecording(true)

        val normalizer = AudioNormalizer()

        val audioProcessor = AudioProcessor(
            audioState,
            normalizer,
            speechDenoiser,
            syllableCounter,
            signalController,
            settingsRepository
        )
        recorder = AudioRecorder(audioProcessor::run)
        recorder?.start()

        signalController.triggerStart()
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    fun stopRecording() {
        recorder?.stop()
        recorder = null

        audioState.reset()
        audioState.setRecording(false)
        notificationManager.updateNotification(false)

        signalController.triggerStop()
        autoStopJob?.cancel()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleObserver)
    }

    private fun startAutoStopTimer() {
        serviceScope.launch {
            val autoStopTime = settingsRepository.settingsFlow.first().autoStopTimer
            if (autoStopTime > 0) {
                autoStopJob?.cancel()
                autoStopJob = serviceScope.launch {
                    delay(autoStopTime * 60L * 1000L)
                    stopRecording()
                }
            }
        }
    }

}
