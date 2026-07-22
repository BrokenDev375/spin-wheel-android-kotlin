package com.vga.spinwheel.advertisement

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor() : ViewModel() {

    private val states = mutableStateMapOf<String, NativeAdSlotState>()

    fun stateFor(placement: String): NativeAdSlotState =
        states.getOrPut(placement) { NativeAdSlotState() }

    fun prepareForEntry(
        placement: String,
        retryAfterErrorMs: Long = DEFAULT_RETRY_AFTER_ERROR_MS,
        nowMillis: Long = System.currentTimeMillis(),
    ) {
        val state = stateFor(placement)
        if (state.failed && nowMillis - state.lastAttemptAtMs >= retryAfterErrorMs) {
            state.resetError()
        }
    }

    fun loadNativeAd(
        context: Context,
        placement: String,
        unitId: String,
    ) {
        val state = stateFor(placement)
        if (unitId.isBlank() || state.isLoading || state.nativeAd != null || state.failed) return

        state.markLoading()
        println("ADSLOT load START $placement unit=$unitId")
        Admob.getInstance().loadNativeAd(
            context.applicationContext,
            unitId,
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    println("ADSLOT loaded $placement ad=true")
                    state.onLoaded(nativeAd)
                }

                override fun onAdFailedToLoad() {
                    println("ADSLOT ERR failed $placement")
                    state.onError()
                }

                override fun onAdImpression() {
                    println("ADSLOT impression $placement")
                }

                override fun onAdClicked() {
                    println("ADSLOT clicked $placement")
                }
            },
        )
    }

    override fun onCleared() {
        states.values.forEach { it.destroy() }
        states.clear()
        super.onCleared()
    }

    private companion object {
        const val DEFAULT_RETRY_AFTER_ERROR_MS = 30 * 60 * 1000L
    }
}

class NativeAdSlotState {
    var isLoading by mutableStateOf(false)
        private set

    var nativeAd by mutableStateOf<NativeAd?>(null)
        private set

    var failed by mutableStateOf(false)
        private set

    var lastAttemptAtMs: Long = 0L
        private set

    fun markLoading(nowMillis: Long = System.currentTimeMillis()) {
        isLoading = true
        failed = false
        lastAttemptAtMs = nowMillis
    }

    fun onLoaded(ad: NativeAd) {
        nativeAd?.destroy()
        nativeAd = ad
        isLoading = false
        failed = false
    }

    fun onError() {
        isLoading = false
        failed = true
    }

    fun resetError() {
        failed = false
    }

    fun destroy() {
        nativeAd?.destroy()
        nativeAd = null
        isLoading = false
    }
}
