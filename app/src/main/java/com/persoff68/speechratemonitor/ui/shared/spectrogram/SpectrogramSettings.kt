package com.persoff68.speechratemonitor.ui.shared.spectrogram

import androidx.compose.ui.geometry.Size
import com.persoff68.speechratemonitor.Config

class SpectrogramSettings(size: Size = Size(0f, 0f)) {
    val cellWidth: Float = size.width / Config.SPECTROGRAM_VIEW_LENGTH
    val cellHeight: Float = size.height / Config.SPECTROGRAM_VIEW_HEIGHT

    val relativeRatio: Float = 0.6f
}