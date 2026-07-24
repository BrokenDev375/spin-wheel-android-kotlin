package com.vga.spinwheel.core

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.vga.spinwheel.advertisement.NativeInterHost
import com.vga.spinwheel.ui.nav.AppNavHost
import com.vga.spinwheel.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var appliedLanguageCode: String = ""

    override fun attachBaseContext(newBase: Context) {
        appliedLanguageCode = AppStorage.languageCode(newBase)
        super.attachBaseContext(LocaleHelper.wrap(newBase, appliedLanguageCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppNavHost()
                NativeInterHost()
            }
        }
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
