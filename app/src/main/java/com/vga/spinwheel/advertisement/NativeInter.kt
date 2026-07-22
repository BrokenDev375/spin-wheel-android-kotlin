package com.vga.spinwheel.advertisement

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob

object NativeInterController {

    var currentRequest by mutableStateOf<NativeInterRequest?>(null)
        private set

    private val preloadedAds = mutableMapOf<String, NativeAd>()
    private val loadingPlacements = mutableSetOf<String>()
    private var nextRequestId = 0L

    @Synchronized
    fun show(
        placement: String,
        unitId: String,
        onFinished: () -> Unit,
    ) {
        val nextAction = OnceAction(onFinished)
        if (unitId.isBlank()) {
            nextAction.run()
            return
        }
        if (currentRequest != null) {
            nextAction.run()
            return
        }

        currentRequest = NativeInterRequest(
            id = ++nextRequestId,
            placement = placement,
            unitId = unitId,
            nextAction = nextAction,
        )
    }

    @Synchronized
    fun finish(requestId: Long) {
        val request = currentRequest ?: return
        if (request.id != requestId) return
        currentRequest = null
        request.nextAction.run()
    }

    @Synchronized
    fun preload(
        context: Context,
        placement: String,
        unitId: String,
    ) {
        if (unitId.isBlank()) return
        if (preloadedAds.containsKey(placement) || loadingPlacements.contains(placement)) return

        loadingPlacements += placement
        println("ADS native-inter preload START $placement unit=$unitId")
        Admob.getInstance().loadNativeAd(
            context.applicationContext,
            unitId,
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    synchronized(this@NativeInterController) {
                        loadingPlacements -= placement
                        preloadedAds.remove(placement)?.destroy()
                        preloadedAds[placement] = nativeAd
                    }
                    println("ADS native-inter preloaded $placement ad=true")
                }

                override fun onAdFailedToLoad() {
                    synchronized(this@NativeInterController) {
                        loadingPlacements -= placement
                    }
                    println("ADS native-inter preload ERR $placement")
                }
            },
        )
    }

    @Synchronized
    fun takePreloaded(placement: String): NativeAd? = preloadedAds.remove(placement)

    @Synchronized
    fun clearPreloaded() {
        preloadedAds.values.forEach { it.destroy() }
        preloadedAds.clear()
        loadingPlacements.clear()
    }
}

data class NativeInterRequest(
    val id: Long,
    val placement: String,
    val unitId: String,
    val nextAction: OnceAction,
)

@Composable
fun NativeInterHost() {
    val request = NativeInterController.currentRequest ?: return
    val preloadedNativeAd = remember(request.id) {
        NativeInterController.takePreloaded(request.placement)
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        NativeAdsFull(
            unitId = request.unitId,
            preloadedNativeAd = preloadedNativeAd,
            onClose = { NativeInterController.finish(request.id) },
            onError = { NativeInterController.finish(request.id) },
        )
    }
}
