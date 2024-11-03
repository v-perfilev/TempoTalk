package com.persoff68.speechratemonitor.audio.util

import kotlin.math.sqrt

object ArrayUtils {
    fun transpose(matrix: Array<FloatArray>): Array<FloatArray> {
        val rowCount = matrix.size
        val colCount = matrix[0].size
        val transposed = Array(colCount) { FloatArray(rowCount) }
        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                transposed[j][i] = matrix[i][j]
            }
        }
        return transposed
    }

    fun reshape(array: FloatArray, rows: Int, cols: Int): Array<FloatArray> {
        val matrix = Array(rows) { FloatArray(cols) }
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                matrix[i][j] = array[i * cols + j]
            }
        }
        return matrix
    }

    fun slice(matrix: Array<FloatArray>, start: Int): Array<FloatArray> {
        val newLength = matrix.size - start
        val sliced = Array(newLength) { FloatArray(matrix[0].size) }
        for (i in start until matrix.size) {
            sliced[i - start] = matrix[i]
        }
        return sliced
    }

    fun normalize(array: FloatArray): FloatArray {
        val mean = 0f
        val variance = 40000f
        val standardDeviation = sqrt(variance)
        val standardizedArray = array.map { (it - mean) / standardDeviation }.toFloatArray()
        val max = 40f
        return standardizedArray.map { it / max }.toFloatArray()
    }
}
