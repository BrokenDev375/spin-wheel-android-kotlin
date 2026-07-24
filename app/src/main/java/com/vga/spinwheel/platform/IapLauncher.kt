package com.vga.spinwheel.platform

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.brian.base_iap.utils.IAPUtils
import com.brian.base_iap.utils.NativeCodecSnowFlakeCortexAI

object IapLauncher {

    fun open(context: Context) {
        val activity = context.findActivity()
        if (activity == null) {
            Log.w(TAG, "no Activity to open IAP")
            return
        }

        NativeCodecSnowFlakeCortexAI.nativeAiStartIapActivity(activity)
    }

    fun isPremium(): Boolean = IAPUtils.isPremium()

    private tailrec fun Context.findActivity(): Activity? =
        when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }

    private const val TAG = "IapLauncher"
}
