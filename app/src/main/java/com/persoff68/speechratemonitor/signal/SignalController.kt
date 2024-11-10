package com.persoff68.speechratemonitor.signal

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.settings.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: SettingsRepository

) {
    private var mediaPlayer: MediaPlayer? = null
    private var lastNotificationTime: Long = 0


    fun triggerStart() {
        playSound(R.raw.start)
        playVibration(100L)
        Log.d("SignalController", "Playing start signal")
    }

    fun triggerStop() {
        playSound(R.raw.stop)
        playVibration(100L)
        Log.d("SignalController", "Playing stop signal")
    }

    fun triggerAlert() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastNotificationTime < Config.PAUSE_BETWEEN_ALERTS_IN_SECONDS * 1000) return
        lastNotificationTime = currentTime

        playSound(R.raw.notification)
        playVibration(500L)
        Log.d("SignalController", "Playing alert signal")
    }

    private fun playSound(resId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val isSoundEnabled = repository.settingsFlow.first().soundNotification
            if (!isSoundEnabled) {
                return@launch
            }
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer?.apply {
                setOnCompletionListener {
                    release()
                }
                start()
            }
        }
    }

    private fun playVibration(duration: Long) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(VibratorManager::class.java)
            vibratorManager?.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        vibrator?.let {
            val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            it.vibrate(effect)
        }
    }

}