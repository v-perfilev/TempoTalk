package com.persoff68.speechratemonitor.audio.processor

import com.persoff68.speechratemonitor.audio.ai.SpeechDenoiser
import com.persoff68.speechratemonitor.audio.ai.SyllableCounter
import com.persoff68.speechratemonitor.audio.state.AudioState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioProcessor(
    audioState: AudioState,
    speechDenoiser: SpeechDenoiser,
    syllableCounter: SyllableCounter
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Volatile
    private var accumulatedBuffer = FloatArray(0)

    @Volatile
    private var accumulatedSpectrogram = Array(0) { FloatArray(0) }

    private val processingChain = AudioProcessingChain(
        listOf(
            BufferNormalizationHandler(),
            BufferUIUpdateHandler(audioState),
            BufferAccumulationHandler(accumulatedBuffer),
            SpectrogramProcessingHandler(speechDenoiser),
            SpectrogramUIUpdateHandler(audioState),
            SpectrogramAccumulationHandler(accumulatedSpectrogram),
            SyllableCountProcessingHandler(syllableCounter),
            SyllableCountUIUpdateHandler(audioState)
        )
    )

    fun run(buffer: FloatArray) = coroutineScope.launch {
        val request = AudioProcessingRequest(buffer = buffer)
        processingChain.process(request)
    }

}

