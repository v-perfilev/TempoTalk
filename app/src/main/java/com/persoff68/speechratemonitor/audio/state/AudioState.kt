package com.persoff68.speechratemonitor.audio.state

import com.persoff68.speechratemonitor.Config
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class AudioState @Inject constructor() {
    private val _isRecordingState = MutableStateFlow(false)
    val isRecordingState: StateFlow<Boolean> = _isRecordingState.asStateFlow()

    private val _tempoState = MutableStateFlow(Config.DEFAULT_TEMPO)
    val tempoState: StateFlow<Int> = _tempoState.asStateFlow()

    private val _bufferState = MutableStateFlow(Config.DEFAULT_BUFFER)
    val bufferState: StateFlow<FloatArray> = _bufferState.asStateFlow()

    private val _spectrogramState = MutableStateFlow(Config.DEFAULT_SPECTROGRAM)
    val spectrogramState: StateFlow<Array<FloatArray>> = _spectrogramState.asStateFlow()

    @Synchronized
    fun setRecording(value: Boolean) {
        _isRecordingState.value = value
    }

    @Synchronized
    fun setTempoData(value: Int) {
        _tempoState.value = value
    }

    @Synchronized
    fun setBufferData(value: FloatArray) {
        _bufferState.value = scaleBufferData(value)
    }

    @Synchronized
    fun setSpectrogramData(value: Array<FloatArray>) {
        _spectrogramState.value = scaleSpectrogramData(value)
    }

    @Synchronized
    fun reset() {
        setTempoData(Config.DEFAULT_TEMPO)
        setBufferData(Config.DEFAULT_BUFFER)
        setSpectrogramData(Config.DEFAULT_SPECTROGRAM)
    }

    private fun scaleBufferData(inputArray: FloatArray): FloatArray {
        val step = (inputArray.size - 1).toFloat() / (Config.BUFFER_VIEW_LENGTH - 1).toFloat()

        val result = FloatArray(Config.BUFFER_VIEW_LENGTH)

        for (i in 0 until Config.BUFFER_VIEW_LENGTH) {
            val index = i * step
            val lowerIndex = index.toInt()
            val upperIndex = (lowerIndex + 1).coerceAtMost(inputArray.size - 1)

            val weight = index - lowerIndex
            val interpolatedValue =
                inputArray[lowerIndex] * (1 - weight) + inputArray[upperIndex] * weight

            result[i] = interpolatedValue.coerceIn(-1f, 1f)
        }

        return result
    }

    private fun scaleSpectrogramData(inputArray: Array<FloatArray>): Array<FloatArray> {
        val numRows = inputArray.size
        val numCols = inputArray[0].size
        val length = Config.SPECTROGRAM_VIEW_LENGTH / Config.FRAME_COUNT
        val height = Config.SPECTROGRAM_VIEW_HEIGHT
        val radius = Config.SPECTROGRAM_SCALE_PARAMETER

        val result = Array(length) { FloatArray(height) }

        val rowStep = (numRows - 1).toFloat() / (length - 1).toFloat()
        val colStep = (numCols - 1).toFloat() / (height - 1).toFloat()

        for (i in 0 until length) {
            val rowIndex = i * rowStep
            val lowerRowIndex = rowIndex.toInt()
            val upperRowIndex = (lowerRowIndex + 1).coerceAtMost(numRows - 1)
            val rowWeight = rowIndex - lowerRowIndex

            for (j in 0 until height) {
                val colIndex = j * colStep
                val lowerColIndex = colIndex.toInt()

                var sumTop = 0f
                var sumBottom = 0f
                var totalWeightTop = 0f
                var totalWeightBottom = 0f

                for (r in -radius..radius) {
                    val rowOffsetTop = (lowerRowIndex + r).coerceIn(0, numRows - 1)
                    val rowOffsetBottom = (upperRowIndex + r).coerceIn(0, numRows - 1)

                    for (c in -radius..radius) {
                        val colOffset = (lowerColIndex + c).coerceIn(0, numCols - 1)

                        val distance = sqrt((r * r + c * c).toDouble()).toFloat()
                        val weight = 1 / (1 + distance)

                        sumTop += inputArray[rowOffsetTop][colOffset] * weight
                        sumBottom += inputArray[rowOffsetBottom][colOffset] * weight

                        totalWeightTop += weight
                        totalWeightBottom += weight
                    }
                }

                val normalizedTop = sumTop / totalWeightTop
                val normalizedBottom = sumBottom / totalWeightBottom

                result[i][j] = normalizedTop * (1 - rowWeight) + normalizedBottom * rowWeight
            }
        }

        return result
    }

}
