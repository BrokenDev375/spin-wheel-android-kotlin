package com.vga.spinwheel

import android.app.Activity
import com.brian.base_application.BaseApplication
import com.brian.base_iap.utils.FirebaseRemoteConfigUtil
import com.brian.base_iap.utils.IAPUtils
import com.nlbn.ads.util.AppFlyer
import com.vga.spinwheel.core.AppStorage
import com.vga.spinwheel.core.IntroActivity
import com.vga.spinwheel.core.MainActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        registerRemoteConfigDefaults()
    }

    override fun getHomeActivity(): Class<out Activity> =
        if (AppStorage.isOnboardingDone(this)) {
            MainActivity::class.java
        } else {
            IntroActivity::class.java
        }

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

    override fun getFeature1TextRes(): Int = R.string.iap_feature_1

    override fun getFeature2TextRes(): Int = R.string.iap_feature_2

    override fun getFeature3TextRes(): Int = R.string.iap_feature_3

    override fun getFeature4TextRes(): Int = R.string.iap_feature_4

    override fun getFeature5TextRes(): Int = R.string.iap_feature_5

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

    override fun isPurchased(): Boolean = IAPUtils.isPremium()

    override fun enableAdsResume(): Boolean = !BuildConfig.DEBUG && !IAPUtils.isPremium()

    override fun buildDebug(): Boolean = BuildConfig.DEBUG

    override fun isForceShowFullAdsTest(): Boolean = false

    override fun getListTestDeviceId(): List<String> = emptyList()

    override fun getResumeAdId(): String = GOOGLE_TEST_APP_OPEN_ID

    private fun registerRemoteConfigDefaults() {
        runCatching {
            FirebaseRemoteConfigUtil.getInstance().setAppDefaultsFromXml(R.xml.config)
        }
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_PREFIX = "SpinWheel"
        const val GOOGLE_TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
        const val MOCK_KEY_PREFIX = "mock_"
    }
}
