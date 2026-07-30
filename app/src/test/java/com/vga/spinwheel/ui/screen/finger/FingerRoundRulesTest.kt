package com.vga.spinwheel.ui.screen.finger

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FingerRoundRulesTest {

    @Test
    fun clampFingerCount_keepsSelectionWithinSupportedRange() {
        assertEquals(1, FingerRoundRules.clampWinnerCount(-5))
        assertEquals(3, FingerRoundRules.clampWinnerCount(3))
        assertEquals(5, FingerRoundRules.clampWinnerCount(99))
    }

    @Test
    fun normalizeTouches_boundsPositionAndKeepsDistinctTouches() {
        val points = FingerRoundRules.normalizeTouches(
            touches = listOf(
                FingerTouchInput(id = 1L, x = -10f, y = 50f),
                FingerTouchInput(id = 2L, x = 40f, y = 220f),
                FingerTouchInput(id = 3L, x = 80f, y = 100f),
                FingerTouchInput(id = 2L, x = 55f, y = 120f),
            ),
            width = 100f,
            height = 200f,
        )

        assertEquals(3, points.size)
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
        assertTrue(FingerRoundRules.hasRequiredTouches(points, winnerCount = 2))
    }
}
