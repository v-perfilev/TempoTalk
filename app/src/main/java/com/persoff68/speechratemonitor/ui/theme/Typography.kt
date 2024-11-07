package com.persoff68.speechratemonitor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.persoff68.speechratemonitor.R

val DigitalFontFamily = FontFamily(
    Font(R.font.digital_7_regular, FontWeight.Normal, FontStyle.Normal),
)

val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = DigitalFontFamily,
    ),
)