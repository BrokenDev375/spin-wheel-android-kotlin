package com.vga.spinwheel.core

import android.content.Context

object AppStorage {

    fun languageCode(context: Context): String =
        preferences(context).getString(KEY_LANGUAGE, DEFAULT_LANGUAGE_CODE)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_LANGUAGE_CODE

    fun setLanguageCode(context: Context, languageCode: String) {
        preferences(context)
            .edit()
            .putString(KEY_LANGUAGE, languageCode.ifBlank { DEFAULT_LANGUAGE_CODE })
            .commit()
    }

    fun isOnboardingDone(context: Context): Boolean =
        preferences(context).getBoolean(KEY_ONBOARDING_DONE, false)

    fun setOnboardingDone(context: Context, done: Boolean) {
        preferences(context)
            .edit()
            .putBoolean(KEY_ONBOARDING_DONE, done)
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
    private const val KEY_LANGUAGE = "language_pres"
    private const val KEY_ONBOARDING_DONE = "onboarding_done"
    private const val KEY_IS_ADS_CAMPAIGN = "is_ads_campaign"
    private const val KEY_ADS_CAMPAIGN_RESOLVED = "ads_campaign_resolved"
    private const val DEFAULT_LANGUAGE_CODE = "vi"
}
