package com.vga.spinwheel.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vga.spinwheel.data.model.AppSettingKeys
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import com.vga.spinwheel.ui.nav.AppNavHost
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startRoute = if (settingsRepository.getBoolean(
                feature = RandomFeature.APP,
                key = AppSettingKeys.INTRO_DONE,
                defaultValue = false,
            )
        ) {
            Screen.Home.route
        } else {
            Screen.Intro.route
        }

        setContent {
            AppTheme {
                AppNavHost(startDestination = startRoute)
            }
        }
    }
}
