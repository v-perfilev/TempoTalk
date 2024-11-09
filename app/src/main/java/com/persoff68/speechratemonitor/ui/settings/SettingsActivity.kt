package com.persoff68.speechratemonitor.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @Inject
    lateinit var audioState: AudioState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechRateMonitorAppTheme {
                Scaffold { innerPadding ->
                    SettingsContent(Modifier.padding(innerPadding))
                }
            }
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

