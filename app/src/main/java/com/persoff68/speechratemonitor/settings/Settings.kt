package com.persoff68.speechratemonitor.settings

data class Settings(
    val maxSyllables: Int = 7,
    val warningThreshold: Int = 5,
    val autoStopTimer: Int = 10,
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
