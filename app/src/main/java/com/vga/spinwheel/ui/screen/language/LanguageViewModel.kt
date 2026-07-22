package com.vga.spinwheel.ui.screen.language

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

data class LanguageOption(
    val code: String,
    val localName: String,
    val englishName: String,
    val flag: String,
)

data class LanguageUiState(
    val query: String = "",
    val selectedCode: String = DEFAULT_LANGUAGE_CODE,
)

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LanguageUiState(
            selectedCode = settingsRepository.getString(
                RandomFeature.APP,
                AppSettingKeys.LANGUAGE_CODE,
                DEFAULT_LANGUAGE_CODE,
            )
        )
    )
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    val languages: List<LanguageOption> = defaultLanguages

    fun setQuery(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun selectLanguage(code: String) {
        _uiState.update { it.copy(selectedCode = code) }
    }

    fun saveSelection(onSaved: () -> Unit) {
        val code = _uiState.value.selectedCode
        viewModelScope.launch {
            settingsRepository.putString(
                feature = RandomFeature.APP,
                key = AppSettingKeys.LANGUAGE_CODE,
                value = code,
            )
            onSaved()
        }
    }
}

private const val DEFAULT_LANGUAGE_CODE = "vi"

private val defaultLanguages = listOf(
    LanguageOption("vi", "Tiếng Việt", "Vietnamese", "VN"),
    LanguageOption("ru", "русский", "Russian", "RU"),
    LanguageOption("hi", "हिन्दी", "Hindi", "IN"),
    LanguageOption("nl", "Nederlands", "Dutch", "NL"),
    LanguageOption("tr", "Türk", "Turkish", "TR"),
    LanguageOption("zh", "简体中文", "Chinese (Simplified)", "CN"),
    LanguageOption("zh-TW", "繁體中文", "Chinese (Traditional)", "TW"),
    LanguageOption("fa", "فارسی", "Persian", "IR"),
    LanguageOption("uk", "Українська", "Ukrainian", "UA"),
    LanguageOption("en", "English", "English", "US"),
)
