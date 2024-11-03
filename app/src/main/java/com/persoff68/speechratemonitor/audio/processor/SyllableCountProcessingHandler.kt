package com.persoff68.speechratemonitor.audio.processor

import android.util.Log
import com.persoff68.speechratemonitor.audio.ai.SyllableCounter
import com.persoff68.speechratemonitor.audio.util.TensorUtils
import org.pytorch.Tensor

class SyllableCountProcessingHandler(
    private val syllableCounter: SyllableCounter
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        if (request.spectrogram.isNotEmpty()) {
            val countingTensor = prepareTensorForCounting(request.spectrogram)
            val countedTensor = syllableCounter.forward(countingTensor)
            val syllableCount = extractSyllableCount(countedTensor)
            request.syllableCount = maxOf(syllableCount, 0)
        }
    }

    private fun prepareTensorForCounting(spectrogram: Array<FloatArray>): Tensor {
        val tensor = TensorUtils.toTensor(
            spectrogram,
            longArrayOf(1L, spectrogram.size.toLong(), spectrogram[0].size.toLong())
        )
        Log.d(
            "SpectrogramProcessingHandler",
            "Counting tensor: ${tensor.shape().contentToString()}"
        )
        return tensor
    }

    private fun extractSyllableCount(tensor: Tensor): Int {
        val array = tensor.dataAsFloatArray
        return array[0].toInt() - 1
    }
}