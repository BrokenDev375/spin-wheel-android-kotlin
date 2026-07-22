package com.vga.spinwheel

import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.ui.screen.wheel.WheelFormRules
import org.junit.Assert.assertEquals
import org.junit.Test

class WheelFormRulesTest {

    @Test
    fun firstTwoItems_doNotDecreaseBelowOne() {
        val items = defaultItems()

        val updated = WheelFormRules.changePriority(
            items = items,
            itemId = "item-1",
            delta = -1,
        )

        assertEquals(3, updated.size)
        assertEquals(1, updated[0].priority)
    }

    @Test
    fun extraItem_isRemovedWhenDecreasedFromOneToZero() {
        val items = defaultItems()

        val updated = WheelFormRules.changePriority(
            items = items,
            itemId = "item-3",
            delta = -1,
        )

        assertEquals(listOf("item-1", "item-2"), updated.map { it.id })
    }

    @Test
    fun priorityStillClampsAtMaximum() {
        val items = listOf(
            WheelItem(id = "item-1", name = "A", priority = 10),
            WheelItem(id = "item-2", name = "B", priority = 1),
        )

        val updated = WheelFormRules.changePriority(
            items = items,
            itemId = "item-1",
            delta = 1,
        )

        assertEquals(10, updated[0].priority)
    }

    private fun defaultItems(): List<WheelItem> = listOf(
        WheelItem(id = "item-1", name = "A", priority = 1),
        WheelItem(id = "item-2", name = "B", priority = 1),
        WheelItem(id = "item-3", name = "C", priority = 1),
    )
}
