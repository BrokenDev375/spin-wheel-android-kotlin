package com.vga.spinwheel.core

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.vga.spinwheel.advertisement.NativeInterHost
import com.vga.spinwheel.ui.nav.AppNavHost
import com.vga.spinwheel.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.updateLocale(this, AppStorage.languageCode(this))

        setContent {
            AppTheme {
                AppNavHost()
                NativeInterHost()
            }
        }
    }
}
