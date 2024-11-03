package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.audio.util.ArrayUtils

class BufferNormalizationHandler : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        request.buffer = ArrayUtils.normalize(request.buffer)
    }
}