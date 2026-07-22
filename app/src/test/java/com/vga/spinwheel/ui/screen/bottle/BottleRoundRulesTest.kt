package com.vga.spinwheel.ui.screen.bottle

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BottleRoundRulesTest {

    @Test
    fun clampDurationSeconds_keepsValueWithinSupportedRange() {
        assertEquals(1, BottleRoundRules.clampDurationSeconds(-4))
        assertEquals(2, BottleRoundRules.clampDurationSeconds(2))
        assertEquals(15, BottleRoundRules.clampDurationSeconds(40))
    }

    @Test
    fun clampStyleIndex_keepsValueWithinAvailableStyles() {
        assertEquals(0, BottleRoundRules.clampStyleIndex(-1, styleCount = 6))
        assertEquals(4, BottleRoundRules.clampStyleIndex(4, styleCount = 6))
        assertEquals(5, BottleRoundRules.clampStyleIndex(20, styleCount = 6))
        assertEquals(0, BottleRoundRules.clampStyleIndex(3, styleCount = 0))
    }

    @Test
    fun randomFinalAngle_staysInOneTurnRange() {
        val random = Random(11)

        repeat(100) {
            val angle = BottleRoundRules.randomFinalAngle(random)
            assertTrue(angle in 0..359)
        }
    }
}
