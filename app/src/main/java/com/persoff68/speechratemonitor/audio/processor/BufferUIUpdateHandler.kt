package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.audio.state.AudioState

class BufferUIUpdateHandler(
    private val audioState: AudioState
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        audioState.setBufferData(request.buffer)
    }
}