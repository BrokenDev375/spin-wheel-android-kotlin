package com.vga.spinwheel.ui.screen.language

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.vga.spinwheel.R
import com.vga.spinwheel.core.AppStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LanguageOption(
    val code: String,
    @StringRes val localNameRes: Int,
    @StringRes val englishNameRes: Int,
    val flag: String,
)

data class LanguageUiState(
    val query: String = "",
    val selectedCode: String = DEFAULT_LANGUAGE_CODE,
)

@HiltViewModel
class LanguageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LanguageUiState(
            selectedCode = AppStorage.languageCode(context),
        )
    )
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    val languages: List<LanguageOption> = supportedLanguages

    fun setQuery(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun selectLanguage(code: String) {
        _uiState.update { it.copy(selectedCode = code) }
    }

    fun selectedCode(): String = _uiState.value.selectedCode

    fun saveSelection(onSaved: () -> Unit) {
        onSaved()
    }
}

private const val DEFAULT_LANGUAGE_CODE = "vi"

private val supportedLanguages = listOf(
    LanguageOption(
        code = "vi",
        localNameRes = R.string.language_option_vi_local,
        englishNameRes = R.string.language_option_vi_english,
        flag = "VN",
    ),
    LanguageOption(
        code = "en",
        localNameRes = R.string.language_option_en_local,
        englishNameRes = R.string.language_option_en_english,
        flag = "US",
    ),
)
