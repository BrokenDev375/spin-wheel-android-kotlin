package com.vga.spinwheel.data.repo

import android.content.SharedPreferences
import com.vga.spinwheel.core.di.AppDispatchers
import com.vga.spinwheel.data.model.RandomFeature
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Singleton
class SettingsRepository @Inject constructor(
    private val preferences: SharedPreferences,
    private val dispatchers: AppDispatchers,
) {

    fun getBoolean(feature: RandomFeature, key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(composeKey(feature, key), defaultValue)

    suspend fun putBoolean(feature: RandomFeature, key: String, value: Boolean) =
        edit { putBoolean(composeKey(feature, key), value) }

    fun observeBoolean(
        feature: RandomFeature,
        key: String,
        defaultValue: Boolean,
    ): Flow<Boolean> = observe(composeKey(feature, key)) {
        preferences.getBoolean(composeKey(feature, key), defaultValue)
    }

    fun getInt(feature: RandomFeature, key: String, defaultValue: Int): Int =
        preferences.getInt(composeKey(feature, key), defaultValue)

    suspend fun putInt(feature: RandomFeature, key: String, value: Int) =
        edit { putInt(composeKey(feature, key), value) }

    fun observeInt(
        feature: RandomFeature,
        key: String,
        defaultValue: Int,
    ): Flow<Int> = observe(composeKey(feature, key)) {
        preferences.getInt(composeKey(feature, key), defaultValue)
    }

    fun getLong(feature: RandomFeature, key: String, defaultValue: Long): Long =
        preferences.getLong(composeKey(feature, key), defaultValue)

    suspend fun putLong(feature: RandomFeature, key: String, value: Long) =
        edit { putLong(composeKey(feature, key), value) }

    fun observeLong(
        feature: RandomFeature,
        key: String,
        defaultValue: Long,
    ): Flow<Long> = observe(composeKey(feature, key)) {
        preferences.getLong(composeKey(feature, key), defaultValue)
    }

    fun getString(feature: RandomFeature, key: String, defaultValue: String): String =
        preferences.getString(composeKey(feature, key), defaultValue) ?: defaultValue

    suspend fun putString(feature: RandomFeature, key: String, value: String) =
        edit { putString(composeKey(feature, key), value) }

    fun observeString(
        feature: RandomFeature,
        key: String,
        defaultValue: String,
    ): Flow<String> = observe(composeKey(feature, key)) {
        preferences.getString(composeKey(feature, key), defaultValue) ?: defaultValue
    }

    fun getFeatureSettings(feature: RandomFeature): Map<String, *> {
        val prefix = featurePrefix(feature)
        return preferences.all
            .filterKeys { it.startsWith(prefix) }
            .mapKeys { (key, _) -> key.removePrefix(prefix) }
    }

    suspend fun remove(feature: RandomFeature, key: String) =
        edit { remove(composeKey(feature, key)) }

    suspend fun clearFeature(feature: RandomFeature) = withContext(dispatchers.io) {
        val prefix = featurePrefix(feature)
        val keys = preferences.all.keys.filter { it.startsWith(prefix) }
        preferences.edit().apply {
            keys.forEach { remove(it) }
        }.apply()
    }

    private fun <T> observe(
        storageKey: String,
        read: () -> T,
    ): Flow<T> = callbackFlow {
        trySend(read())
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == storageKey) {
                trySend(read())
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged().flowOn(dispatchers.io)

    private suspend fun edit(block: SharedPreferences.Editor.() -> SharedPreferences.Editor) =
        withContext(dispatchers.io) {
            preferences.edit().block().apply()
        }

    private fun composeKey(feature: RandomFeature, key: String): String =
        "${feature.storageKey}.$key"

    private fun featurePrefix(feature: RandomFeature): String = "${feature.storageKey}."
}
