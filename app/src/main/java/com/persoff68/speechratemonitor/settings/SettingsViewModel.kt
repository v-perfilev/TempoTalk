package com.persoff68.speechratemonitor.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<Settings> = repository.settingsFlow.stateIn(
        viewModelScope,
        initialValue = Settings(),
        started = kotlinx.coroutines.flow.SharingStarted.Lazily
    )

    fun updateMaxSyllables(value: Int) = viewModelScope.launch {
        repository.updateMaxSyllables(value)
    }

    fun updateWarningThreshold(value: Int) = viewModelScope.launch {
        repository.updateWarningThreshold(value)
    }

    fun updateAutoStopTimer(value: Int) = viewModelScope.launch {
        repository.updateAutoStopTimer(value)
    }

    fun updateSoundNotification(value: Boolean) = viewModelScope.launch {
        repository.updateSoundNotification(value)
    }

    fun updateNoiseSuppression(value: Boolean) = viewModelScope.launch {
        repository.updateNoiseSuppression(value)
    }

    fun updateDefaultIndicator(value: IndicatorType) = viewModelScope.launch {
        repository.updateDefaultIndicatorType(value)
    }

    fun updateTheme(value: ThemeMode) = viewModelScope.launch {
        repository.updateTheme(value)
    }

}
