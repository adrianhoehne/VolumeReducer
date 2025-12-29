package dev.bergkaese.volumereducer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bergkaese.volumereducer.feature.controlvolume.AudioController
import dev.bergkaese.volumereducer.feature.controlvolume.Factor
import dev.bergkaese.volumereducer.feature.persistvolume.VolumeStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppModel(val audioController: AudioController = AudioController(),
               val volumeStorage: VolumeStorage,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    var currentVolume by mutableFloatStateOf(1f)
        private set
    val permissionsGranted = MutableStateFlow(false)

    init {
        audioController.init()
        volumeStorage.getPersistedVolume().let {
            currentVolume = it
            audioController.setGlobalVolumeFactor(it)
        }
    }

    fun setVolume(factor: Factor) {
        viewModelScope.launch {
            withContext(dispatcherIO) {
                audioController.setGlobalVolumeFactor(factor)
                volumeStorage.persistCurrentVolume(factor)
            }
            currentVolume = factor
        }
    }

    public override fun onCleared() {
        audioController.release()
    }
}
