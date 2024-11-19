package com.persoff68.speechratemonitor.audio.normalizer

class FloatingBuffer(private val limit: Int) {

    val buffer = ArrayDeque<Float>().apply { add(60000f) }

    fun add(value: Float): Float {
        buffer.addLast(value)
        while (buffer.size > limit) buffer.removeFirst()
        return calculateWeightedAverage()
    }

    private fun calculateWeightedAverage(): Float {
        val weights = generateWeights(buffer.size)
        val weightedSum = buffer.zip(weights) { value, weight -> value * weight }.sum()
        val totalWeight = weights.sum()
        return weightedSum / totalWeight
    }

    private fun generateWeights(size: Int): List<Float> {
        return (1..size).map { it.toFloat() }
    }

}