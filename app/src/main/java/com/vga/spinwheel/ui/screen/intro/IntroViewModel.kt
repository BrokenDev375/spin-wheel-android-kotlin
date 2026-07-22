package com.vga.spinwheel.ui.screen.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.AppSettingKeys
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    fun markIntroDone(onSaved: () -> Unit) {
        viewModelScope.launch {
            settingsRepository.putBoolean(
                feature = RandomFeature.APP,
                key = AppSettingKeys.INTRO_DONE,
                value = true,
            )
            onSaved()
        }
    }
}
