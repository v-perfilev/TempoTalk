package com.persoff68.speechratemonitor.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persoff68.speechratemonitor.settings.IndicatorType
import com.persoff68.speechratemonitor.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SettingsRepository
) : ViewModel() {

    private val _showWaveform: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val showWaveform: LiveData<Boolean> = _showWaveform

    init {
        viewModelScope.launch {
            repository.settingsFlow.collect { settings ->
                val savedValue = savedStateHandle.get<Boolean>("showWaveform")
                if (savedValue != null) {
                    _showWaveform.value = savedValue!!
                } else {
                    _showWaveform.value = settings.defaultIndicator == IndicatorType.Waveform
                }
            }
        }
    }

    fun toggleIndicator() {
        _showWaveform.value = !(_showWaveform.value ?: true)
    }
}