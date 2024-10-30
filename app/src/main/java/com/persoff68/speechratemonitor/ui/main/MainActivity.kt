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
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechRateMonitorAppTheme {
                Scaffold { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
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
        composable("main") { MainScreen(modifier) }
    }
}