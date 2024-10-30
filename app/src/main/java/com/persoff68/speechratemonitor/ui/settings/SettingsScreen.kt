package com.persoff68.speechratemonitor.ui.settings

import android.app.Activity
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
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current as Activity

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                context.finish()
            },
            modifier = Modifier.padding(bottom = 16.dp),
        ) {
            Text("To MainScreen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SpeechRateMonitorAppTheme {
        SettingsScreen()
    }
}