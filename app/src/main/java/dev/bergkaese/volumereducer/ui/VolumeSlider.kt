package dev.bergkaese.volumereducer.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun VolumeSlider(identifier: String, value: Float, onValueChange: (Float) -> Unit) {
    Text(identifier, style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(16.dp))
    Slider(
        value = value,
        onValueChange = {
            onValueChange.invoke(it)
        },
        valueRange = 0.0f..1.0f,
        steps = 9,
        modifier = Modifier.testTag("slider_volume").fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text("${(value * 100).toInt()} %")
}