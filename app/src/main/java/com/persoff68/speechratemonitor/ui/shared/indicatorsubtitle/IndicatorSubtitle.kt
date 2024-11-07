package com.persoff68.speechratemonitor.ui.shared.indicatorsubtitle

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.persoff68.speechratemonitor.ui.shared.gauge.Gauge
import com.persoff68.speechratemonitor.ui.theme.SpeechRateMonitorAppTheme


@Preview(showBackground = true, device = Devices.PIXEL, apiLevel = 34)
@Composable
fun IndicatorSubtitlePreview() {
    SpeechRateMonitorAppTheme {
        Surface {
            Gauge(value = 17, minValue = 0, maxValue = 20)
        }
    }
}

@Composable
fun IndicatorSubtitle(showWaveform: Boolean) {
    val subtitleTextColor = MaterialTheme.colorScheme.onBackground

    Text(
        text = if (showWaveform) "Waveform" else "Spectrogram",
        style = MaterialTheme.typography.headlineSmall,
        color = subtitleTextColor,
        fontSize = 21.sp,
        fontWeight = FontWeight.SemiBold
    )
}