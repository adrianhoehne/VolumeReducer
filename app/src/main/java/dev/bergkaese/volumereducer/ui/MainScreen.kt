package dev.bergkaese.volumereducer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.bergkaese.volumereducer.ui.theme.MainTheme

@Composable
fun MainScreen(
    isGranted: Boolean,
    volumeIdentifier: String,
    currentVolume: Float,
    onVolumeChange: (Float) -> Unit,
    finishButtonText: String,
    onFinish: () -> Unit)
{
    MainTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(isGranted){
                    VolumeSlider(
                        identifier = volumeIdentifier,
                        value = currentVolume,
                        onValueChange = { factor -> onVolumeChange(factor) },
                    )
                }
                Button(
                    onClick = onFinish,
                    modifier = Modifier.testTag("btn_close").padding(top = 16.dp)
                ) {
                    Text(finishButtonText)
                }
            }
        }
    }
}