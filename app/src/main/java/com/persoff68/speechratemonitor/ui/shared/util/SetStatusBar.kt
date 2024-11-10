package com.persoff68.speechratemonitor.ui.shared.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import com.persoff68.speechratemonitor.ui.theme.LocalDarkTheme

@Composable
fun SetStatusBarTheme() {
    val isDarkTheme = LocalDarkTheme.current
    val activity = LocalContext.current as Activity
    val window = activity.window
    window.statusBarColor = Color.Transparent.toArgb()
    WindowInsetsControllerCompat(window, window.decorView)
        .isAppearanceLightStatusBars = !isDarkTheme
}