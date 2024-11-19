package com.persoff68.speechratemonitor.audio.normalizer

import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

class AudioNormalizer {

    private val noiseLevelBuffer = FloatingBuffer(10)
    private val rmsBuffer = FloatingBuffer(10)

    private val windowSize: Int = 100
    private val inputMin = 20f
    private val inputMax = 20000f
    private val outputMin = 1000f
    private val outputMax = 60000f

    fun normalize(buffer: FloatArray): FloatArray {
        val rmsWindows = calculateRmsWindows(buffer)
        val avgNoiseLevel = calculateNoiseLevel(rmsWindows)
        val adjustedBuffer = adjustForNoise(buffer, avgNoiseLevel)
        val avgDenoisedRms = calculateAdjustedRms(adjustedBuffer)
        val normalizationParameter = mapRmsToNormalizationParameter(avgDenoisedRms)
        return adjustedBuffer.map { it / normalizationParameter }.toFloatArray()
    }

    private fun calculateRmsWindows(array: FloatArray): List<Float> =
        array
            .asSequence()
            .chunked(windowSize)
            .map { calculateRms(it.toFloatArray()) }
            .toList()

    private fun calculateNoiseLevel(rmsWindows: List<Float>): Float {
        val noiseLevel = rmsWindows
            .sorted()
            .take(rmsWindows.size / 10)
            .average()
            .toFloat()
        return noiseLevelBuffer.add(noiseLevel)
    }

    private fun adjustForNoise(array: FloatArray, noiseLevel: Float): FloatArray =
        array.map { sign(it) * maxOf(0f, abs(it) - noiseLevel) }.toFloatArray()

    private fun calculateAdjustedRms(array: FloatArray): Float {
        val denoisedRms = calculateRms(array)
        return rmsBuffer.add(denoisedRms)
    }

    private fun calculateRms(window: FloatArray): Float {
        val sum = window.sumOf { it.toDouble() * it.toDouble() }
        return sqrt(sum / window.size).toFloat()
    }

    private fun mapRmsToNormalizationParameter(rms: Float): Float {
        val clampedRms = rms.coerceIn(inputMin, inputMax)
        return outputMin + (clampedRms - inputMin) * (outputMax - outputMin) / (inputMax - inputMin)
    }

}