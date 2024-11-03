package com.persoff68.speechratemonitor.audio.util

import org.pytorch.Tensor

object TensorUtils {

    fun toTensor(matrix: Array<FloatArray>, shape: LongArray): Tensor {
        val normalizedFlatArray = matrix.map { it.toList() }.flatten().toFloatArray()
        return Tensor.fromBlob(normalizedFlatArray, shape)
    }
}
