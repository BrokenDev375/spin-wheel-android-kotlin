package com.vga.spinwheel.advertisement

import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.util.Admob
import com.vga.spinwheel.firebase.Remote

@Composable
fun NativeAdSlot(
    placement: String,
    modifier: Modifier = Modifier,
    isSmall: Boolean = false,
    viewModel: AdsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val enabled = remember(placement) { Remote.instance.isAdEnabled(placement) }
    if (!enabled) return

    val unitId = remember(placement) { Remote.instance.adUnit(placement) }
    if (unitId.isBlank()) return

    val state = viewModel.stateFor(placement)
    LaunchedEffect(placement, unitId) {
        viewModel.prepareForEntry(placement)
        viewModel.loadNativeAd(context, placement, unitId)
    }

    when {
        state.failed -> Unit
        state.nativeAd != null -> {
            val nativeAd = state.nativeAd ?: return
            key(nativeAd) {
                AndroidView(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(if (isSmall) SMALL_HEIGHT else FULL_HEIGHT),
                    factory = { viewContext ->
                        val adView = LayoutInflater.from(viewContext)
                            .inflate(nativeLayout(isSmall), null) as NativeAdView
                        Admob.getInstance().pushAdsToViewCustom(nativeAd, adView)
                        adView
                    },
                    update = {},
                )
            }
        }
        state.isLoading -> {
            AndroidView(
                modifier = modifier
                    .fillMaxWidth()
                    .height(if (isSmall) SMALL_HEIGHT else FULL_HEIGHT),
                factory = { viewContext ->
                    LayoutInflater.from(viewContext).inflate(loadingLayout(isSmall), null)
                },
                update = {},
            )
        }
    }
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

private val SMALL_HEIGHT = 112.dp
private val FULL_HEIGHT = 280.dp
