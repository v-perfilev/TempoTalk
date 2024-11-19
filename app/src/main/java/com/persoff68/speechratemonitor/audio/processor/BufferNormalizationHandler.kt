package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.audio.normalizer.AudioNormalizer

class BufferNormalizationHandler(
    private val normalizer: AudioNormalizer
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        request.buffer = normalizer.normalize(request.buffer)
    }
}