package com.persoff68.speechratemonitor

object Config {
    const val SAMPLE_RATE = 16000
    const val CHUNK_SIZE = 8000
    const val BUFFER_SIZE = 2000
    const val N_FFT = 256
    const val HOP_LENGTH = 122
    const val WIN_LENGTH = 256
    const val FREQUENCY_BINS = 128
    const val TIME_STEPS = 64
    const val FRAME_COUNT = 4
    const val FRAME_SIZE_IN_MS = 1000L * CHUNK_SIZE / SAMPLE_RATE

    const val BUFFER_VIEW_LENGTH = 100
    const val SPECTROGRAM_VIEW_LENGTH = 60
    const val SPECTROGRAM_VIEW_HEIGHT = 30
    const val SPECTROGRAM_SCALE_PARAMETER = 5

    const val DEFAULT_TEMPO = 0
    val DEFAULT_BUFFER = FloatArray(BUFFER_VIEW_LENGTH) { 0f }
    val DEFAULT_SPECTROGRAM = Array(SPECTROGRAM_VIEW_LENGTH) {
        FloatArray(SPECTROGRAM_VIEW_HEIGHT) { 0f }
    }

    const val PAUSE_BETWEEN_ALERTS_IN_SECONDS = 3000L
    const val SHORT_VIBRATION_DURATION = 200L
    const val LONG_VIBRATION_DURATION = 1000L

    const val DEFAULT_MAX_SYLLABLES_VALUE = 7
    const val BOTTOM_MAX_SYLLABLES_VALUE = 5f
    const val TOP_MAX_SYLLABLES_VALUE = 10f
    const val DEFAULT_WARNING_THRESHOLD_VALUE = 5
    const val BOTTOM_WARNING_THRESHOLD_VALUE = 3f
    const val TOP_WARNING_THRESHOLD_VALUE = 10f
    const val DEFAULT_AUTO_STOP_TIMEOUT = 10
    const val BOTTOM_AUTO_STOP_TIMEOUT = 0f
    const val TOP_AUTO_STOP_TIMEOUT = 30f
}
