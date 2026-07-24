package com.vga.spinwheel.advertisement

import android.app.Activity
import com.brian.base_iap.utils.IAPUtils
import com.google.android.gms.ads.LoadAdError
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.vga.spinwheel.firebase.Remote

object AdManager {

    fun showInter(activity: Activity, interPlacement: String, onNext: () -> Unit) {
        val remote = Remote.instance
        if (IAPUtils.isPremium()) {
            onNext()
            return
        }

        val nativeInterPlacement = "native_$interPlacement"

        val proceedToNativeInter = {
            val nativeInterUnit = remote.adUnit(nativeInterPlacement)
            val nativeInterShow = remote.isAdEnabled(nativeInterPlacement) &&
                nativeInterUnit.isNotBlank() &&
                AdScenario.shouldShow(
                    activity,
                    nativeInterPlacement,
                    ratio = remote.getInt("${nativeInterPlacement}_ratio"),
                    maxPerDay = remote.getInt("${nativeInterPlacement}_max"),
                )
            println("ADS native-inter $nativeInterPlacement show=$nativeInterShow")
            if (nativeInterShow) {
                NativeInterController.show(nativeInterPlacement) { onNext() }
            } else {
                onNext()
            }
        }

        val interAdUnit = remote.adUnit(interPlacement)
        val interShow = remote.isAdEnabled(interPlacement) &&
            interAdUnit.isNotBlank() &&
            AdScenario.shouldShow(
                activity,
                interPlacement,
                ratio = remote.getInt("${interPlacement}_ratio"),
                maxPerDay = remote.getInt("${interPlacement}_max"),
            )
        println("ADS showInter $interPlacement interShow=$interShow")

        if (interShow) {
            if (remote.isAdEnabled(nativeInterPlacement)) {
                NativeInterController.preload(activity.applicationContext, remote.adUnit(nativeInterPlacement))
            }
            var advanced = false
            fun once() {
                if (!advanced) {
                    advanced = true
                    proceedToNativeInter()
                }
            }
            Admob.getInstance().loadAndShowInter(
                activity,
                interAdUnit,
                false,
                object : AdCallback() {
                    override fun onNextAction() {
                        println("ADS inter $interPlacement closed next")
                        once()
                    }

                    override fun onAdFailedToLoad(error: LoadAdError?) {
                        println("ADS ERR inter $interPlacement LOAD FAILED: code=${error?.code} ${error?.message}")
                        once()
                    }
                },
            )
        } else {
            proceedToNativeInter()
        }
    }
}
