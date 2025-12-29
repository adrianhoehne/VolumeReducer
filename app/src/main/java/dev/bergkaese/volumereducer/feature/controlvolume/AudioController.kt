package dev.bergkaese.volumereducer.feature.controlvolume

import kotlin.math.abs

typealias Factor = Float

class AudioController(val volumeEqualizer: VolumeEqualizer = VolumeEqualizerImpl()) {

    fun init(sessionId: Int = 0, priority: Priority = Priority.MEDIUM) {
        volumeEqualizer.create(sessionId, priority)
    }

    fun setGlobalVolumeFactor(factor: Factor) {
        val minLevel = volumeEqualizer.getMinBandVolume()
        val bands = volumeEqualizer.getNumberOfBands()
        for (bandId in 0 until bands) {
            val desiredLevel = factor.toBandLevel(minLevel)
            volumeEqualizer.setBandVolume(bandId.toShort(), desiredLevel)
        }
    }

    fun release() {
        volumeEqualizer.release()
    }
}

private fun Factor.toBandLevel(minLevel: Short): Short {
    val desiredLevel = abs(minLevel.toInt()) * this + minLevel
    return desiredLevel.toInt().toShort()
}

enum class Priority {
    LOW, MEDIUM, HIGH
}