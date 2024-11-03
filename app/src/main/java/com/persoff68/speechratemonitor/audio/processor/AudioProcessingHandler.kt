package com.persoff68.speechratemonitor.audio.processor

interface AudioProcessingHandler {
    suspend fun handle(request: AudioProcessingRequest)
}