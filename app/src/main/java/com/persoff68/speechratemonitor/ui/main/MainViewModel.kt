package com.persoff68.speechratemonitor.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _showWaveform = savedStateHandle.getLiveData("showWaveform", true)
    val showWaveform = _showWaveform

    fun toggleIndicator() {
        _showWaveform.value = !(_showWaveform.value ?: true)
    }
}