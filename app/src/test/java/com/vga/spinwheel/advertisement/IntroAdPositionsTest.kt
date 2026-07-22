package com.vga.spinwheel.advertisement

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntroAdPositionsTest {

    @Test
    fun inlineAndModalPositionsAreIndependent() {
        val positions = IntroAdPositions(
            inlineSlides = setOf(1, 3),
            modalAfterSlides = setOf(2, 4),
        )

        assertTrue(positions.hasInline(1))
        assertFalse(positions.hasInline(2))
        assertTrue(positions.hasModalAfter(2))
        assertFalse(positions.hasModalAfter(3))
    }
}
