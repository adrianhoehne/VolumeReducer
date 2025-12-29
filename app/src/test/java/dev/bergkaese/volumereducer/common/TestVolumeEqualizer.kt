package dev.bergkaese.volumereducer.common

import dev.bergkaese.volumereducer.feature.controlvolume.Priority
import dev.bergkaese.volumereducer.feature.controlvolume.VolumeEqualizer

class TestEqualizer: VolumeEqualizer {
    var sessionId: Int? = null
    var priority: Priority? = null
    val _bandVolumes = mutableMapOf<Short, Short>()
    var _numberOfBands = 5
    var _minBandVolume: Short = -1500
    var released = false

    override fun create(
        sessionId: Int,
        priority: Priority
    ) {
        this.sessionId = sessionId
        this.priority = priority
    }

    override fun setBandVolume(bandId: Short, volume: Short) {
        _bandVolumes[bandId] = volume
    }

    override fun getNumberOfBands(): Int = _numberOfBands

    override fun getMinBandVolume(): Short = _minBandVolume

    override fun release() {
        released = true
    }
}