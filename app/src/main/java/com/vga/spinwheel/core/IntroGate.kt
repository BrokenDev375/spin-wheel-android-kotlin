package com.vga.spinwheel.core

object IntroGate {

    const val DEFAULT_COUNT_APP_OPEN = 3
    const val DEFAULT_ORGANIC_NUMBER_NOT_GUIDE = 3

    fun shouldGoHome(
        launchNumber: Int,
        isAdsCampaign: Boolean,
        countAppOpen: Int,
        organicNumberNotGuide: Int,
    ): Boolean {
        val safeLaunchNumber = launchNumber.coerceAtLeast(1)
        val safeCountAppOpen = sanitizeCountAppOpen(countAppOpen)
        val safeOrganicNumberNotGuide = sanitizeOrganicNumberNotGuide(organicNumberNotGuide)

        return if (isAdsCampaign) {
            safeLaunchNumber >= safeCountAppOpen
        } else {
            safeLaunchNumber < safeOrganicNumberNotGuide ||
                safeLaunchNumber >= safeCountAppOpen + safeOrganicNumberNotGuide
        }
    }

    fun sanitizeCountAppOpen(value: Int): Int =
        if (value > 0) value else DEFAULT_COUNT_APP_OPEN

    fun sanitizeOrganicNumberNotGuide(value: Int): Int =
        value.coerceAtLeast(0)
}
