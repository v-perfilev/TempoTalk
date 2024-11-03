package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.Config

class BufferAccumulationHandler(
    private var accumulatedBuffer: FloatArray
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        synchronized(accumulatedBuffer) {
            accumulatedBuffer += request.buffer
            if (accumulatedBuffer.size >= Config.CHUNK_SIZE) {
                request.buffer = accumulatedBuffer.copyOf()
                accumulatedBuffer = FloatArray(0)
            } else {
                request.buffer = FloatArray(0)
            }
        }
    }
}
