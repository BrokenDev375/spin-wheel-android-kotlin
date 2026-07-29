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
    const val MIN_TEAM_COUNT = 2
    const val MAX_TEAM_COUNT = 10
    const val MIN_DURATION_SECONDS = 2
    const val MAX_DURATION_SECONDS = 10

    fun clampTeamCount(value: Int): Int =
        value.coerceIn(MIN_TEAM_COUNT, MAX_TEAM_COUNT)

    fun clampDuration(value: Int): Int =
        value.coerceIn(MIN_DURATION_SECONDS, MAX_DURATION_SECONDS)

    fun memberNames(items: List<WheelItem>): List<String> =
        items.mapNotNull { item ->
            item.name.trim().takeIf { it.isNotEmpty() }
        }

    fun createTeams(
        members: List<String>,
        teamCount: Int,
    ): List<TeamGroup> {
        val normalizedTeamCount = clampTeamCount(teamCount)
        val validMembers = members.map { it.trim() }.filter { it.isNotEmpty() }
        if (validMembers.isEmpty()) return emptyList()

        val actualTeamCount = normalizedTeamCount.coerceAtMost(validMembers.size)
        val teams = List(actualTeamCount) { mutableListOf<String>() }
        
        validMembers.forEachIndexed { index, member ->
            teams[index % actualTeamCount].add(member)
        }

        return teams.mapIndexed { index, names ->
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


