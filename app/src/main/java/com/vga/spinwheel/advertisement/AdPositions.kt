package com.vga.spinwheel.advertisement

import com.brian.base_iap.utils.IAPUtils
import com.vga.spinwheel.firebase.Remote
import org.json.JSONArray

object AdPositions {

    private const val DEFAULT = "[[1,22,3,4],[1,2,33,4],[1,2,3,4]]"

    fun selected(): List<Int> {
        if (IAPUtils.isPremium()) return emptyList()
        val raw = runCatching { Remote.instance.getString(Remote.KEY_POSITION_INTRO) }.getOrNull()
            .let { if (it.isNullOrBlank()) DEFAULT else it }
        return parsePositions(raw)
    }

    private fun parsePositions(raw: String): List<Int> = try {
        val root = JSONArray(raw)
        if (root.length() == 0) {
            emptyList()
        } else {
            val chosen = if (root.opt(0) is JSONArray) {
                root.getJSONArray((0 until root.length()).random())
            } else {
                root
            }
            (0 until chosen.length()).map { index -> chosen.getInt(index) }
        }
    } catch (exception: Exception) {
        emptyList()
    }
}
