package com.vga.spinwheel.ui.screen.card

import kotlin.random.Random

object CardRoundRules {
    const val MIN_DURATION_SECONDS = 1
    const val MAX_DURATION_SECONDS = 15
    const val MIN_TOTAL_CARDS = 2
    const val MAX_TOTAL_CARDS = 12

    fun clampDurationSeconds(value: Int): Int =
        value.coerceIn(MIN_DURATION_SECONDS, MAX_DURATION_SECONDS)

    fun clampTotalCards(value: Int): Int =
        value.coerceIn(MIN_TOTAL_CARDS, MAX_TOTAL_CARDS)

    fun clampWinners(value: Int, totalCards: Int): Int {
        val total = clampTotalCards(totalCards)
        return value.coerceIn(1, total - 1)
    }

    fun clampThemeIndex(value: Int, themeCount: Int): Int =
        if (themeCount <= 0) 0 else value.coerceIn(0, themeCount - 1)

    fun randomWinnerFlags(
        totalCards: Int,
        winners: Int,
        random: Random = Random.Default,
    ): List<Boolean> {
        val total = clampTotalCards(totalCards)
        val winnerCount = clampWinners(winners, total)
        val selected = mutableSetOf<Int>()

        while (selected.size < winnerCount) {
            selected += random.nextInt(total)
        }

        return List(total) { index -> index in selected }
    }
}
