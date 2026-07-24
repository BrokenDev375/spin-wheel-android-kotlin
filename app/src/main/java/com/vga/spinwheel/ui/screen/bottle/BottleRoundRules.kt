package com.vga.spinwheel.ui.screen.bottle

import kotlin.random.Random

object BottleRoundRules {
    const val MIN_DURATION_SECONDS = 2
    const val MAX_DURATION_SECONDS = 10
    const val ANGLE_RANGE_DEGREES = 360

    fun clampDurationSeconds(value: Int): Int =
        value.coerceIn(MIN_DURATION_SECONDS, MAX_DURATION_SECONDS)

    fun clampStyleIndex(value: Int, styleCount: Int): Int {
        if (styleCount <= 0) return 0
        return value.coerceIn(0, styleCount - 1)
    }

    fun randomFinalAngle(random: Random = Random.Default): Int =
        random.nextInt(ANGLE_RANGE_DEGREES)
}
