package com.persoff68.speechratemonitor.audio.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.persoff68.speechratemonitor.audio.AudioModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var audioModule: AudioModule

    companion object {
        const val ACTION_START = "ACTION_START_RECORDING"
        const val ACTION_STOP = "ACTION_STOP_RECORDING"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            when (intent.action) {
                ACTION_START -> audioModule.start()
                ACTION_STOP -> audioModule.stop()
            }
        }
    }
}