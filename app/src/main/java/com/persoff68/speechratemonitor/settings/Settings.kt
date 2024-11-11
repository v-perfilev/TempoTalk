package com.persoff68.speechratemonitor.settings

import com.persoff68.speechratemonitor.Config

data class Settings(
    val maxSyllables: Int = Config.DEFAULT_MAX_SYLLABLES_VALUE,
    val warningThreshold: Int = Config.DEFAULT_WARNING_THRESHOLD_VALUE,
    val autoStopTimer: Int = Config.DEFAULT_AUTO_STOP_TIMEOUT,
    val soundNotification: Boolean = true,
    val noiseSuppression: Boolean = true,
    val defaultIndicator: IndicatorType = IndicatorType.Waveform,
    val theme: ThemeMode = ThemeMode.System
)

enum class IndicatorType {
    Waveform, Spectrogram
}

enum class ThemeMode {
    System, Light, Dark
}
