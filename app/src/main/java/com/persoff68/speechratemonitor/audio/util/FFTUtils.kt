package com.persoff68.speechratemonitor.audio.util

import com.persoff68.speechratemonitor.Config
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.sqrt

object FFTUtils {

    private val window = FloatArray(Config.FFT_WIN_LENGTH) { index ->
        (0.54 - 0.46 * kotlin.math.cos(2.0 * kotlin.math.PI * index / (Config.FFT_WIN_LENGTH - 1))).toFloat()
    }

    suspend fun processSpectrogram(input: FloatArray): Array<FloatArray> = coroutineScope {
        val segments = mutableListOf<Deferred<FloatArray>>()
        var start = 0
        while (start + Config.FFT_WIN_LENGTH <= input.size) {
            val segment = input.copyOfRange(start, start + Config.FFT_WIN_LENGTH)
            segments.add(async(Dispatchers.Default) {
                fftMagnitudeSpectrum(segment)
            })
            start += Config.FFT_HOP_LENGTH
        }
        segments.map { it.await() }.toTypedArray()
    }

    private fun fftMagnitudeSpectrum(input: FloatArray): FloatArray {
        val real = applyWindow(input.copyOfRange(0, Config.FFT_WIN_LENGTH))
        val imag = FloatArray(Config.FFT_SIZE) { 0f }
        fftRecursive(real, imag, Config.FFT_SIZE)
        return calculateMagnitude(real, imag)
    }

    private fun applyWindow(input: FloatArray): FloatArray {
        return input.mapIndexed { index, value ->
            if (index < window.size) value * window[index] else 0f
        }.toFloatArray()
    }

    private fun fftRecursive(real: FloatArray, imag: FloatArray, n: Int) {
        if (n <= 1) return

        val evenReal = FloatArray(n / 2)
        val evenImag = FloatArray(n / 2)
        val oddReal = FloatArray(n / 2)
        val oddImag = FloatArray(n / 2)

        for (i in 0 until n / 2) {
            evenReal[i] = real[2 * i]
            evenImag[i] = imag[2 * i]
            oddReal[i] = real[2 * i + 1]
            oddImag[i] = imag[2 * i + 1]
        }

        fftRecursive(evenReal, evenImag, n / 2)
        fftRecursive(oddReal, oddImag, n / 2)

        for (k in 0 until n / 2) {
            val angle = 2 * kotlin.math.PI * k / n
            val cos = kotlin.math.cos(angle).toFloat()
            val sin = kotlin.math.sin(angle).toFloat()

            val tpre = oddReal[k] * cos + oddImag[k] * sin
            val tpim = -oddReal[k] * sin + oddImag[k] * cos

            real[k] = evenReal[k] + tpre
            imag[k] = evenImag[k] + tpim
            real[k + n / 2] = evenReal[k] - tpre
            imag[k + n / 2] = evenImag[k] - tpim
        }
    }

    private fun calculateMagnitude(real: FloatArray, imag: FloatArray): FloatArray =
        FloatArray(Config.FFT_SIZE / 2) { i ->
            sqrt(real[i] * real[i] + imag[i] * imag[i])
        }
}
