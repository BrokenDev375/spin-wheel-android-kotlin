package com.vga.spinwheel.data.repo

import com.vga.spinwheel.data.model.RandomFeature
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class NumberSettingsRepository @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    companion object {
        private const val KEY_MIN = "number_min"
        private const val KEY_MAX = "number_max"
        private const val KEY_COUNT = "number_count"
        private const val KEY_ALLOW_DUPLICATES = "number_allow_duplicates"
        private const val KEY_DURATION = "number_duration"
    }

    fun observeMin(): Flow<Int> =
        settingsRepository.observeInt(RandomFeature.NUMBER, KEY_MIN, 1)

    suspend fun setMin(min: Int) =
        settingsRepository.putInt(RandomFeature.NUMBER, KEY_MIN, min)

    fun observeMax(): Flow<Int> =
        settingsRepository.observeInt(RandomFeature.NUMBER, KEY_MAX, 10)

    suspend fun setMax(max: Int) =
        settingsRepository.putInt(RandomFeature.NUMBER, KEY_MAX, max)

    fun observeCount(): Flow<Int> =
        settingsRepository.observeInt(RandomFeature.NUMBER, KEY_COUNT, 1)

    suspend fun setCount(count: Int) =
        settingsRepository.putInt(RandomFeature.NUMBER, KEY_COUNT, count)

    fun observeAllowDuplicates(): Flow<Boolean> =
        settingsRepository.observeBoolean(RandomFeature.NUMBER, KEY_ALLOW_DUPLICATES, false)

    suspend fun setAllowDuplicates(allowDuplicates: Boolean) =
        settingsRepository.putBoolean(RandomFeature.NUMBER, KEY_ALLOW_DUPLICATES, allowDuplicates)

    fun observeDuration(): Flow<Int> =
        settingsRepository.observeInt(RandomFeature.NUMBER, KEY_DURATION, 1) // 0: 1s, 1: 2s, 2: 3s

    suspend fun setDuration(duration: Int) =
        settingsRepository.putInt(RandomFeature.NUMBER, KEY_DURATION, duration)
}
