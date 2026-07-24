package com.vga.spinwheel.firebase

import android.util.Log
import com.brian.base_iap.utils.FirebaseRemoteConfigUtil
import com.brian.base_iap.utils.IAPUtils
import com.vga.spinwheel.BuildConfig

class Remote private constructor() {

    private val frc: FirebaseRemoteConfigUtil
        get() = FirebaseRemoteConfigUtil.getInstance()

    fun adUnit(placement: String): String {
        val source = if (BuildConfig.DEBUG) "debug_test" else "remote_config"
        val unit = if (BuildConfig.DEBUG) {
            debugTestUnit(placement)
        } else {
            runCatching { frc.getAdsConfigValue(placement) }.getOrDefault("")
        }
        Log.d(
            ADS_LOG_TAG,
            "adUnit placement=$placement source=$source debug=${BuildConfig.DEBUG} buildType=${BuildConfig.BUILD_TYPE} hasUnit=${unit.isNotBlank()} id=${maskAdId(unit)}"
        )
        return unit
    }

    fun getBoolean(key: String): Boolean {
        val premium = IAPUtils.isPremium()
        if (premium && key.endsWith("_enable")) {
            Log.d(ADS_LOG_TAG, "getBoolean key=$key value=false reason=premium")
            return false
        }

        val value = runCatching { frc.getBoolean(key) }.getOrDefault(false)
        if (key.endsWith("_enable")) {
            Log.d(ADS_LOG_TAG, "getBoolean key=$key value=$value premium=$premium")
        }
        return value
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

    private fun maskAdId(adId: String): String =
        if (adId.isBlank()) {
            "<blank>"
        } else {
            "***${adId.takeLast(8)}"
        }

    companion object {
        const val KEY_POSITION_INTRO = "positionIntrol"

        val instance: Remote by lazy { Remote() }

        private const val ADS_LOG_TAG = "ADS_CHECK"
        private const val GOOGLE_TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
        private const val GOOGLE_TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val GOOGLE_TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
    }
}
