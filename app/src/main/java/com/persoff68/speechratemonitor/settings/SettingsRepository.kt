package com.persoff68.speechratemonitor.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val MAX_SYLLABLES = intPreferencesKey("max_syllables")
        val WARNING_THRESHOLD = intPreferencesKey("warning_threshold")
        val AUTO_STOP_TIMER = intPreferencesKey("auto_stop_timer")
        val SOUND_NOTIFICATION = booleanPreferencesKey("sound_notification")
        val NOISE_SUPPRESSION = booleanPreferencesKey("noise_suppression")
        val INDICATOR_TYPE = stringPreferencesKey("default_indicator_type")
        val THEME = stringPreferencesKey("theme")
    }

    val settingsFlow: Flow<Settings> = dataStore.data.map { preferences ->
        val defaultSettings = Settings()
        Settings(
            maxSyllables = preferences[MAX_SYLLABLES] ?: defaultSettings.maxSyllables,
            warningThreshold = preferences[WARNING_THRESHOLD] ?: defaultSettings.warningThreshold,
            autoStopTimer = preferences[AUTO_STOP_TIMER] ?: defaultSettings.autoStopTimer,
            soundNotification = preferences[SOUND_NOTIFICATION]
                ?: defaultSettings.soundNotification,
            noiseSuppression = preferences[NOISE_SUPPRESSION] ?: defaultSettings.noiseSuppression,
            defaultIndicator = IndicatorType.valueOf(
                preferences[INDICATOR_TYPE] ?: defaultSettings.defaultIndicator.toString()
            ),
            theme = ThemeMode.valueOf(preferences[THEME] ?: defaultSettings.theme.toString()),
        )
    }

    suspend fun updateMaxSyllables(value: Int) {
        dataStore.edit { it[MAX_SYLLABLES] = value }
    }

    suspend fun updateWarningThreshold(value: Int) {
        dataStore.edit { it[WARNING_THRESHOLD] = value }
    }

    suspend fun updateAutoStopTimer(value: Int) {
        dataStore.edit { it[AUTO_STOP_TIMER] = value }
    }

    suspend fun updateSoundNotification(value: Boolean) {
        dataStore.edit { it[SOUND_NOTIFICATION] = value }
    }

    suspend fun updateNoiseSuppression(value: Boolean) {
        dataStore.edit { it[NOISE_SUPPRESSION] = value }
    }

    suspend fun updateDefaultIndicatorType(value: IndicatorType) {
        dataStore.edit { it[INDICATOR_TYPE] = value.name }
    }

    suspend fun updateTheme(value: ThemeMode) {
        dataStore.edit { it[THEME] = value.name }
    }

}
