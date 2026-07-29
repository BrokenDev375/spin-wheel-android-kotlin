package com.vga.spinwheel.core

import android.content.Context
import android.util.Log
import com.brian.base_iap.utils.IAPUtils
import com.vga.spinwheel.platform.IapLauncher

/**
 * Mở màn IAP/paywall của lib. Dùng IapLauncher.
 * Dùng cho nút mua gói ở Home/Settings và paywall-gate tính năng premium.
 */
object IapOpener {

    fun isPremium(): Boolean =
        runCatching { IAPUtils.isPremium() }
            .onFailure { Log.w(TAG, "Unable to read premium state", it) }
            .getOrDefault(false)

    fun open(context: Context, source: String = "home") {
        runCatching { IapLauncher.open(context) }
            .onFailure { Log.w(TAG, "Unable to open IAP from $source", it) }
    }

    /** Paywall-gate: nếu đã premium chạy [onPremium], chưa thì mở IAP (tự đóng sau khi mua). */
    fun gate(context: Context, source: String = "feature", onPremium: () -> Unit) {
        if (isPremium()) onPremium() else open(context, source)
    }

    private const val TAG = "IapOpener"
}
