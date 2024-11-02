package com.persoff68.speechratemonitor.ui.shared.spectrogram

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

class SpectrogramSettings(size: Size) {
    private val totalLength: Int = 40
    private val totalHeight: Int = 20

    val colorRatio: Float = 0.8f

    val startColor: Color = Color.Transparent
    val middleColor: Color = Color.Green
    val endColor: Color = Color.Yellow

    val cellWidth: Float = size.width / totalLength
    val cellHeight: Float = size.height / totalHeight
}
