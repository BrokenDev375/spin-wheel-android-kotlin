package com.vga.spinwheel.ui.screen.finger

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FingerRoundRulesTest {

    @Test
    fun clampFingerCount_keepsSelectionWithinSupportedRange() {
        assertEquals(1, FingerRoundRules.clampFingerCount(-5))
        assertEquals(3, FingerRoundRules.clampFingerCount(3))
        assertEquals(5, FingerRoundRules.clampFingerCount(99))
    }

    @Test
    fun normalizeTouches_capsToSelectedFingerCountAndBoundsPosition() {
        val points = FingerRoundRules.normalizeTouches(
            touches = listOf(
                FingerTouchInput(id = 1L, x = -10f, y = 50f),
                FingerTouchInput(id = 2L, x = 40f, y = 220f),
                FingerTouchInput(id = 3L, x = 80f, y = 100f),
            ),
            width = 100f,
            height = 200f,
            fingerCount = 2,
        )

        assertEquals(2, points.size)
        assertEquals(0.04f, points[0].xRatio)
        assertEquals(0.25f, points[0].yRatio)
        assertEquals(0.40f, points[1].xRatio)
        assertEquals(0.96f, points[1].yRatio)
    }

    @Test
    fun chooseWinner_usesProvidedRandomIndex() {
        val points = listOf(
            FingerPoint(id = 10L, xRatio = 0.2f, yRatio = 0.3f, colorIndex = 0),
            FingerPoint(id = 20L, xRatio = 0.5f, yRatio = 0.6f, colorIndex = 1),
        )

        val winner = FingerRoundRules.chooseWinner(points) { 1 }

        assertEquals(20L, winner.id)
        assertTrue(FingerRoundRules.hasRequiredTouches(points, fingerCount = 2))
    }
}
