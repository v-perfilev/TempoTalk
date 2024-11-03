package com.persoff68.speechratemonitor.audio.ai

import android.content.Context
import com.persoff68.speechratemonitor.audio.util.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import javax.inject.Inject

class SpeechDenoiser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var module: Module

    init {
        val modelPath = FileUtils.assetFilePath(context, "speech_denoiser_model_mobile.pt")
        module = Module.load(modelPath)
    }

    fun forward(input: Tensor): Tensor {
        return module.forward(IValue.from(input)).toTensor()
    }
}
