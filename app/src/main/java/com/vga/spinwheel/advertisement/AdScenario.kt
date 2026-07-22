package com.vga.spinwheel.advertisement

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class AdScenario(
    private val store: AdScenarioStore,
    private val nowMillis: () -> Long = System::currentTimeMillis,
) {

    fun shouldShow(
        placement: String,
        ratio: Int,
        maxPerDay: Int,
        noCount: Boolean = false,
    ): Boolean {
        cleanup()
        val day = currentDay()
        val safeRatio = AdScenarioRules.sanitizeRatio(ratio)
        val opportunityKey = key(day, placement, KIND_OPPORTUNITY)
        val shownKey = key(day, placement, KIND_SHOWN)
        val currentOpportunityCount = store.getInt(opportunityKey, 0)
        val nextOpportunityCount = if (noCount) {
            currentOpportunityCount
        } else {
            currentOpportunityCount + 1
        }
        val shownCount = store.getInt(shownKey, 0)

        if (!noCount) {
            store.putInt(opportunityKey, nextOpportunityCount)
        }

        val shouldShow = AdScenarioRules.shouldShow(
            opportunityCount = nextOpportunityCount,
            shownCount = shownCount,
            ratio = safeRatio,
            maxPerDay = maxPerDay,
        )
        if (shouldShow && !noCount) {
            store.putInt(shownKey, shownCount + 1)
        }
        return shouldShow
    }

    fun cleanup() {
        val oldestDayToKeep = currentDay() - RETENTION_DAYS
        store.allKeys()
            .filter { it.startsWith(KEY_PREFIX) }
            .filter { key -> dayFromKey(key)?.let { it < oldestDayToKeep } == true }
            .forEach(store::remove)
    }

    private fun currentDay(): Long = TimeUnit.MILLISECONDS.toDays(nowMillis())

    private fun key(
        day: Long,
        placement: String,
        kind: String,
    ): String = "$KEY_PREFIX$day|$placement|$kind"

    private fun dayFromKey(key: String): Long? =
        key.removePrefix(KEY_PREFIX)
            .substringBefore('|')
            .toLongOrNull()

    companion object {
        private const val PREFERENCES_NAME = "ad_scenario"
        private const val KEY_PREFIX = "ad_scenario|"
        private const val KIND_OPPORTUNITY = "showCount"
        private const val KIND_SHOWN = "showCountAd"
        private const val RETENTION_DAYS = 7L

        fun from(context: Context): AdScenario =
            AdScenario(
                SharedPreferencesAdScenarioStore(
                    context.applicationContext.getSharedPreferences(
                        PREFERENCES_NAME,
                        Context.MODE_PRIVATE,
                    ),
                ),
            )
    }
}

object AdScenarioRules {

    fun shouldShow(
        opportunityCount: Int,
        shownCount: Int,
        ratio: Int,
        maxPerDay: Int,
    ): Boolean {
        if (maxPerDay <= 0) return false
        val safeRatio = sanitizeRatio(ratio)
        val safeOpportunityCount = opportunityCount.coerceAtLeast(0)
        val safeShownCount = shownCount.coerceAtLeast(0)
        return safeOpportunityCount % safeRatio == 0 && safeShownCount < maxPerDay
    }

    fun sanitizeRatio(ratio: Int): Int = if (ratio <= 0) 1 else ratio
}

interface AdScenarioStore {
    fun allKeys(): Set<String>
    fun getInt(key: String, defaultValue: Int): Int
    fun putInt(key: String, value: Int)
    fun remove(key: String)
}

class SharedPreferencesAdScenarioStore(
    private val preferences: SharedPreferences,
) : AdScenarioStore {

    override fun allKeys(): Set<String> = preferences.all.keys

    override fun getInt(key: String, defaultValue: Int): Int =
        preferences.getInt(key, defaultValue)

    override fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    override fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }
}
