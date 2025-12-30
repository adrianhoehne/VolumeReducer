package dev.bergkaese.volumereducer.feature.persistvolume

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class VolumeStorageTest {
    private lateinit var context: Context
    private lateinit var volumeStorage: VolumeStorage

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit()
        volumeStorage = VolumeStorageBySharedPrefs(context)
    }

    @After
    fun after(){
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun testThatPersistedVolumeIsReturned() {
        val volume = 0.5f
        volumeStorage.persistCurrentVolume(volume)
        assertEquals(volume, volumeStorage.getPersistedVolume())
    }

    @Test
    fun testThatPersistedVolumeIsReturnedWithDefault1f() {
        assertEquals(1f, volumeStorage.getPersistedVolume())
    }
}