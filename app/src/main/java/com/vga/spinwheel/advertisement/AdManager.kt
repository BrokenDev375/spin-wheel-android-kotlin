package com.vga.spinwheel.advertisement

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.vga.spinwheel.firebase.Remote

object AdManager {

    fun showInter(
        activity: Activity?,
        placement: String,
        noCount: Boolean = false,
        onNext: () -> Unit,
    ) {
        if (activity == null) {
            OnceAction(onNext).run()
            return
        }

        val nextAction = OnceAction(onNext)
        val fallbackAction = OnceAction {
            showNativeInterFallback(
                activity = activity,
                interPlacement = placement,
                nextAction = nextAction,
            )
        }

        val remote = Remote.instance
        if (!remote.isAdEnabled(placement)) {
            fallbackAction.run()
            return
        }

        val scenario = AdScenario.from(activity)
        val shouldShowInter = scenario.shouldShow(
            placement = placement,
            ratio = placementInt(remote, placement, "ratio", "showRatio", defaultValue = 1),
            maxPerDay = placementInt(remote, placement, "max", "maxShowPerDay", defaultValue = 20),
            noCount = noCount,
        )
        if (!shouldShowInter) {
            fallbackAction.run()
            return
        }

        val unitId = remote.adUnit(placement)
        if (unitId.isBlank()) {
            fallbackAction.run()
            return
        }

        maybePreloadNativeInter(activity, placement)
        println("ADS inter load START $placement unit=$unitId")
        Admob.getInstance().loadAndShowInter(
            activity,
            unitId,
            true,
            object : AdCallback() {
                override fun onNextAction() {
                    println("ADS inter NEXT $placement")
                    fallbackAction.run()
                }

                override fun onAdFailedToLoad(error: LoadAdError?) {
                    println("ADS inter ERR load $placement ${error?.message.orEmpty()}")
                    fallbackAction.run()
                }

                override fun onAdFailedToShow(error: AdError?) {
                    println("ADS inter ERR show $placement ${error?.message.orEmpty()}")
                    fallbackAction.run()
                }
            },
        )
    }

    private fun maybePreloadNativeInter(
        activity: Activity,
        interPlacement: String,
    ) {
        val remote = Remote.instance
        val nativePlacement = nativeInterPlacement(interPlacement)
        if (!remote.isAdEnabled(nativePlacement)) return

        val unitId = remote.adUnit(nativePlacement)
        if (unitId.isBlank()) return

        NativeInterController.preload(
            context = activity.applicationContext,
            placement = nativePlacement,
            unitId = unitId,
        )
    }

    private fun showNativeInterFallback(
        activity: Activity,
        interPlacement: String,
        nextAction: OnceAction,
    ) {
        val remote = Remote.instance
        val nativePlacement = nativeInterPlacement(interPlacement)
        if (!remote.isAdEnabled(nativePlacement)) {
            nextAction.run()
            return
        }

        val scenario = AdScenario.from(activity)
        val shouldShowNativeInter = scenario.shouldShow(
            placement = nativePlacement,
            ratio = placementInt(remote, nativePlacement, "ratio", "showRatio", defaultValue = 1),
            maxPerDay = placementInt(remote, nativePlacement, "max", "maxShowPerDay", defaultValue = 20),
        )
        if (!shouldShowNativeInter) {
            nextAction.run()
            return
        }

        val unitId = remote.adUnit(nativePlacement)
        if (unitId.isBlank()) {
            nextAction.run()
            return
        }

        NativeInterController.show(
            placement = nativePlacement,
            unitId = unitId,
            onFinished = nextAction::run,
        )
    }

    fun nativeInterPlacement(interPlacement: String): String =
        if (interPlacement.startsWith("inter_")) {
            "native_$interPlacement"
        } else {
            "native_inter_$interPlacement"
        }

    private fun placementInt(
        remote: Remote,
        placement: String,
        suffix: String,
        legacySuffix: String,
        defaultValue: Int,
    ): Int {
        val primaryValue = remote.getInt("${placement}_$suffix", Int.MIN_VALUE)
        if (primaryValue != Int.MIN_VALUE) return primaryValue
        return remote.getInt("${placement}_$legacySuffix", defaultValue)
    }
}
