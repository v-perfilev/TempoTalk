package com.persoff68.speechratemonitor.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.persoff68.speechratemonitor.R
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.settings.Settings
import com.persoff68.speechratemonitor.settings.SettingsRepository
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @Inject
    lateinit var audioState: AudioState

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by settingsRepository.settingsFlow
                .collectAsState(initial = Settings())
            SpeechRateMonitorAppTheme(settings.theme) {
                Scaffold { innerPadding ->
                    SettingsContent(Modifier.padding(innerPadding))
                }
            }
        }

        if (audioState.isRecordingState.value) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.settings_active_analysing_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Composable
    private fun SettingsContent(modifier: Modifier) {
        val navController = rememberNavController()
        SettingsNavigation(navController, modifier)
    }

    @Composable
    private fun SettingsNavigation(navController: NavHostController, modifier: Modifier) {
        NavHost(navController, startDestination = "main") {
            composable("main") {
                SettingsScreen(
                    modifier = modifier,
                    audioState = audioState
                )
            }
        }
    }
}

