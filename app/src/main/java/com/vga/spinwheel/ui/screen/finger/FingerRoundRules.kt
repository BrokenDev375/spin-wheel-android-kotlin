package com.vga.spinwheel.ui.screen.finger

import kotlin.random.Random

data class FingerTouchInput(
    val id: Long,
    val x: Float,
    val y: Float,
)

data class FingerPoint(
    val id: Long,
    val xRatio: Float,
    val yRatio: Float,
    val colorIndex: Int,
)

object FingerRoundRules {
    const val MIN_WINNER_COUNT = 1
    const val MAX_WINNER_COUNT = 5

    fun clampWinnerCount(count: Int): Int =
        count.coerceIn(MIN_WINNER_COUNT, MAX_WINNER_COUNT)

    fun normalizeTouches(
        touches: List<FingerTouchInput>,
        width: Float,
        height: Float,
    ): List<FingerPoint> {
        if (width <= 0f || height <= 0f) return emptyList()

        return touches
            .distinctBy { it.id }
            .mapIndexed { index, touch ->
                FingerPoint(
                    id = touch.id,
                    xRatio = (touch.x / width).coerceIn(0.04f, 0.96f),
                    yRatio = (touch.y / height).coerceIn(0.04f, 0.96f),
                    colorIndex = index,
                )
            }
    }

    fun hasRequiredTouches(points: List<FingerPoint>, winnerCount: Int): Boolean =
        points.size >= clampWinnerCount(winnerCount)

    fun chooseWinner(
        points: List<FingerPoint>,
        randomIndex: (Int) -> Int = { size -> Random.nextInt(size) },
    ): FingerPoint {
        require(points.isNotEmpty()) { "At least one finger point is required." }
        val index = randomIndex(points.size).coerceIn(0, points.lastIndex)
        return points[index]
    }

    fun chooseWinners(
        points: List<FingerPoint>,
        winnerCount: Int,
    ): List<FingerPoint> {
        require(points.isNotEmpty()) { "At least one finger point is required." }
        val count = clampWinnerCount(winnerCount).coerceAtMost(points.size)
        return points.shuffled().take(count)
    }
}
