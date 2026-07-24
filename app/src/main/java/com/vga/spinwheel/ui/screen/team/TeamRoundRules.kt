package com.vga.spinwheel.ui.screen.team

import com.vga.spinwheel.data.model.WheelItem
import kotlin.random.Random

data class TeamGroup(
    val index: Int,
    val members: List<String>,
) {
    val title: String = "Team $index"
}

object TeamRoundRules {
    const val MIN_GROUP_SIZE = 1
    const val MAX_GROUP_SIZE = 10
    const val MIN_DURATION_SECONDS = 2
    const val MAX_DURATION_SECONDS = 10

    fun clampGroupSize(value: Int): Int =
        value.coerceIn(MIN_GROUP_SIZE, MAX_GROUP_SIZE)

    fun clampDuration(value: Int): Int =
        value.coerceIn(MIN_DURATION_SECONDS, MAX_DURATION_SECONDS)

    fun memberNames(items: List<WheelItem>): List<String> =
        items.mapNotNull { item ->
            item.name.trim().takeIf { it.isNotEmpty() }
        }

    fun createTeams(
        members: List<String>,
        groupSize: Int,
    ): List<TeamGroup> {
        val normalizedGroupSize = clampGroupSize(groupSize)
        return members
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .chunked(normalizedGroupSize)
            .mapIndexed { index, names ->
                TeamGroup(index = index + 1, members = names)
            }
    }

    fun shuffledMembers(
        members: List<String>,
        seedEnabled: Boolean,
        seed: Long,
    ): List<String> =
        if (seedEnabled) {
            members.shuffled(Random(seed))
        } else {
            members.shuffled()
        }
}
