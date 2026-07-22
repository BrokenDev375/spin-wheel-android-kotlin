package com.vga.spinwheel.ui.screen.team

import com.vga.spinwheel.data.model.WheelItem
import org.junit.Assert.assertEquals
import org.junit.Test

class TeamRoundRulesTest {

    @Test
    fun createTeams_allowsLastTeamToHaveFewerMembers() {
        val teams = TeamRoundRules.createTeams(
            members = listOf("1", "2", "3", "4", "5"),
            groupSize = 2,
        )

        assertEquals(3, teams.size)
        assertEquals(listOf("1", "2"), teams[0].members)
        assertEquals(listOf("3", "4"), teams[1].members)
        assertEquals(listOf("5"), teams[2].members)
    }

    @Test
    fun memberNames_removesBlankValues() {
        val names = TeamRoundRules.memberNames(
            listOf(
                WheelItem(id = "1", name = " A "),
                WheelItem(id = "2", name = ""),
                WheelItem(id = "3", name = "B"),
            )
        )

        assertEquals(listOf("A", "B"), names)
    }

    @Test
    fun seedShuffle_isStableWhenSeedEnabled() {
        val members = listOf("A", "B", "C", "D")

        val first = TeamRoundRules.shuffledMembers(members, seedEnabled = true, seed = 42L)
        val second = TeamRoundRules.shuffledMembers(members, seedEnabled = true, seed = 42L)

        assertEquals(first, second)
    }
}
