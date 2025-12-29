package dev.bergkaese.volumereducer.feature.controlvolume

import android.media.audiofx.Equalizer
import android.util.Log
import kotlinx.coroutines.CancellationException

interface VolumeEqualizer {
    fun create(sessionId: Int = 0, priority: Priority = Priority.MEDIUM)
    fun setBandVolume(bandId: Short, volume: Short)
    fun getNumberOfBands(): Int
    fun getMinBandVolume(): Short
    fun release()
}

class VolumeEqualizerImpl : VolumeEqualizer {
    private lateinit var _equalizer: Equalizer

    override fun create(
        sessionId: Int,
        priority: Priority
    ) {
        try {
            val priorityNumber = when (priority) {
                Priority.LOW -> -1
                Priority.MEDIUM -> 0
                Priority.HIGH -> 1
            }

            _equalizer = Equalizer(priorityNumber, sessionId)
            _equalizer.enabled = true
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            Log.e("VolumeEqualizer", "Failed to initialize VolumeEqualizer", e)
            throw e
        }
    }

    override fun getMinBandVolume(): Short {
        return _equalizer.bandLevelRange[0]
    }

    override fun getNumberOfBands(): Int = _equalizer.numberOfBands.toInt()

    override fun setBandVolume(bandId: Short, volume: Short) {
        _equalizer.setBandLevel(bandId, volume)
    }

    override fun release() {
        _equalizer.release()
    }
}