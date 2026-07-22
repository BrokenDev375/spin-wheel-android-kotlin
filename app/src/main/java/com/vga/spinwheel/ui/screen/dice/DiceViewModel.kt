package com.vga.spinwheel.ui.screen.dice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class DiceUiState(
    val diceCount: Int = 1,
    val duration: Int = 3,
    val styleIndex: Int = 0,
    val tempStyleIndex: Int = 0,
    val currentResults: List<Int> = emptyList(),
    val isRolling: Boolean = false,
)

@HiltViewModel
class DiceViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _currentResults = MutableStateFlow<List<Int>>(emptyList())
    private val _isRolling = MutableStateFlow(false)
    private val _tempStyleIndex = MutableStateFlow(0)

    val uiState: StateFlow<DiceUiState> = combine(
        combine(
            settingsRepository.observeInt(RandomFeature.DICE, "count", 1),
            settingsRepository.observeInt(RandomFeature.DICE, "duration", 3),
            settingsRepository.observeInt(RandomFeature.DICE, "style", 0)
        ) { count, duration, style -> Triple(count, duration, style) },
        _tempStyleIndex,
        _currentResults,
        _isRolling
    ) { (count, duration, style), tempStyle, results, isRolling ->
        DiceUiState(
            diceCount = count,
            duration = duration,
            styleIndex = style,
            tempStyleIndex = tempStyle,
            currentResults = results,
            isRolling = isRolling
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DiceUiState()
    )

    private var rollJob: Job? = null

    init {
        viewModelScope.launch {
            val style = settingsRepository.getInt(RandomFeature.DICE, "style", 0)
            _tempStyleIndex.value = style
        }
    }

    fun setDiceCount(count: Int) {
        val validCount = count.coerceIn(1, 6)
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.DICE, "count", validCount)
            resetResults()
        }
    }

    fun setDuration(duration: Int) {
        val validDuration = duration.coerceIn(1, 15)
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.DICE, "duration", validDuration)
        }
    }

    fun setTempStyleIndex(index: Int) {
        _tempStyleIndex.value = index
    }

    fun saveStyleIndex() {
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.DICE, "style", _tempStyleIndex.value)
        }
    }

    fun resetResults() {
        rollJob?.cancel()
        _isRolling.value = false
        _currentResults.value = emptyList()
    }

    fun startRoll(onFinished: () -> Unit) {
        if (_isRolling.value) return
        
        rollJob?.cancel()
        rollJob = viewModelScope.launch {
            _isRolling.value = true
            val count = uiState.value.diceCount
            val duration = uiState.value.duration
            val endTime = System.currentTimeMillis() + (duration * 1000L)

            while (System.currentTimeMillis() < endTime) {
                _currentResults.value = List(count) { Random.nextInt(1, 7) }
                delay(100)
            }

            // Final result
            _currentResults.value = List(count) { Random.nextInt(1, 7) }
            _isRolling.value = false
            delay(500)
            onFinished()
        }
    }
}
