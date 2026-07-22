package com.vga.spinwheel.data.repo

import com.vga.spinwheel.data.model.RandomFeature
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CoinSettingsRepository @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    companion object {
        private const val KEY_DURATION = "coin_duration"
        private const val KEY_LABEL_INDEX = "coin_label_index"
    }

    fun observeDuration(): Flow<Int> =
        settingsRepository.observeInt(RandomFeature.COIN, KEY_DURATION, 2)

    suspend fun setDuration(duration: Int) =
        settingsRepository.putInt(RandomFeature.COIN, KEY_DURATION, duration)

    fun observeLabelIndex(): Flow<Int> =
        settingsRepository.observeInt(RandomFeature.COIN, KEY_LABEL_INDEX, 0)

    suspend fun setLabelIndex(index: Int) =
        settingsRepository.putInt(RandomFeature.COIN, KEY_LABEL_INDEX, index)
}
