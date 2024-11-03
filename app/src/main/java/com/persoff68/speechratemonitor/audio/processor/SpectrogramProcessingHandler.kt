package com.persoff68.speechratemonitor.audio.processor

import android.util.Log
import com.persoff68.speechratemonitor.Config
import com.persoff68.speechratemonitor.audio.ai.SpeechDenoiser
import com.persoff68.speechratemonitor.audio.util.ArrayUtils
import com.persoff68.speechratemonitor.audio.util.FFTUtils
import com.persoff68.speechratemonitor.audio.util.TensorUtils
import org.pytorch.Tensor

class SpectrogramProcessingHandler(
    private val speechDenoiser: SpeechDenoiser
) : AudioProcessingHandler {
    override suspend fun handle(request: AudioProcessingRequest) {
        if (request.buffer.isNotEmpty()) {
            val inputSpectrogram = FFTUtils.processSpectrogram(request.buffer)
            val denoisingTensor = prepareTensorForDenoising(inputSpectrogram)
            val denoisedTensor = speechDenoiser.forward(denoisingTensor)
            val denoisedSpectrogram = extractDenoisedSpectrogram(denoisedTensor)
            request.spectrogram = denoisedSpectrogram
        }
    }

    private fun prepareTensorForDenoising(spectrogram: Array<FloatArray>): Tensor {
        val transposedSpectrogram = ArrayUtils.transpose(spectrogram)
        val tensor = TensorUtils.toTensor(
            transposedSpectrogram,
            longArrayOf(1L, 1L, spectrogram.size.toLong(), spectrogram[0].size.toLong())
        )
        Log.d("BufferProcessingHandler", "Denoising tensor: ${tensor.shape().contentToString()}")
        return tensor
    }

    private fun extractDenoisedSpectrogram(tensor: Tensor): Array<FloatArray> {
        val array = tensor.dataAsFloatArray
        val spectrogram = ArrayUtils.reshape(array, Config.FREQUENCY_BINS, Config.TIME_STEPS)
        return ArrayUtils.transpose(spectrogram)
    }
}
