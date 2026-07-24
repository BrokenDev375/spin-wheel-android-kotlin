package com.vga.spinwheel.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.vga.spinwheel.advertisement.NativeInterHost
import com.vga.spinwheel.ui.screen.intro.IntroScreen
import com.vga.spinwheel.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private var appliedLanguageCode: String = ""

    override fun attachBaseContext(newBase: Context) {
        appliedLanguageCode = AppStorage.languageCode(newBase)
        super.attachBaseContext(LocaleHelper.wrap(newBase, appliedLanguageCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                IntroScreen(
                    onFinished = {
                        AppStorage.setOnboardingDone(this, true)
                        startActivity(
                            Intent(this, MainActivity::class.java)
                                .addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                        Intent.FLAG_ACTIVITY_NEW_TASK,
                                ),
                        )
                        finish()
                    },
                )
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
