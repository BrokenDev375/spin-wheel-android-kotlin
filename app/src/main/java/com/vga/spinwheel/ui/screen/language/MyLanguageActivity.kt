package com.vga.spinwheel.ui.screen.language

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.brian.base_application.language.LanguageRouter
import com.vga.spinwheel.core.AppStorage
import com.vga.spinwheel.ui.theme.AppTheme

class MyLanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialLanguageCode = AppStorage.languageCode(this)
        val isFromSettings = intent.getBooleanExtra(EXTRA_FROM_SETTINGS, false)

        onBackPressedDispatcher.addCallback(this) {
            if (isFromSettings) {
                finish()
            } else {
                val currentCode = AppStorage.languageCode(this@MyLanguageActivity)
                LanguageRouter.confirmLanguageSelection(this@MyLanguageActivity, currentCode)
            }
        }

        setContent {
            AppTheme {
                LanguageScreen(
                    initialCode = initialLanguageCode,
                    onDone = { selectedCode ->
                        if (isFromSettings) {
                            LanguageRouter.confirmLanguageSelection(this, selectedCode, navigate = false)
                            finish()
                        } else {
                            LanguageRouter.confirmLanguageSelection(this, selectedCode)
                        }
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_FROM_SETTINGS = "extra_from_settings"
    }
}
