package com.vga.spinwheel.advertisement

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brian.base_iap.utils.IAPUtils
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.vga.spinwheel.firebase.Remote

private const val MIN_AD_REQUEST_INTERVAL_MS = 1_000L

@Composable
fun NativeAdSlot(
    placement: String,
    modifier: Modifier = Modifier,
    isSmall: Boolean = false,
    onResolved: ((loaded: Boolean) -> Unit)? = null,
) {
    val remote = Remote.instance
    val unitId = remember(placement) { remote.adUnit(placement) }
    val enabled = !IAPUtils.isPremium() && remote.isAdEnabled(placement) && unitId.isNotBlank()
    if (!enabled) {
        LaunchedEffect(placement) { onResolved?.invoke(false) }
        return
    }

    val viewModel = rememberAdsViewModel()
    val slot = remember(placement) { viewModel.slot(placement) }
    val isLoading by slot.isLoading.collectAsState()
    val isLoaded by slot.isLoaded.collectAsState()
    val cachedAd by slot.nativeAd.collectAsState()
    var suppressedAfterError by remember(placement) { mutableStateOf(false) }

    LaunchedEffect(placement) { slot.prepareForEntry(MIN_AD_REQUEST_INTERVAL_MS) }

    when {
        suppressedAfterError -> Unit
        isLoaded -> NativeAdView2(cachedAd, modifier, isSmall)
        isLoading -> Shimmer(modifier, isSmall)
        else -> NativeLoader(
            placement = placement,
            unitId = unitId,
            modifier = modifier,
            isSmall = isSmall,
            loadOnMount = slot.shouldLoadOnMount(),
            onStartLoad = {
                println("ADSLOT load START $placement unit=$unitId")
                slot.onStartLoad()
            },
            onLoaded = slot::onLoaded,
            onLoadedNative = slot::onLoadedNative,
            onImpression = slot::onImpression,
            onResolved = { loaded ->
                if (!loaded) {
                    println("ADSLOT ERR failed $placement")
                    slot.onError()
                    suppressedAfterError = true
                } else {
                    println("ADSLOT loaded $placement ad=true")
                }
                onResolved?.invoke(loaded)
            },
        )
    }
}

@Composable
private fun NativeLoader(
    placement: String,
    unitId: String,
    modifier: Modifier,
    isSmall: Boolean,
    loadOnMount: Boolean,
    onStartLoad: () -> Unit,
    onLoaded: () -> Unit,
    onLoadedNative: (NativeAd) -> Unit,
    onImpression: () -> Unit,
    onResolved: (Boolean) -> Unit,
) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val context = LocalContext.current

    LaunchedEffect(loadOnMount) {
        if (!loadOnMount) return@LaunchedEffect
        onStartLoad()
        Admob.getInstance().loadNativeAd(
            context.applicationContext,
            unitId,
            object : NativeCallback() {
                override fun onNativeAdLoaded(loadedAd: NativeAd?) {
                    if (loadedAd != null) {
                        nativeAd = loadedAd
                        onLoadedNative(loadedAd)
                        onLoaded()
                        onResolved(true)
                    } else {
                        onResolved(false)
                    }
                }

                override fun onAdFailedToLoad() {
                    onResolved(false)
                }

                override fun onAdImpression() {
                    println("ADSLOT impression $placement")
                    onImpression()
                }

                override fun onAdClicked() {
                    println("ADSLOT clicked $placement")
                }
            },
        )
    }

    nativeAd?.let { NativeAdView2(it, modifier, isSmall) }
}

@Composable
private fun NativeAdView2(
    nativeAd: NativeAd?,
    modifier: Modifier = Modifier,
    isSmall: Boolean = false,
) {
    val ad = nativeAd ?: return
    key(ad) {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { viewContext ->
                val adView = LayoutInflater.from(viewContext)
                    .inflate(nativeLayout(isSmall), null) as NativeAdView
                Admob.getInstance().pushAdsToViewCustom(ad, adView)
                adView
            },
            update = {},
        )
    }
}

@Composable
private fun Shimmer(
    modifier: Modifier = Modifier,
    isSmall: Boolean = false,
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { viewContext ->
            LayoutInflater.from(viewContext).inflate(loadingLayout(isSmall), null)
        },
        update = {},
    )
}

private tailrec fun Context.findComponentActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findComponentActivity()
    else -> null
}

@Composable
private fun rememberAdsViewModel(): AdsViewModel {
    val owner: ViewModelStoreOwner = LocalContext.current.findComponentActivity()
        ?: checkNotNull(LocalViewModelStoreOwner.current) { "No ViewModelStoreOwner" }
    return viewModel(viewModelStoreOwner = owner)
}

private fun nativeLayout(isSmall: Boolean): Int =
    if (isSmall) {
        com.brian.base_application.R.layout.ads_native_bot_no_media_short_main
    } else {
        com.brian.base_application.R.layout.ads_native_bot_2
    }

private fun loadingLayout(isSmall: Boolean): Int =
    if (isSmall) {
        com.brian.base_application.R.layout.ads_native_loading_short_main
    } else {
        com.brian.base_application.R.layout.ads_native_bot_loading_2
    }
