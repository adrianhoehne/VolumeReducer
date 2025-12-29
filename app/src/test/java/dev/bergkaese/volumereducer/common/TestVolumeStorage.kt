package dev.bergkaese.volumereducer.common

import dev.bergkaese.volumereducer.feature.persistvolume.VolumeFactor
import dev.bergkaese.volumereducer.feature.persistvolume.VolumeStorage

class TestVolumeStorage : VolumeStorage {
    private var _persistedVolume = 1f

    override fun persistCurrentVolume(volumeFactor: VolumeFactor) {
        _persistedVolume = volumeFactor
    }

    override fun getPersistedVolume(): VolumeFactor {
        return _persistedVolume
    }
}