package com.persoff68.speechratemonitor.audio.processor

data class AudioProcessingRequest(
    var buffer: FloatArray = floatArrayOf(),
    var spectrogram: Array<FloatArray> = arrayOf(),
    var syllableCount: Int = 0
)
