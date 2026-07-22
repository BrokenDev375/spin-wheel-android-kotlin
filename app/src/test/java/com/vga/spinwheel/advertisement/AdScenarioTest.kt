package com.vga.spinwheel.advertisement

import java.util.concurrent.TimeUnit
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdScenarioTest {

    @Test
    fun ratioShowsOnlyOnMatchingOpportunity() {
        val scenario = AdScenario(InMemoryAdScenarioStore())

        assertFalse(scenario.shouldShow("inter_home", ratio = 2, maxPerDay = 10))
        assertTrue(scenario.shouldShow("inter_home", ratio = 2, maxPerDay = 10))
        assertFalse(scenario.shouldShow("inter_home", ratio = 2, maxPerDay = 10))
        assertTrue(scenario.shouldShow("inter_home", ratio = 2, maxPerDay = 10))
    }

    @Test
    fun maxPerDayCapsShownAds() {
        val scenario = AdScenario(InMemoryAdScenarioStore())

        assertTrue(scenario.shouldShow("native_inter_home", ratio = 1, maxPerDay = 2))
        assertTrue(scenario.shouldShow("native_inter_home", ratio = 1, maxPerDay = 2))
        assertFalse(scenario.shouldShow("native_inter_home", ratio = 1, maxPerDay = 2))
    }

    @Test
    fun noCountDoesNotConsumeCap() {
        val scenario = AdScenario(InMemoryAdScenarioStore())

        assertTrue(scenario.shouldShow("inter_home", ratio = 1, maxPerDay = 1, noCount = true))
        assertTrue(scenario.shouldShow("inter_home", ratio = 1, maxPerDay = 1))
        assertFalse(scenario.shouldShow("inter_home", ratio = 1, maxPerDay = 1))
    }

    @Test
    fun cleanupRemovesKeysOlderThanSevenDays() {
        val store = InMemoryAdScenarioStore(
            mutableMapOf(
                "ad_scenario|1|old|showCount" to 1,
                "ad_scenario|4|fresh|showCount" to 1,
                "other_key" to 1,
            ),
        )
        val scenario = AdScenario(
            store = store,
            nowMillis = { TimeUnit.DAYS.toMillis(10) },
        )

        scenario.cleanup()

        assertFalse("ad_scenario|1|old|showCount" in store.values)
        assertTrue("ad_scenario|4|fresh|showCount" in store.values)
        assertTrue("other_key" in store.values)
    }

    private class InMemoryAdScenarioStore(
        val values: MutableMap<String, Int> = mutableMapOf(),
    ) : AdScenarioStore {
        override fun allKeys(): Set<String> = values.keys
        override fun getInt(key: String, defaultValue: Int): Int = values[key] ?: defaultValue
        override fun putInt(key: String, value: Int) {
            values[key] = value
        }

        override fun remove(key: String) {
            values.remove(key)
        }
    }
}
