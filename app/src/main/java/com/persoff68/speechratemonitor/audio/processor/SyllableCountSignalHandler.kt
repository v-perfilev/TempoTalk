package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.settings.SettingsRepository
import com.persoff68.speechratemonitor.signal.SignalController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SyllableCountSignalHandler(
    private val signalController: SignalController,
    private val settingsRepository: SettingsRepository
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        CoroutineScope(Dispatchers.Default).launch {
            val warningThreshold = settingsRepository.settingsFlow.first().warningThreshold
            if (request.syllableCount >= warningThreshold) {
                signalController.triggerAlert()
            }
        }
    }
}