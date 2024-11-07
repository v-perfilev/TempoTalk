package com.persoff68.speechratemonitor.ui.shared.gauge

import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize

class GaugeSettings(intSize: IntSize = IntSize(0, 0)) {
    val size = intSize.toSize()

    val arcAngle: Float = 210f
    val arcWidth: Float = 120f

    val glowParameter: Float = 15f

    val strokeWidth: Float = 8f
    val strokeAngle: Float = 0.6f

    val tickCount: Int = 10
    val tickLength: Float = 20f
    val tickWidth: Float = 5f
    val tickAngle: Float = 0.2f

    val needleLength: Float = size.minDimension / 2 - arcWidth - 40f
    val needleWidth: Float = 40f
}