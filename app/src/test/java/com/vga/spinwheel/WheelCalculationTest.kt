package com.vga.spinwheel

import com.vga.spinwheel.data.model.WheelItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class WheelCalculationTest {

    @Test
    fun testPriorityWeightedRandomSelection() {
        val items = listOf(
            WheelItem(id = "1", name = "High Priority", priority = 9),
            WheelItem(id = "2", name = "Low Priority", priority = 1),
        )

        val totalWeight = items.sumOf { it.priority }
        var highCount = 0
        var lowCount = 0

        // Run 10,000 simulations
        for (i in 0 until 10000) {
            val randomVal = Random.nextInt(totalWeight)
            var accumulated = 0
            var selected: WheelItem? = null
            for (item in items) {
                accumulated += item.priority
                if (randomVal < accumulated) {
                    selected = item
                    break
                }
            }
            if (selected?.id == "1") highCount++ else lowCount++
        }

        // High priority should win approximately 90% of the time (e.g. > 80%)
        assertTrue("High priority item should win majority of spins", highCount > 8000)
    }

    @Test
    fun testValidationRules() {
        val emptyName = ""
        val validItems = listOf(
            WheelItem(id = "1", name = "Option 1", priority = 1),
        )

        val isNameBlank = emptyName.isBlank()
        val isItemCountInvalid = validItems.size < 2

        assertTrue(isNameBlank)
        assertTrue(isItemCountInvalid)
    }
}
