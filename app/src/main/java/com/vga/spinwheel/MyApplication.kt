package com.vga.spinwheel

import android.app.Activity
import com.brian.base_application.BaseApplication
import com.brian.base_iap.iap.IapFeatureConfig
import com.brian.base_iap.iap.IapFeatureItem
import com.brian.base_iap.utils.FirebaseRemoteConfigUtil
import com.brian.base_iap.utils.IAPUtils
import com.nlbn.ads.util.AppFlyer
import com.nlbn.ads.util.AppOpenManager
import com.vga.spinwheel.core.AppStorage
import com.vga.spinwheel.core.InstallReferrerHelper
import com.vga.spinwheel.core.LocaleHelper
import com.vga.spinwheel.core.MainActivity
import com.vga.spinwheel.firebase.Remote
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : BaseApplication() {

    override fun onCreate() {
        InstallReferrerHelper.resolve(this)
        AppOpenManager.getInstance().disableAppResumeWithActivity(com.brian.base_application.start.SplashActivity::class.java)
        com.brian.base_application.language.LanguageRouter.customActivityClass = com.vga.spinwheel.ui.screen.language.MyLanguageActivity::class.java
        AppStorage.language(this)?.let { languageCode ->
            LocaleHelper.updateLocale(this, languageCode)
        }
        super.onCreate()
        refreshIapFeatureConfig(AppStorage.languageCode(this))
        registerRemoteConfigDefaults()
    }

    override fun getHomeActivity(): Class<out Activity> = MainActivity::class.java

    override fun getAppNameRes(): Int = R.string.app_name

    override fun getIconSplashRes(): Int = R.drawable.icon_app

    override fun getSplashLoadingRes(): Int = R.raw.splash_loading

    override fun hasForegroundServicePermission(): Boolean = true

    override fun initAppFlyerId() {
        val appFlyerId = getString(R.string.app_flyer_id)
        if (appFlyerId.isBlank() || appFlyerId.startsWith(MOCK_KEY_PREFIX, ignoreCase = true)) {
            return
        }

        AppFlyer.getInstance().initAppFlyer(
            this,
            appFlyerId,
            BuildConfig.DEBUG,
            false,
            true
        )
    }

    override fun setupKoin() = Unit

    override fun notifyLanguageSaved(languageCode: String) {
        AppStorage.setLanguageCode(this, languageCode)
        LocaleHelper.updateLocale(this, languageCode)
        refreshIapFeatureConfig(languageCode)
    }

    private fun refreshIapFeatureConfig(languageCode: String) {
        val localizedContext = LocaleHelper.wrap(this, languageCode)

        // base-application caches these paywall labels as Strings, so refresh after locale changes.
        IapFeatureConfig.items =
            listOf(
                IapFeatureItem(
                    localizedContext.getString(getFeature1TextRes()),
                    getFeature1IconRes(),
                    true,
                    true
                ),
                IapFeatureItem(
                    localizedContext.getString(getFeature2TextRes()),
                    getFeature2IconRes(),
                    true,
                    true
                ),
                IapFeatureItem(
                    localizedContext.getString(getFeature3TextRes()),
                    getFeature3IconRes(),
                    true,
                    true
                ),
                IapFeatureItem(
                    localizedContext.getString(getFeature4TextRes()),
                    getFeature4IconRes(),
                    true,
                    false
                ),
                IapFeatureItem(
                    localizedContext.getString(getFeature5TextRes()),
                    getFeature5IconRes(),
                    true,
                    false
                )
            )
    }

    override fun iapPremiumKey(): String = defaultIapPremiumKey()

    override fun iapPremiumWeeklyKey(): String = defaultIapPremiumWeeklyKey()

    override fun iapPremiumMonthlyKey(): String = defaultIapPremiumMonthlyKey()

    override fun iapPremiumYearlyKey(): String = defaultIapPremiumYearlyKey()

    override fun iapPublicKey(): String = getString(R.string.public_license_key)

    // Paywall order: ads, unlock, custom wheel, premium themes, support.
    override fun getFeature1IconRes(): Int = R.drawable.icon_4_iap

    override fun getFeature2IconRes(): Int = R.drawable.icon_2_iap

    override fun getFeature3IconRes(): Int = R.drawable.icon_1_iap

    override fun getFeature4IconRes(): Int = R.drawable.icon_5_iap

    override fun getFeature5IconRes(): Int = R.drawable.icon_3_iap

    override fun getFeature1TextRes(): Int = R.string.premium_feature_remove_ads

    override fun getFeature2TextRes(): Int = R.string.premium_feature_unlock_all

    override fun getFeature3TextRes(): Int = R.string.premium_feature_unlimited

    override fun getFeature4TextRes(): Int = R.string.premium_feature_premium_skins

    override fun getFeature5TextRes(): Int = R.string.premium_feature_support

    override fun getNotiTitleRes(): Int = R.string.notification_permission_title

    override fun getNotiContentRes(): Int = R.string.notification_permission_content

    override fun getNotificationImages(): IntArray = intArrayOf(
        R.drawable.icon_noti_1,
        R.drawable.icon_noti_2,
        R.drawable.icon_noti_3,
        R.drawable.icon_noti_4,
        R.drawable.icon_noti_5
    )

    override fun getNotificationIconRes(): Int = R.drawable.icon_notification

    override fun getNotificationChannelPrefix(): String = NOTIFICATION_CHANNEL_PREFIX

    override fun getNewFileNotiContentRes(): Int = R.string.notification_new_random_content

    override fun getScreenshotNotiTitleRes(): Int = R.string.notification_screenshot_title

    override fun getRecentDocumentsTitleRes(): Int = R.string.notification_recent_title

    override fun getOpenTextRes(): Int = R.string.notification_open

    override fun getScanDocumentRes(): Int = R.string.notification_spin_now

    override fun getWidgetButtonBackgroundRes(): Int = R.drawable.bg_widget_button

    override fun getDailyCallOpenAppContentRes(): Int = R.string.notification_daily_content

    override fun getCheckNowTextRes(): Int = R.string.notification_check_now

    override fun getDocumentPreviewRes(): Int = R.drawable.img_document_preview

    override fun getFullScreenNoti1Res(): Int = R.string.notification_fullscreen_1

    override fun getFullScreenNoti2Res(): Int = R.string.notification_fullscreen_2

    override fun getNotificationTitles2ArrayRes(): Int = R.array.notification_title2

    override fun getNotificationMessages2ArrayRes(): Int = R.array.notification_message2

    override fun getNotificationButtons2ArrayRes(): Int = R.array.notification_button2

    override fun getNotificationOutAppTitleRes(): Int = R.string.notification_out_app_title

    override fun getNotificationOutAppContentRes(): Int = R.string.notification_out_app_content

    override fun isPurchased(): Boolean {
        val premium = IAPUtils.isPremium()
        println("ADS_CHECK: isPurchased=$premium")
        return premium
    }

    override fun enableAdsResume(): Boolean {
        val premium = IAPUtils.isPremium()
        val enabled = !BuildConfig.DEBUG && !premium
        println(
            "ADS_CHECK: enableAdsResume=$enabled debug=${BuildConfig.DEBUG} buildType=${BuildConfig.BUILD_TYPE} premium=$premium"
        )
        return enabled
    }

    override fun buildDebug(): Boolean {
        println("ADS_CHECK: buildDebug=${BuildConfig.DEBUG} buildType=${BuildConfig.BUILD_TYPE}")
        return BuildConfig.DEBUG
    }

    override fun isForceShowFullAdsTest(): Boolean = false

    override fun getListTestDeviceId(): List<String> = emptyList()

    override fun getResumeAdId(): String {
        return Remote.instance.adUnit("open_app")
    }

    private fun registerRemoteConfigDefaults() {
        runCatching {
            FirebaseRemoteConfigUtil.getInstance().setAppDefaultsFromXml(R.xml.config)
        }
    }

    private fun maskAdId(adId: String): String =
        if (adId.isBlank()) {
            "<blank>"
        } else {
            "***${adId.takeLast(8)}"
        }

    private companion object {
        const val ADS_LOG_TAG = "ADS_CHECK"
        const val NOTIFICATION_CHANNEL_PREFIX = "SpinWheel"
        const val GOOGLE_TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
        const val MOCK_KEY_PREFIX = "mock_"
    }
}
