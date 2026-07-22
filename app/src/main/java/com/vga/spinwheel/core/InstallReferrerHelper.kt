package com.vga.spinwheel.core

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import java.net.URLDecoder
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object InstallReferrerHelper {

    @Volatile
    private var cached: Boolean? = null

    @Volatile
    private var resolving = false

    fun resolve(context: Context) {
        val appContext = context.applicationContext
        cached?.let { return }

        if (AppStorage.isAdsCampaignResolved(appContext)) {
            cached = AppStorage.isAdsCampaign(appContext)
            return
        }

        synchronized(this) {
            if (resolving) return
            resolving = true
        }

        scope.launch {
            resolveFromPlayStore(appContext)
        }
    }

    fun isAdsCampaign(context: Context): Boolean {
        cached?.let { return it }

        val appContext = context.applicationContext
        val stored = AppStorage.isAdsCampaign(appContext)
        if (AppStorage.isAdsCampaignResolved(appContext)) {
            cached = stored
        }
        return stored
    }

    private fun resolveFromPlayStore(context: Context) {
        val client = InstallReferrerClient.newBuilder(context).build()
        try {
            client.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    val isAdsCampaign = when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> runCatching {
                            InstallReferrerClassifier.isAdsCampaign(
                                client.installReferrer.installReferrer,
                            )
                        }.getOrDefault(true)

                        else -> true
                    }
                    cache(context, isAdsCampaign)
                    runCatching { client.endConnection() }
                }

                override fun onInstallReferrerServiceDisconnected() = Unit
            })
        } catch (_: Exception) {
            cache(context, true)
            runCatching { client.endConnection() }
        }
    }

    private fun cache(context: Context, isAdsCampaign: Boolean) {
        AppStorage.setAdsCampaign(context, isAdsCampaign)
        cached = isAdsCampaign
        resolving = false
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}

object InstallReferrerClassifier {

    fun isAdsCampaign(rawReferrer: String?): Boolean {
        if (rawReferrer == null) return true

        val params = runCatching { parseParams(rawReferrer) }.getOrElse { return true }
        val gclid = params["gclid"].orEmpty()
        if (gclid.isNotBlank()) return true

        val medium = params["utm_medium"]
            ?.trim()
            ?.lowercase(Locale.US)

        return when {
            medium.isNullOrBlank() -> false
            medium == "organic" -> false
            medium == "(not set)" || medium == "not set" -> false
            else -> true
        }
    }

    private fun parseParams(rawReferrer: String): Map<String, String> {
        val decoded = URLDecoder.decode(rawReferrer, Charsets.UTF_8.name())
        return decoded
            .substringAfter('?', decoded)
            .split('&')
            .mapNotNull { pair ->
                if (pair.isBlank()) return@mapNotNull null
                val index = pair.indexOf('=')
                if (index < 0) {
                    pair to ""
                } else {
                    pair.substring(0, index) to pair.substring(index + 1)
                }
            }
            .associate { (key, value) ->
                key.trim().lowercase(Locale.US) to value.trim()
            }
    }
}
