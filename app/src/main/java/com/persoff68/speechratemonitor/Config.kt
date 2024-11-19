package com.persoff68.speechratemonitor

object Config {
    /*
    Audio processing parameters
     */
    const val AUDIO_SAMPLE_RATE = 16000
    const val AUDIO_CHUNK_SIZE = 8000
    const val AUDIO_BUFFER_SIZE = 2000
    const val FFT_SIZE = 256
    const val FFT_HOP_LENGTH = 122
    const val FFT_WIN_LENGTH = 256
    const val SPECTROGRAM_FREQUENCY_BINS = 128
    const val SPECTROGRAM_TIME_STEPS = 64
    const val FRAME_COUNT = 4
    const val FRAME_DURATION_MS = 1000L * AUDIO_CHUNK_SIZE / AUDIO_SAMPLE_RATE

    /*
    Buffer and spectrogram visualization parameters
     */
    const val BUFFER_VIEW_LENGTH = 100
    const val SPECTROGRAM_VIEW_LENGTH = 60
    const val SPECTROGRAM_VIEW_HEIGHT = 30
    const val SPECTROGRAM_SCALE_FACTOR = 5

    /*
    Default visualisation values
     */
    const val DEFAULT_TEMPO = 0
    val DEFAULT_BUFFER = FloatArray(BUFFER_VIEW_LENGTH) { 0f }
    val DEFAULT_SPECTROGRAM = Array(SPECTROGRAM_VIEW_LENGTH) {
        FloatArray(SPECTROGRAM_VIEW_HEIGHT) { 0f }
    }

    /*
    Alert and vibration settings
     */
    const val ALERT_PAUSE_DURATION_MS = 3000L
    const val VIBRATION_DURATION_SHORT_MS = 200L
    const val VIBRATION_DURATION_LONG_MS = 1000L

    /*
    Configuration ranges for UI sliders
     */
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
