package com.persoff68.speechratemonitor.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persoff68.speechratemonitor.settings.IndicatorType
import com.persoff68.speechratemonitor.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: SettingsRepository
) : ViewModel() {
    private val _showWaveform: MutableLiveData<Boolean> =
        savedStateHandle.getLiveData("showWaveform", true)
    val showWaveform: LiveData<Boolean> = _showWaveform

    init {
        viewModelScope.launch {
            val settings = repository.settingsFlow.first()
            val isWaveform = settings.defaultIndicator == IndicatorType.Waveform
            _showWaveform.value = isWaveform
        }
    }

    fun toggleIndicator() {
        _showWaveform.value = !(_showWaveform.value ?: true)
    }
}