package com.vga.spinwheel.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.AppSettingKeys
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val musicEnabled: Boolean = true,
    val gameSoundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            musicEnabled = settingsRepository.getBoolean(
                RandomFeature.APP,
                AppSettingKeys.MUSIC_ENABLED,
                true,
            ),
            gameSoundEnabled = settingsRepository.getBoolean(
                RandomFeature.APP,
                AppSettingKeys.GAME_SOUND_ENABLED,
                true,
            ),
            vibrationEnabled = settingsRepository.getBoolean(
                RandomFeature.APP,
                AppSettingKeys.VIBRATION_ENABLED,
                true,
            ),
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setMusicEnabled(value: Boolean) {
        updateBoolean(AppSettingKeys.MUSIC_ENABLED, value) {
            copy(musicEnabled = value)
        }
    }

    fun setGameSoundEnabled(value: Boolean) {
        updateBoolean(AppSettingKeys.GAME_SOUND_ENABLED, value) {
            copy(gameSoundEnabled = value)
        }
    }

    fun setVibrationEnabled(value: Boolean) {
        updateBoolean(AppSettingKeys.VIBRATION_ENABLED, value) {
            copy(vibrationEnabled = value)
        }
    }

    private fun updateBoolean(
        key: String,
        value: Boolean,
        reducer: SettingsUiState.() -> SettingsUiState,
    ) {
        _uiState.update { it.reducer() }
        viewModelScope.launch {
            settingsRepository.putBoolean(RandomFeature.APP, key, value)
        }
    }
}
