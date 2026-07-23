package com.vga.spinwheel.core

import android.content.Context
import com.brian.base_application.language.LocaleManager

object LocaleHelper {

    fun updateLocale(context: Context, languageCode: String) {
        if (languageCode.isNotBlank()) {
            LocaleManager.setLocale(context, languageCode)
        }
    }
}
