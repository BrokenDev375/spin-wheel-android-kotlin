package com.vga.spinwheel.advertisement

import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdsViewModel : ViewModel() {

    private val slots = mutableMapOf<String, Slot>()

    fun slot(name: String): Slot = slots.getOrPut(name) { Slot() }

    override fun onCleared() {
        slots.values.forEach { it.destroy() }
        slots.clear()
        super.onCleared()
    }

    class Slot {
        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _isLoaded = MutableStateFlow(false)
        val isLoaded: StateFlow<Boolean> = _isLoaded

        private val _nativeAd = MutableStateFlow<NativeAd?>(null)
        val nativeAd: StateFlow<NativeAd?> = _nativeAd

        private val _impressionRecorded = MutableStateFlow(false)

        private var lastRequestAtMs: Long = 0L

        fun onStartLoad() {
            _isLoading.value = true
            _isLoaded.value = false
            lastRequestAtMs = System.currentTimeMillis()
        }

        fun onLoaded() {
            _isLoading.value = false
            _isLoaded.value = true
        }

        fun onLoadedNative(ad: NativeAd) {
            _nativeAd.value?.let { previous ->
                if (previous !== ad) {
                    runCatching { previous.destroy() }
                }
            }
            _nativeAd.value = ad
        }

        fun onError() {
            _isLoading.value = false
            _isLoaded.value = false
            _nativeAd.value = null
        }

        fun onImpression() {
            _impressionRecorded.value = true
        }

        fun prepareForEntry(minIntervalMs: Long = 0L) {
            if (_impressionRecorded.value) {
                _isLoading.value = false
                _impressionRecorded.value = false
            }
            if (System.currentTimeMillis() - lastRequestAtMs >= minIntervalMs) {
                _isLoaded.value = false
                if (_nativeAd.value != null) {
                    runCatching { _nativeAd.value?.destroy() }
                    _nativeAd.value = null
                }
            }
        }

        fun shouldLoadOnMount(): Boolean =
            !(_isLoading.value || _isLoaded.value || _nativeAd.value != null)

        fun destroy() {
            runCatching { _nativeAd.value?.destroy() }
            _nativeAd.value = null
            _isLoaded.value = false
            _isLoading.value = false
        }
    }
}
