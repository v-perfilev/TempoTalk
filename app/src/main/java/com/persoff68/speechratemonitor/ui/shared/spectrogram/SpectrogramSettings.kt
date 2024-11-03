package com.persoff68.speechratemonitor.ui.shared.spectrogram

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.persoff68.speechratemonitor.Config

class SpectrogramSettings(size: Size) {
    private val totalLength: Int = Config.SPECTROGRAM_VIEW_LENGTH
    private val totalHeight: Int = Config.SPECTROGRAM_VIEW_HEIGHT

    val colorRatio: Float = 0.6f

    val startColor: Color = Color.Transparent
    val middleColor: Color = Color.Green
    val endColor: Color = Color.Yellow

    val cellWidth: Float = size.width / totalLength
    val cellHeight: Float = size.height / totalHeight
}
