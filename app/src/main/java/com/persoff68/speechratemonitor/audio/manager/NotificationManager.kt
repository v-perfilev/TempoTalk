package com.persoff68.speechratemonitor.audio.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.core.app.NotificationCompat
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.audio.manager.NotificationActionReceiver.Companion.ACTION_START
import com.persoff68.speechratemonitor.audio.manager.NotificationActionReceiver.Companion.ACTION_STOP
import com.persoff68.speechratemonitor.ui.main.MainActivity
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

    fun updateNotification(isRecording: Boolean) {
        val notification = getNotification(isRecording)
        val manager = context.getSystemService(AndroidNotificationManager::class.java)
        manager?.notify(NOTIFICATION_ID, notification)
    }

    fun getNotificationId() = NOTIFICATION_ID

    fun getNotification(isRecording: Boolean): Notification {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val startIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_START
        }
        val startPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            startIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(
                if (isRecording) context.getString(R.string.notification_content_analysing)
                else context.getString(R.string.notification_content_ready)
            )
            .setSmallIcon(R.drawable.ic_microphone)
            .setOngoing(true)
            .setLargeIcon(null as Icon?)
            .setContentIntent(openAppPendingIntent)
            .setColor(
                if (isRecording) context.getColor(R.color.red)
                else context.getColor(R.color.green)
            )
            .addAction(
                if (isRecording) R.drawable.ic_stop else R.drawable.ic_microphone,
                if (isRecording) context.getString(R.string.notification_action_stop)
                else context.getString(R.string.notification_action_start),
                if (isRecording) stopPendingIntent else startPendingIntent
            )
            .build()
    }
}