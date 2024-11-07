package com.persoff68.speechratemonitor.ui.shared.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetStatusBarTheme(color: Color, isLightTheme: Boolean) {
    val activity = LocalContext.current as Activity
    val window = activity.window
    window.statusBarColor = color.toArgb()
    WindowInsetsControllerCompat(window, window.decorView)
        .isAppearanceLightStatusBars = isLightTheme
}