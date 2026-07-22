package com.vga.spinwheel.advertisement

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.vga.spinwheel.R
import kotlinx.coroutines.delay

@Composable
fun NativeAdsFull(
    unitId: String,
    modifier: Modifier = Modifier,
    preloadedNativeAd: NativeAd? = null,
    closeDelaySeconds: Int = 3,
    onClose: () -> Unit,
    onError: () -> Unit,
) {
    val context = LocalContext.current
    var nativeAd by remember(preloadedNativeAd) { mutableStateOf(preloadedNativeAd) }
    var isLoading by remember(preloadedNativeAd) { mutableStateOf(preloadedNativeAd == null) }
    var closeEnabled by remember { mutableStateOf(closeDelaySeconds <= 0) }

    LaunchedEffect(closeDelaySeconds) {
        if (closeDelaySeconds > 0) {
            delay(closeDelaySeconds * 1000L)
            closeEnabled = true
        }
    }

    LaunchedEffect(unitId, preloadedNativeAd) {
        if (preloadedNativeAd != null) return@LaunchedEffect
        if (unitId.isBlank()) {
            onError()
            return@LaunchedEffect
        }

        isLoading = true
        println("ADS native full load START unit=$unitId")
        Admob.getInstance().loadNativeAd(
            context.applicationContext,
            unitId,
            object : NativeCallback() {
                override fun onNativeAdLoaded(loadedAd: NativeAd) {
                    println("ADS native full loaded ad=true")
                    isLoading = false
                    nativeAd = loadedAd
                }

                override fun onAdFailedToLoad() {
                    println("ADS native full ERR failed")
                    isLoading = false
                    onError()
                }
            },
        )
    }

    DisposableEffect(nativeAd) {
        onDispose {
            nativeAd?.destroy()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF15131F)),
    ) {
        when {
            nativeAd != null -> {
                val ad = nativeAd ?: return
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { viewContext ->
                        val adView = LayoutInflater.from(viewContext)
                            .inflate(R.layout.ad_native_full, null) as NativeAdView
                        bindFull(adView, ad)
                        adView
                    },
                    update = {},
                )
            }
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = context.getString(R.string.ad_loading),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            enabled = closeEnabled,
            onClick = onClose,
        ) {
            Text(
                text = if (closeEnabled) "X" else closeDelaySeconds.toString(),
                color = Color.White,
            )
        }
    }
}

fun bindFull(
    nativeAdView: NativeAdView,
    nativeAd: NativeAd,
) {
    val mediaView = nativeAdView.findViewById<MediaView>(R.id.ad_media)
    val iconView = nativeAdView.findViewById<ImageView>(R.id.ad_icon)
    val headlineView = nativeAdView.findViewById<TextView>(R.id.ad_headline)
    val bodyView = nativeAdView.findViewById<TextView>(R.id.ad_body)
    val callToActionView = nativeAdView.findViewById<Button>(R.id.ad_call_to_action)

    nativeAdView.mediaView = mediaView
    nativeAdView.iconView = iconView
    nativeAdView.headlineView = headlineView
    nativeAdView.bodyView = bodyView
    nativeAdView.callToActionView = callToActionView

    headlineView.text = nativeAd.headline.orEmpty()

    bodyView.text = nativeAd.body.orEmpty()
    bodyView.visibility = if (nativeAd.body.isNullOrBlank()) View.GONE else View.VISIBLE

    callToActionView.text = nativeAd.callToAction.orEmpty()
    callToActionView.visibility =
        if (nativeAd.callToAction.isNullOrBlank()) View.GONE else View.VISIBLE

    iconView.setImageDrawable(nativeAd.icon?.drawable)
    iconView.visibility = if (nativeAd.icon == null) View.GONE else View.VISIBLE

    mediaView.mediaContent = nativeAd.mediaContent
    mediaView.visibility = if (nativeAd.mediaContent == null) View.GONE else View.VISIBLE

    nativeAdView.setNativeAd(nativeAd)
}
