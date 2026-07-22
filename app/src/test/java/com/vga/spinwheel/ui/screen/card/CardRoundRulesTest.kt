package com.vga.spinwheel.ui.screen.card

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardRoundRulesTest {

    @Test
    fun clampDurationSeconds_keepsValueWithinSupportedRange() {
        assertEquals(1, CardRoundRules.clampDurationSeconds(-2))
        assertEquals(2, CardRoundRules.clampDurationSeconds(2))
        assertEquals(15, CardRoundRules.clampDurationSeconds(42))
    }

    @Test
    fun clampTotalCards_keepsValueWithinSupportedRange() {
        assertEquals(2, CardRoundRules.clampTotalCards(-1))
        assertEquals(4, CardRoundRules.clampTotalCards(4))
        assertEquals(12, CardRoundRules.clampTotalCards(40))
    }

    @Test
    fun clampWinners_keepsAtLeastOneLoser() {
        assertEquals(1, CardRoundRules.clampWinners(-4, totalCards = 4))
        assertEquals(2, CardRoundRules.clampWinners(2, totalCards = 4))
        assertEquals(3, CardRoundRules.clampWinners(8, totalCards = 4))
    }

    @Test
    fun clampThemeIndex_keepsValueWithinAvailableThemes() {
        assertEquals(0, CardRoundRules.clampThemeIndex(-1, themeCount = 4))
        assertEquals(2, CardRoundRules.clampThemeIndex(2, themeCount = 4))
        assertEquals(3, CardRoundRules.clampThemeIndex(20, themeCount = 4))
        assertEquals(0, CardRoundRules.clampThemeIndex(2, themeCount = 0))
    }

    @Test
    fun randomWinnerFlags_createsExactWinnerCount() {
        val flags = CardRoundRules.randomWinnerFlags(
            totalCards = 12,
            winners = 5,
            random = Random(13),
        )

        assertEquals(12, flags.size)
        assertEquals(5, flags.count { it })
        assertTrue(flags.any { !it })
    }
}
