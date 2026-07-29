package com.vga.spinwheel.core

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.vga.spinwheel.advertisement.NativeInterHost
import com.vga.spinwheel.ui.nav.AppNavHost
import com.vga.spinwheel.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

import com.vga.spinwheel.firebase.Remote
import com.vga.spinwheel.ui.nav.Screen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var appliedLanguageCode: String = ""

    override fun attachBaseContext(newBase: Context) {
        appliedLanguageCode = AppStorage.languageCode(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ShortcutHelper.addUninstallShortcut(this)

        val startRoute = resolveStartRoute()

        setContent {
            AppTheme {
                AppNavHost(startRoute = startRoute)
                NativeInterHost()
            }
        }
    }

    private fun resolveStartRoute(): String {
        val n = AppStorage.goToHomeNumber(this)
        AppStorage.setGoToHomeNumber(this, n + 1)

        val countAppOpen = Remote.instance.getInt("count_app_open", 3).let { if (it <= 0) 3 else it }
        val organic = Remote.instance.getInt("organic_number_not_guide", 3).let { if (it < 0) 0 else it }
        val isAds = InstallReferrerHelper.isAdsCampaign(this)

        val goToHomeStatus = if (isAds) {
            n >= countAppOpen
        } else {
            n < organic || n >= (countAppOpen + organic)
        }

        return if (goToHomeStatus) Screen.Home.route else Screen.Intro.route
    }

    override fun onResume() {
        super.onResume()
        val currentLanguageCode = AppStorage.languageCode(this)
        if (currentLanguageCode != appliedLanguageCode) {
            appliedLanguageCode = currentLanguageCode
            recreate()
        }
    }
}
