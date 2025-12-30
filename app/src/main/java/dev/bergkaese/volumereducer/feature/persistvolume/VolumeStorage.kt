package dev.bergkaese.volumereducer.feature.persistvolume

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit


typealias VolumeFactor = Float
interface VolumeStorage {
    fun persistCurrentVolume(volumeFactor: VolumeFactor)
    fun getPersistedVolume(): VolumeFactor
}

const val PREFERENCE_NAME = "current_volume"
const val PREFERENCE_KEY = "key_volume"

class VolumeStorageBySharedPrefs(val context: Context): VolumeStorage {
    override fun persistCurrentVolume(volumeFactor: VolumeFactor) {
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
            .edit { putFloat(PREFERENCE_KEY, volumeFactor) }
    }

    override fun getPersistedVolume(): VolumeFactor {
        return context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
            .getFloat(PREFERENCE_KEY, 1f)
    }
}