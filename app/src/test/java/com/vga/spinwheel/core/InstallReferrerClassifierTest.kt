package com.vga.spinwheel.core

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstallReferrerClassifierTest {

    @Test
    fun gclidIsAdsCampaign() {
        assertTrue(
            InstallReferrerClassifier.isAdsCampaign(
                "utm_source=google&utm_medium=organic&gclid=abc123",
            ),
        )
    }

    @Test
    fun missingMediumIsOrganic() {
        assertFalse(InstallReferrerClassifier.isAdsCampaign("utm_source=google-play"))
    }

    @Test
    fun organicMediumIsOrganic() {
        assertFalse(
            InstallReferrerClassifier.isAdsCampaign(
                "utm_source=google-play&utm_medium=organic",
            ),
        )
    }

    @Test
    fun notSetMediumIsOrganic() {
        assertFalse(
            InstallReferrerClassifier.isAdsCampaign(
                "utm_source=google-play&utm_medium=(not%20set)",
            ),
        )
    }

    @Test
    fun paidMediumIsAdsCampaign() {
        assertTrue(
            InstallReferrerClassifier.isAdsCampaign(
                "utm_source=newsletter&utm_medium=cpc",
            ),
        )
    }

    @Test
    fun unresolvedReferrerFallsBackToAdsCampaign() {
        assertTrue(InstallReferrerClassifier.isAdsCampaign(null))
    }
}
