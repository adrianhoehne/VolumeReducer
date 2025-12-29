package dev.bergkaese.volumereducer.feature.persistvolume

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.Test

class VolumeStorageTest {

    @Test
    fun testThatPersistedVolumeIsReturned() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val volume = 0.5f
        val volumeStorage = VolumeStorageBySharedPrefs(context)
        volumeStorage.persistCurrentVolume(volume)
        assertEquals(volume, volumeStorage.getPersistedVolume())
    }

    @Test
    fun testThatPersistedVolumeIsReturnedWithDefault1f() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val volumeStorage = VolumeStorageBySharedPrefs(context)
        assertEquals(1f, volumeStorage.getPersistedVolume())
    }
}