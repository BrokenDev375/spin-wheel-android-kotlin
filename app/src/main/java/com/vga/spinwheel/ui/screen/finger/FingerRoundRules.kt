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
    const val MIN_FINGER_COUNT = 1
    const val MAX_FINGER_COUNT = 5

    fun clampFingerCount(count: Int): Int =
        count.coerceIn(MIN_FINGER_COUNT, MAX_FINGER_COUNT)

    fun normalizeTouches(
        touches: List<FingerTouchInput>,
        width: Float,
        height: Float,
        fingerCount: Int,
    ): List<FingerPoint> {
        if (width <= 0f || height <= 0f) return emptyList()

        val limit = clampFingerCount(fingerCount)
        return touches
            .distinctBy { it.id }
            .take(limit)
            .mapIndexed { index, touch ->
                FingerPoint(
                    id = touch.id,
                    xRatio = (touch.x / width).coerceIn(0.04f, 0.96f),
                    yRatio = (touch.y / height).coerceIn(0.04f, 0.96f),
                    colorIndex = index,
                )
            }
    }

    fun hasRequiredTouches(points: List<FingerPoint>, fingerCount: Int): Boolean =
        points.size >= clampFingerCount(fingerCount)

    fun chooseWinner(
        points: List<FingerPoint>,
        randomIndex: (Int) -> Int = { size -> Random.nextInt(size) },
    ): FingerPoint {
        require(points.isNotEmpty()) { "At least one finger point is required." }
        val index = randomIndex(points.size).coerceIn(0, points.lastIndex)
        return points[index]
    }
}
