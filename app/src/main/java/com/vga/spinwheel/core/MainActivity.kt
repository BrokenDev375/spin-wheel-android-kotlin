package com.vga.spinwheel.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vga.spinwheel.advertisement.NativeInterHost
import com.vga.spinwheel.firebase.Remote
import com.vga.spinwheel.ui.nav.AppNavHost
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startRoute = resolveStartRoute(advanceLaunchCounter = savedInstanceState == null)

        setContent {
            AppTheme {
                AppNavHost(startDestination = startRoute)
                NativeInterHost()
            }
        }
    }

    private fun resolveStartRoute(advanceLaunchCounter: Boolean): String {
        val launchNumber = AppStorage.goToHomeNumber(this)
        if (advanceLaunchCounter) {
            AppStorage.setGoToHomeNumber(this, launchNumber + 1)
        }

        val remote = Remote.instance
        val countAppOpen = IntroGate.sanitizeCountAppOpen(
            remote.getInt(
                key = Remote.KEY_COUNT_APP_OPEN,
                defaultValue = IntroGate.DEFAULT_COUNT_APP_OPEN,
            ),
        )
        val organicNumberNotGuide = IntroGate.sanitizeOrganicNumberNotGuide(
            remote.getInt(
                key = Remote.KEY_ORGANIC_NUMBER_NOT_GUIDE,
                defaultValue = IntroGate.DEFAULT_ORGANIC_NUMBER_NOT_GUIDE,
            ),
        )
        val goHome = IntroGate.shouldGoHome(
            launchNumber = launchNumber,
            isAdsCampaign = InstallReferrerHelper.isAdsCampaign(this),
            countAppOpen = countAppOpen,
            organicNumberNotGuide = organicNumberNotGuide,
        )
        return if (goHome) Screen.Home.route else Screen.Intro.route
    }
}
