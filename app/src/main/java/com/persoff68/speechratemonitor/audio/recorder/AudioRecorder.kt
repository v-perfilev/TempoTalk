package com.persoff68.speechratemonitor.audio.recorder

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.persoff68.speechratemonitor.Config

class AudioRecorder(private val processor: (FloatArray) -> Unit) {
    private var audioRecord: AudioRecord? = null
    private var recordingThread: Thread? = null

    @Volatile
    private var isRecording = false

    fun start() {
        Log.i("AudioRecorder", "Start recording")
        isRecording = true
        audioRecord = createAudioRecord().apply { startRecording() }
        recordingThread = createRecordingThread().apply { start() }
    }

    fun stop() {
        Log.i("AudioRecorder", "Stop recording")
        isRecording = false
        recordingThread?.join()
        recordingThread = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    @SuppressLint("MissingPermission")
    private fun createAudioRecord(): AudioRecord {
        return AudioRecord(
            MediaRecorder.AudioSource.MIC,
            Config.AUDIO_SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            Config.AUDIO_BUFFER_SIZE
        )
    }

    private fun createRecordingThread(): Thread {
        return Thread {
            val buffer = ShortArray(Config.AUDIO_BUFFER_SIZE)
            while (isRecording) {
                val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (readSize > 0) {
                    val preparedArray = buffer.map { it.toFloat() }.toFloatArray()
                    processor(preparedArray)
                }
                Thread.sleep(50)
            }
        }
    }
}
