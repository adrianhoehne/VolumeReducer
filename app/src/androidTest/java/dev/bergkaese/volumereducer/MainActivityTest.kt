package dev.bergkaese.volumereducer

import android.app.Application
import android.util.Log
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.platform.app.InstrumentationRegistry
import dev.bergkaese.volumereducer.feature.controlvolume.AudioController
import dev.bergkaese.volumereducer.feature.controlvolume.Priority
import dev.bergkaese.volumereducer.feature.controlvolume.VolumeEqualizer
import dev.bergkaese.volumereducer.feature.persistvolume.VolumeFactor
import dev.bergkaese.volumereducer.feature.persistvolume.VolumeStorage
import dev.bergkaese.volumereducer.ui.AppModel
import dev.bergkaese.volumereducer.ui.MainScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    val bandVolumes = mutableMapOf<Short, Short>()
    val fakeVolumeEqualizer = object : VolumeEqualizer {
        override fun create( sessionId: Int, priority: Priority ) { }
        override fun setBandVolume(bandId: Short, volume: Short) {
            bandVolumes[bandId] = volume
        }
        override fun getNumberOfBands(): Int = 5
        override fun getMinBandVolume(): Short = -1000
        override fun release() { }
    }

    val fakeStorage = object : VolumeStorage {
        var _persistedVolume = 1.0f
        override fun persistCurrentVolume(volumeFactor: VolumeFactor) {
            _persistedVolume = volumeFactor
        }
        override fun getPersistedVolume(): VolumeFactor = _persistedVolume
    }

    lateinit var context: Application


    @Before
    fun before(){
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        val testViewModel = AppModel(AudioController(fakeVolumeEqualizer), fakeStorage)
        composeTestRule.setContent {
            MainScreen(
                isGranted = true,
                volumeIdentifier = "Volume",
                currentVolume = testViewModel.currentVolume,
                onVolumeChange = { factor -> testViewModel.setVolume(factor) },
                finishButtonText = "Stop",
                onFinish = { }
            )
        }
    }

    @Test
    fun testThatSliderReducesTheVolume(){
        composeTestRule.waitUntil(1000){
            composeTestRule.onAllNodesWithTag("slider_volume").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("slider_volume")
            .performSemanticsAction(SemanticsActions.SetProgress) { it(0f) }

        composeTestRule.waitForIdle()
        composeTestRule.waitUntil(1000) { bandVolumes.isNotEmpty() && bandVolumes.keys.size == fakeVolumeEqualizer.getNumberOfBands() }
        assertEquals(0.0f, fakeStorage.getPersistedVolume())
        Log.d("BandVolumes", "$bandVolumes")
        bandVolumes.forEach {
            assertEquals(-1000, it.value)
        }
    }
}