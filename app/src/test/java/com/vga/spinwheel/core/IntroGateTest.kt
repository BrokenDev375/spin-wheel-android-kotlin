package com.vga.spinwheel.core

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntroGateTest {

    @Test
    fun adsCampaignShowsIntroBeforeCountThreshold() {
        assertFalse(IntroGate.shouldGoHome(1, isAdsCampaign = true, 3, 3))
        assertFalse(IntroGate.shouldGoHome(2, isAdsCampaign = true, 3, 3))
        assertTrue(IntroGate.shouldGoHome(3, isAdsCampaign = true, 3, 3))
        assertTrue(IntroGate.shouldGoHome(6, isAdsCampaign = true, 3, 3))
    }

    @Test
    fun organicShowsHomeThenIntroThenHomeAgain() {
        assertTrue(IntroGate.shouldGoHome(1, isAdsCampaign = false, 3, 3))
        assertTrue(IntroGate.shouldGoHome(2, isAdsCampaign = false, 3, 3))
        assertFalse(IntroGate.shouldGoHome(3, isAdsCampaign = false, 3, 3))
        assertFalse(IntroGate.shouldGoHome(5, isAdsCampaign = false, 3, 3))
        assertTrue(IntroGate.shouldGoHome(6, isAdsCampaign = false, 3, 3))
    }

    @Test
    fun invalidRemoteValuesFallBackSafely() {
        assertTrue(IntroGate.shouldGoHome(3, isAdsCampaign = true, 0, -5))
        assertFalse(IntroGate.shouldGoHome(1, isAdsCampaign = false, 3, -5))
        assertTrue(IntroGate.shouldGoHome(3, isAdsCampaign = false, 3, -5))
    }
}
