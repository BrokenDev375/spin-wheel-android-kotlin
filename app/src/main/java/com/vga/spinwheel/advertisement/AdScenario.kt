package com.vga.spinwheel.advertisement

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AdScenario {

    private const val PREFS = "ad_scenario"
    private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val DATE_RE = Regex("""\d{4}-\d{2}-\d{2}""")

    fun shouldShow(
        context: Context,
        type: String,
        ratio: Int,
        maxPerDay: Int,
        noCount: Boolean = false,
    ): Boolean {
        val preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val today = dateFmt.format(Date())
        cleanupOld(preferences, today)
        val showKey = "ad_show_count_${today}_$type"
        val showKeyAd = "ad_show_countad_${today}_$type"

        var showCount = preferences.getInt(showKey, 0)
        var showCountAd = preferences.getInt(showKeyAd, 0)

        val safeRatio = if (ratio <= 0) 1 else ratio
        var shouldShow = showCount % safeRatio == 0
        if (showCountAd >= maxPerDay) shouldShow = false

        if (!noCount) {
            showCount++
            val editor = preferences.edit().putInt(showKey, showCount)
            if (shouldShow) {
                showCountAd++
                editor.putInt(showKeyAd, showCountAd)
            }
            editor.apply()
        }
        return shouldShow
    }

    private fun cleanupOld(preferences: SharedPreferences, today: String) {
        try {
            val todayMs = dateFmt.parse(today)?.time ?: return
            val editor = preferences.edit()
            var removed = false
            for (key in preferences.all.keys) {
                if (!key.startsWith("ad_show_count")) continue
                val datePart = DATE_RE.find(key)?.value ?: continue
                val millis = try {
                    dateFmt.parse(datePart)?.time
                } catch (exception: Exception) {
                    null
                } ?: continue
                if ((todayMs - millis) / 86_400_000L > 7) {
                    editor.remove(key)
                    removed = true
                }
            }
            if (removed) editor.apply()
        } catch (exception: Exception) {
            // Ignore corrupted preference keys.
        }
    }
}
