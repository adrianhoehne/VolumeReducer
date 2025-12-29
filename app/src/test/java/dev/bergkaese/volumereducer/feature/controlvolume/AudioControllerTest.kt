package dev.bergkaese.volumereducer.feature.controlvolume

import dev.bergkaese.volumereducer.common.TestEqualizer
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AudioControllerTest {

    @Test
    fun testThatCreatePassesIDAndPriorityToEqualizer(){
        val testEqualizer = TestEqualizer()
        val audioController = AudioController(testEqualizer)
        audioController.init(1, priority = Priority.LOW)
        assertEquals(1, testEqualizer.sessionId)
        assertEquals(Priority.LOW, testEqualizer.priority)
    }

    @Test
    fun testThatSetGlobalVolumeSetsAllBandsToTheSameValue(){
        val testEqualizer = TestEqualizer()
        val audioController = AudioController(testEqualizer)
        audioController.setGlobalVolumeFactor(0.5f)
        assertEquals(5, testEqualizer._numberOfBands)
        assertEquals(-1500, testEqualizer._minBandVolume)
        assertEquals((-750).toShort(), testEqualizer._bandVolumes[0])
        assertEquals((-750).toShort(), testEqualizer._bandVolumes[1])
        assertEquals((-750).toShort(), testEqualizer._bandVolumes[2])
        assertEquals((-750).toShort(), testEqualizer._bandVolumes[3])
        assertEquals((-750).toShort(), testEqualizer._bandVolumes[4])
    }

    @Test
    fun testThatCalculationIsCorrect(){
        val testEqualizer = TestEqualizer()
        testEqualizer._numberOfBands = 1
        testEqualizer._minBandVolume = -1000
        val audioController = AudioController(testEqualizer)

        audioController.setGlobalVolumeFactor(0.1f)
        assertEquals((-900).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.2f)
        assertEquals((-800).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.3f)
        assertEquals((-700).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.4f)
        assertEquals((-600).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.5f)
        assertEquals((-500).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.6f)
        assertEquals((-400).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.7f)
        assertEquals((-300).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.8f)
        assertEquals((-200).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(0.9f)
        assertEquals((-100).toShort(), testEqualizer._bandVolumes[0])
        audioController.setGlobalVolumeFactor(1.0f)
        assertEquals((0).toShort(), testEqualizer._bandVolumes[0])
    }

    @Test
    fun testWrongMinBandVolumeDoesNotBreakCalculation(){
        val testEqualizer = TestEqualizer()
        testEqualizer._minBandVolume = 100
        testEqualizer._numberOfBands = 1
        val audioController = AudioController(testEqualizer)
        audioController.setGlobalVolumeFactor(0.5f)
        assertEquals(150.toShort(), testEqualizer._bandVolumes[0])
    }
}