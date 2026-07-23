package com.vga.spinwheel.advertisement

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.vga.spinwheel.R
import com.vga.spinwheel.firebase.Remote
import kotlinx.coroutines.delay

object NativeInterController {

    data class Req(val placement: String, val onDismiss: () -> Unit)

    var request by mutableStateOf<Req?>(null)
        private set

    private var preloaded: NativeAd? = null
    private var preloadedUnit: String? = null

    fun preload(context: android.content.Context, unit: String) {
        if (unit.isBlank() || (preloadedUnit == unit && preloaded != null)) return

        Admob.getInstance().loadNativeAd(
            context.applicationContext,
            unit,
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                    if (nativeAd != null) {
                        println("ADS native-inter PRELOAD ok $unit")
                        preloaded?.destroy()
                        preloaded = nativeAd
                        preloadedUnit = unit
                    }
                }

                override fun onAdFailedToLoad() {
                    // Host loads when show is requested.
                }
            },
        )
    }

    fun takePreloaded(unit: String): NativeAd? {
        if (preloadedUnit == unit && preloaded != null) {
            val ad = preloaded
            preloaded = null
            preloadedUnit = null
            return ad
        }
        return null
    }

    fun show(placement: String, onDismiss: () -> Unit) {
        request = Req(placement, onDismiss)
    }

    fun dismiss() {
        val current = request
        request = null
        current?.onDismiss?.invoke()
    }
}

@Composable
fun NativeInterHost() {
    val req = NativeInterController.request ?: return
    val context = LocalContext.current
    val adUnit = remember(req.placement) { Remote.instance.adUnit(req.placement) }
    var nativeAd by remember(req.placement) { mutableStateOf<NativeAd?>(null) }
    var failed by remember(req.placement) { mutableStateOf(false) }
    var counter by remember(req.placement) { mutableStateOf(3) }

    DisposableEffect(req.placement) {
        val preloaded = NativeInterController.takePreloaded(adUnit)
        when {
            preloaded != null -> {
                println("ADS native-inter ${req.placement} use preload")
                nativeAd = preloaded
            }
            adUnit.isBlank() -> {
                println("ADS ERR native-inter ${req.placement} empty adUnit skip")
                failed = true
            }
            else -> Admob.getInstance().loadNativeAd(
                context.applicationContext,
                adUnit,
                object : NativeCallback() {
                    override fun onNativeAdLoaded(ad: NativeAd?) {
                        if (ad != null) {
                            println("ADS native-inter ${req.placement} loaded")
                            nativeAd = ad
                        } else {
                            println("ADS ERR native-inter ${req.placement} loaded NULL")
                            failed = true
                        }
                    }

                    override fun onAdFailedToLoad() {
                        println("ADS ERR native-inter ${req.placement} LOAD FAILED")
                        failed = true
                    }
                },
            )
        }
        onDispose { nativeAd?.destroy() }
    }

    LaunchedEffect(failed) {
        if (failed) NativeInterController.dismiss()
    }
    LaunchedEffect(nativeAd) {
        if (nativeAd != null) {
            counter = 3
            while (counter > 0) {
                delay(1000)
                counter -= 1
            }
        }
    }

    val ad = nativeAd ?: return
    Dialog(
        onDismissRequest = { if (counter == 0) NativeInterController.dismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
    ) {
        Box(Modifier.fillMaxSize().background(Color.White)) {
            key(ad) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { viewContext ->
                        LayoutInflater.from(viewContext).inflate(R.layout.ad_native_full, null)
                            .also { bindNativeAd(it as NativeAdView, ad) }
                    },
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 12.dp, end = 16.dp)
                    .size(28.dp)
                    .background(Color(0x22000000), CircleShape)
                    .clickable(enabled = counter == 0) { NativeInterController.dismiss() },
                contentAlignment = Alignment.Center,
            ) {
                if (counter == 0) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    Text(counter.toString(), color = Color(0xFF6B7280), fontSize = 14.sp)
                }
            }
        }
    }
}

private fun bindNativeAd(adView: NativeAdView, ad: NativeAd) {
    val media = adView.findViewById<MediaView>(R.id.ad_media)
    adView.mediaView = media

    val headline = adView.findViewById<TextView>(R.id.ad_headline)
    adView.headlineView = headline
    headline.text = ad.headline

    val body = adView.findViewById<TextView>(R.id.ad_body)
    adView.bodyView = body
    body.text = ad.body
    body.visibility = if (ad.body.isNullOrEmpty()) View.GONE else View.VISIBLE

    val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
    adView.advertiserView = advertiser
    advertiser.text = ad.advertiser
    advertiser.visibility = if (ad.advertiser.isNullOrEmpty()) View.GONE else View.VISIBLE

    val icon = adView.findViewById<ImageView>(R.id.ad_app_icon)
    adView.iconView = icon
    val iconDrawable = ad.icon?.drawable
    if (iconDrawable != null) {
        icon.setImageDrawable(iconDrawable)
        icon.visibility = View.VISIBLE
    } else {
        icon.visibility = View.GONE
    }

    val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
    adView.callToActionView = callToAction
    callToAction.text = ad.callToAction.orEmpty()

    adView.setNativeAd(ad)
}
