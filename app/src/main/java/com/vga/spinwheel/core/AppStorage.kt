package com.vga.spinwheel.core

import android.content.Context

object AppStorage {

    fun goToHomeNumber(context: Context): Int =
        preferences(context).getInt(KEY_GO_TO_HOME_NUMBER, DEFAULT_GO_TO_HOME_NUMBER)
            .coerceAtLeast(DEFAULT_GO_TO_HOME_NUMBER)

    fun setGoToHomeNumber(context: Context, value: Int) {
        preferences(context)
            .edit()
            .putInt(KEY_GO_TO_HOME_NUMBER, value.coerceAtLeast(DEFAULT_GO_TO_HOME_NUMBER))
            .apply()
    }

    fun isAdsCampaign(context: Context): Boolean =
        preferences(context).getBoolean(KEY_IS_ADS_CAMPAIGN, true)

    fun setAdsCampaign(context: Context, isAdsCampaign: Boolean) {
        preferences(context)
            .edit()
            .putBoolean(KEY_IS_ADS_CAMPAIGN, isAdsCampaign)
            .putBoolean(KEY_ADS_CAMPAIGN_RESOLVED, true)
            .apply()
    }

    fun isAdsCampaignResolved(context: Context): Boolean =
        preferences(context).getBoolean(KEY_ADS_CAMPAIGN_RESOLVED, false)

    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private const val PREFERENCES_NAME = "AppStorage"
    private const val KEY_GO_TO_HOME_NUMBER = "goToHomeNumber"
    private const val KEY_IS_ADS_CAMPAIGN = "is_ads_campaign"
    private const val KEY_ADS_CAMPAIGN_RESOLVED = "ads_campaign_resolved"
    private const val DEFAULT_GO_TO_HOME_NUMBER = 1
}
