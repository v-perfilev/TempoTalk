package com.persoff68.speechratemonitor.audio.processor

class AudioProcessingChain(
    private val handlers: List<AudioProcessingHandler>
) {
    suspend fun process(request: AudioProcessingRequest) {
        for (handler in handlers) {
            handler.handle(request)
        }
    }
}