package com.persoff68.speechratemonitor.audio

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.persoff68.speechratemonitor.audio.service.AudioService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioModule @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun start() {
        val intent = Intent(context, AudioService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop() {
        val intent = Intent(context, AudioService::class.java)
        context.stopService(intent)
    }
}
