package com.persoff68.speechratemonitor.audio.manager

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import com.persoff68.speechratemonitor.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import android.app.NotificationManager as AndroidNotificationManager

@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CHANNEL_ID = "AudioServiceChannel"
        private const val CHANNEL_NAME = "Audio Service"
        private const val NOTIFICATION_ID = 1
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            AndroidNotificationManager.IMPORTANCE_LOW
        )

        val manager = context.getSystemService(AndroidNotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    fun getNotification(): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("SpeechRateMonitorApp")
            .setContentText("Recording audio in background")
            .setSmallIcon(R.drawable.ic_cog)
            .build()
    }

    fun getNotificationId(): Int = NOTIFICATION_ID
}