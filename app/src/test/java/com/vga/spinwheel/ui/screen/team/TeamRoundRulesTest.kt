package com.vga.spinwheel.ui.screen.team

import com.vga.spinwheel.data.model.WheelItem
import org.junit.Assert.assertEquals
import org.junit.Test

class TeamRoundRulesTest {

    @Test
    fun createTeams_distributesMembersAcrossRequestedTeams() {
        val teams = TeamRoundRules.createTeams(
            members = listOf("1", "2", "3", "4", "5"),
            teamCount = 2,
        )

        assertEquals(2, teams.size)
        assertEquals(listOf("1", "3", "5"), teams[0].members)
        assertEquals(listOf("2", "4"), teams[1].members)
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
    fun shuffledMembers_keepsAllMembers() {
        val members = listOf("A", "B", "C", "D")

        val shuffled = TeamRoundRules.shuffledMembers(members)

        assertEquals(members.sorted(), shuffled.sorted())
    }
}
