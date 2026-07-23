package com.vga.spinwheel.platform

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.brian.base_iap.utils.IAPUtils
import com.brian.base_iap.utils.NativeCodecSnowFlakeCortexAI

object IapLauncher {

    fun open(context: Context): Boolean {
        val activity = context.findActivity()
        if (activity == null) {
            println("IAP paywall ERR activity unavailable")
            return false
        }

        NativeCodecSnowFlakeCortexAI.nativeAiStartIapActivity(activity)
        return true
    }

    fun isPremium(): Boolean = IAPUtils.isPremium()

    private tailrec fun Context.findActivity(): Activity? =
        when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }
}
