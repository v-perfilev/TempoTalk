package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.audio.util.ArrayUtils

class SpectrogramAccumulationHandler(
    private var accumulatedSpectrogram: Array<FloatArray>
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        if (request.spectrogram.isNotEmpty()) {
            synchronized(accumulatedSpectrogram) {
                accumulatedSpectrogram += request.spectrogram
                request.spectrogram = accumulatedSpectrogram.copyOf()
                if (accumulatedSpectrogram.size > Config.TIME_STEPS * Config.FRAME_COUNT) {
                    accumulatedSpectrogram = ArrayUtils
                        .slice(accumulatedSpectrogram, Config.TIME_STEPS)
                }
            }
        }
    }
}