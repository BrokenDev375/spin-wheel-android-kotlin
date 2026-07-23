package com.vga.spinwheel.firebase

import com.brian.base_iap.utils.FirebaseRemoteConfigUtil
import com.brian.base_iap.utils.IAPUtils
import com.vga.spinwheel.BuildConfig

class Remote private constructor() {

    private val frc: FirebaseRemoteConfigUtil
        get() = FirebaseRemoteConfigUtil.getInstance()

    fun adUnit(placement: String): String =
        if (BuildConfig.DEBUG) {
            debugTestUnit(placement)
        } else {
            runCatching { frc.getAdsConfigValue(placement) }.getOrDefault("")
        }

    fun getBoolean(key: String): Boolean {
        if (IAPUtils.isPremium() && key.endsWith("_enable")) return false
        return runCatching { frc.getBoolean(key) }.getOrDefault(false)
    }

    fun getString(key: String): String =
        runCatching { frc.getString(key) }.getOrDefault("")

    fun getLong(key: String): Long =
        runCatching { frc.getLong(key) }.getOrDefault(0L)

    fun getInt(
        key: String,
        defaultValue: Int = 0,
    ): Int {
        val stringValue = getString(key)
        if (stringValue.isNotBlank()) {
            return stringValue.toIntOrNull() ?: defaultValue
        }

        val value = getLong(key)
        return if (value != 0L) {
            value.coerceIn(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
        } else {
            defaultValue
        }
    }

    fun isAdEnabled(placement: String): Boolean = getBoolean("${placement}_enable")

    private fun debugTestUnit(placement: String): String =
        when {
            placement.startsWith("inter") -> GOOGLE_TEST_INTERSTITIAL_ID
            placement.startsWith("open") -> GOOGLE_TEST_APP_OPEN_ID
            else -> GOOGLE_TEST_NATIVE_ID
        }

    companion object {
        const val KEY_POSITION_INTRO = "positionIntrol"

        val instance: Remote by lazy { Remote() }

        private const val GOOGLE_TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
        private const val GOOGLE_TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val GOOGLE_TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
    }
}
