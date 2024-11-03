package com.persoff68.speechratemonitor.ui.main

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
import com.persoff68.speechratemonitor.audio.AudioModule
import com.persoff68.speechratemonitor.audio.manager.PermissionManager
import com.persoff68.speechratemonitor.audio.state.AudioState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager.initialize(this)
        enableEdgeToEdge()
        setContent {
            SpeechRateMonitorAppTheme {
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
//        composable("main") { MainScreen(modifier) }
            composable("main") {
                GaugeScreen(
                    modifier = modifier,
                    audioState = audioState,
                    audioModule = audioModule,
                    permissionManager = permissionManager
                )
            }
        }
    }
}