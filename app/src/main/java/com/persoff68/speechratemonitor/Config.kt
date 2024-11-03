package com.persoff68.speechratemonitor

object Config {
    const val SAMPLE_RATE = 16000
    const val CHUNK_SIZE = 8000
    const val BUFFER_SIZE = 1000
    const val N_FFT = 256
    const val HOP_LENGTH = 122
    const val WIN_LENGTH = 256
    const val FREQUENCY_BINS = 128
    const val TIME_STEPS = 64
    const val FRAME_COUNT = 6
    const val FRAME_SIZE_IN_MS = 1000L * CHUNK_SIZE / SAMPLE_RATE

    const val LOW_CUTOFF_FREQ = 200
    const val HIGH_CUTOFF_FREQ = 6000
    const val NOISE_THRESHOLD = 0.05f

    const val MIN_GAUGE_VALUE = 0
    const val MAX_GAUGE_VALUE = 5

    const val BUFFER_VIEW_LENGTH = 100
    const val SPECTROGRAM_VIEW_LENGTH = 60
    const val SPECTROGRAM_VIEW_HEIGHT = 30
    const val SPECTROGRAM_SCALE_PARAMETER = 5

    const val DEFAULT_TEMPO = 0
    val DEFAULT_BUFFER = FloatArray(BUFFER_VIEW_LENGTH) { 0f }
    val DEFAULT_SPECTROGRAM = Array(SPECTROGRAM_VIEW_LENGTH) {
        FloatArray(SPECTROGRAM_VIEW_HEIGHT) { 0f }
    }
}
