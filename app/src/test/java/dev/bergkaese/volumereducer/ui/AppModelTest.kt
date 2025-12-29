package dev.bergkaese.volumereducer.ui

import dev.bergkaese.volumereducer.common.TestEqualizer
import dev.bergkaese.volumereducer.common.TestVolumeStorage
import dev.bergkaese.volumereducer.feature.controlvolume.AudioController
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppModelTest {
    private val testDispatcher = StandardTestDispatcher()
    lateinit var testVolumeStorage: TestVolumeStorage
    lateinit var testVolumeEqualizer: TestEqualizer


    @Before
    fun before(){
        Dispatchers.setMain(testDispatcher)
        testVolumeEqualizer = TestEqualizer()
        testVolumeEqualizer._minBandVolume = -1000
        testVolumeStorage = TestVolumeStorage()
    }

    @After
    fun after(){
        Dispatchers.resetMain()
    }

    @Test
    fun testThatSetVolumeCallsAudioController() = runTest {
        val appModel = AppModel(AudioController(testVolumeEqualizer), testVolumeStorage, testDispatcher)
        assertEquals("Initial Value is wrong",1.0f, appModel.currentVolume)

        appModel.setVolume(0.5f)
        advanceUntilIdle()
        // AudioController calls VolumeEqualizer, so check is done in VolumeEqualizer
        assertEquals("Band Volume in Equalizer should be 0.5", (-500).toShort(), testVolumeEqualizer._bandVolumes[0])
    }

    @Test
    fun testThatSetVolumePersistsTheVolume() = runTest {
        val appModel = AppModel(AudioController(testVolumeEqualizer), testVolumeStorage, testDispatcher)
        assertEquals("Initial Value is wrong",1.0f, appModel.currentVolume)

        appModel.setVolume(0.5f)
        advanceUntilIdle()

        assertEquals("Current Volume in VolumeStorage should be 0.5", 0.5f, testVolumeStorage.getPersistedVolume())
    }

    @Test
    fun testThatSetVolumeUpdatesCurrentVolume() = runTest {
        val appModel = AppModel(AudioController(testVolumeEqualizer), testVolumeStorage, testDispatcher)
        assertEquals(1.0f, appModel.currentVolume)

        appModel.setVolume(0.5f)
        advanceUntilIdle()
        assertEquals("Current Volume in AppModel should be 0.5", 0.5f, appModel.currentVolume)
    }

    @Test
    fun testThatOnClearedCallsRelease() = runTest {
        val appModel = AppModel(AudioController(testVolumeEqualizer), testVolumeStorage, testDispatcher)
        appModel.onCleared()
        assertEquals(true, testVolumeEqualizer.released)
    }
}