package com.persoff68.speechratemonitor.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechRateMonitorAppTheme {
                SettingsContent()
            }
        }
    }
}

@Composable
private fun SettingsContent() {
    val navController = rememberNavController()
    SettingsNavigation(navController)
}

@Composable
private fun SettingsNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { SettingsScreen() }
    }
}