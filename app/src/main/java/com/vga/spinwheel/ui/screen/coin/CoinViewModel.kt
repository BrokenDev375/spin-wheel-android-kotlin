package com.vga.spinwheel.ui.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.repo.CoinSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val repository: CoinSettingsRepository,
) : ViewModel() {

    val duration: StateFlow<Int> = repository.observeDuration()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val currentSkinIndex: StateFlow<Int> = repository.observeLabelIndex()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
        
    val currentSkin: StateFlow<CoinSkin> = currentSkinIndex.map { index ->
        CoinSkins.AllSkins.getOrNull(index) ?: CoinSkins.Default
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CoinSkins.Default)

    private val _tempSkinIndex = MutableStateFlow(0)
    val tempSkinIndex = _tempSkinIndex.asStateFlow()

    private val _headScore = MutableStateFlow(0)
    val headScore: StateFlow<Int> = _headScore.asStateFlow()

    private val _tailScore = MutableStateFlow(0)
    val tailScore: StateFlow<Int> = _tailScore.asStateFlow()

    fun recordResult(isHeads: Boolean) {
        if (isHeads) {
            _headScore.value += 1
        } else {
            _tailScore.value += 1
        }
    }

    fun resetScore() {
        _headScore.value = 0
        _tailScore.value = 0
    }

    fun setDuration(value: Int) {
        viewModelScope.launch {
            repository.setDuration(value.coerceIn(1, 10))
        }
    }

    fun initTempSkinIndex() {
        _tempSkinIndex.value = currentSkinIndex.value
    }

    fun setTempSkinIndex(index: Int) {
        _tempSkinIndex.value = index
    }

    fun saveSkinIndex() {
        viewModelScope.launch {
            repository.setLabelIndex(_tempSkinIndex.value)
        }
    }
}
