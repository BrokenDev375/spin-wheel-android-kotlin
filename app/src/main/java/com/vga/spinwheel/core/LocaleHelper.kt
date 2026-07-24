package com.vga.spinwheel.core

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {

    fun wrap(context: Context, languageCode: String = AppStorage.languageCode(context)): Context {
        if (languageCode.isBlank()) return context

        val locale = languageCode.toLocale()
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        configuration.setLocales(LocaleList(locale))

        return context.createConfigurationContext(configuration)
    }

    private fun String.toLocale(): Locale {
        val languageTag = replace('_', '-')
            .let { tag ->
                when (tag) {
                    "en-UK" -> "en-GB"
                    "iw-IL" -> "he-IL"
                    else -> tag
                }
            }
        return Locale.forLanguageTag(languageTag)
    }
}
