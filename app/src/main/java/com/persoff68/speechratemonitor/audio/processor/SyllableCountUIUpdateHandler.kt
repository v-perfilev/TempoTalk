package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.audio.state.AudioState

class SyllableCountUIUpdateHandler(
    private val audioState: AudioState
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        if (request.spectrogram.isNotEmpty()) {
            audioState.setTempoData(request.syllableCount)
        }
    }
}