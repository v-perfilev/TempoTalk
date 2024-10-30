package com.persoff68.speechratemonitor.ui.main

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.persoff68.speechratemonitor.ui.settings.SettingsActivity
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Composable
fun MainScreen() {
    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
            },
            modifier = Modifier.padding(bottom = 16.dp),
        ) {
            Text("To SettingsScreen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SpeechRateMonitorAppTheme {
        MainScreen()
    }
}