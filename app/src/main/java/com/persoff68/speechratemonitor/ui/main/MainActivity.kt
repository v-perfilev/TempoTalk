package com.persoff68.speechratemonitor.ui.main

import android.content.pm.ActivityInfo
import android.os.Bundle
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
import com.persoff68.speechratemonitor.audio.AudioModule
import com.persoff68.speechratemonitor.audio.manager.PermissionManager
import com.persoff68.speechratemonitor.audio.state.AudioState
import com.persoff68.speechratemonitor.settings.Settings
import com.persoff68.speechratemonitor.settings.SettingsRepository
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var audioState: AudioState

    @Inject
    lateinit var audioModule: AudioModule

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        permissionManager.initialize(this)
        enableEdgeToEdge()
        setContent {
            val settings by settingsRepository.settingsFlow
                .collectAsState(initial = Settings())
            SpeechRateMonitorAppTheme(settings.theme) {
                Scaffold { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    private fun MainContent(modifier: Modifier) {
        val navController = rememberNavController()
        MainNavigation(navController, modifier)
    }

    @Composable
    private fun MainNavigation(navController: NavHostController, modifier: Modifier) {
        NavHost(navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    modifier = modifier,
                    audioState = audioState,
                    audioModule = audioModule,
                    permissionManager = permissionManager
                )
            }
        }
    }
}