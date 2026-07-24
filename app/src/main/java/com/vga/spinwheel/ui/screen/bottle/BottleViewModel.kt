package com.vga.spinwheel.ui.screen.bottle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface BottleStage {
    data object Idle : BottleStage
    data object Spinning : BottleStage
    data object Settled : BottleStage
    data object Result : BottleStage
}

data class BottleUiState(
    val durationSeconds: Int = BottleViewModel.DEFAULT_DURATION_SECONDS,
    val styleIndex: Int = 0,
    val tempStyleIndex: Int = 0,
    val lastAngle: Int = 0,
    val settleDurationMillis: Int = BottleViewModel.DEFAULT_SETTLE_DURATION_MILLIS,
    val stage: BottleStage = BottleStage.Idle,
    val runId: Long = 0L,
) {
    val isSpinning: Boolean
        get() = stage == BottleStage.Spinning

    val isRunning: Boolean
        get() = stage == BottleStage.Spinning || stage == BottleStage.Settled
}

@HiltViewModel
class BottleViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val styleCount = BottleStyles.all.size

    private val _uiState = MutableStateFlow(
        BottleUiState(
            durationSeconds = BottleRoundRules.clampDurationSeconds(
                settingsRepository.getInt(
                    RandomFeature.BOTTLE,
                    SETTING_DURATION,
                    DEFAULT_DURATION_SECONDS,
                )
            ),
            styleIndex = BottleRoundRules.clampStyleIndex(
                settingsRepository.getInt(
                    RandomFeature.BOTTLE,
                    SETTING_STYLE_INDEX,
                    0,
                ),
                styleCount,
            ),
        )
    )
    val uiState: StateFlow<BottleUiState> = _uiState.asStateFlow()

    private var spinJob: Job? = null

    fun startSpin() {
        val state = _uiState.value
        if (state.isRunning) return

        spinJob?.cancel()
        val runId = state.runId + 1
        val totalDurationMillis = state.durationSeconds * 1_000L
        val fastSpinDurationMillis = minOf(FAST_SPIN_DURATION_MILLIS, totalDurationMillis)
        val settleDurationMillis = (totalDurationMillis - fastSpinDurationMillis)
            .coerceAtLeast(0L)
            .toInt()
        _uiState.update {
            it.copy(
                stage = BottleStage.Spinning,
                lastAngle = 0,
                settleDurationMillis = settleDurationMillis,
                runId = runId,
            )
        }

        spinJob = viewModelScope.launch {
            delay(fastSpinDurationMillis)
            if (!isActiveRun(runId)) return@launch

            val finalAngle = BottleRoundRules.randomFinalAngle()
            _uiState.update {
                it.copy(
                    stage = BottleStage.Settled,
                    lastAngle = finalAngle,
                )
            }

            delay(settleDurationMillis.toLong())
            if (!isActiveRun(runId)) return@launch
            _uiState.update { it.copy(stage = BottleStage.Result) }
        }
    }

    fun resetSpin() {
        spinJob?.cancel()
        _uiState.update {
            it.copy(
                stage = BottleStage.Idle,
                lastAngle = 0,
                runId = it.runId + 1,
            )
        }
    }

    fun retryFromResult() {
        spinJob?.cancel()
        _uiState.update {
            it.copy(
                stage = BottleStage.Idle,
                runId = it.runId + 1,
            )
        }
    }

    fun updateDuration(value: Int) {
        val clamped = BottleRoundRules.clampDurationSeconds(value)
        _uiState.update { it.copy(durationSeconds = clamped) }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.BOTTLE, SETTING_DURATION, clamped)
        }
    }

    fun beginStyleSelection() {
        _uiState.update { it.copy(tempStyleIndex = it.styleIndex) }
    }

    fun selectTempStyle(index: Int) {
        val clamped = BottleRoundRules.clampStyleIndex(index, styleCount)
        _uiState.update { it.copy(tempStyleIndex = clamped) }
    }

    fun saveSelectedStyle() {
        val clamped = BottleRoundRules.clampStyleIndex(_uiState.value.tempStyleIndex, styleCount)
        _uiState.update {
            it.copy(
                styleIndex = clamped,
                tempStyleIndex = clamped,
            )
        }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.BOTTLE, SETTING_STYLE_INDEX, clamped)
        }
    }

    fun cancelOngoingSpin() {
        if (!_uiState.value.isRunning) return
        resetSpin()
    }

    private fun isActiveRun(runId: Long): Boolean =
        _uiState.value.runId == runId

    companion object {
        const val DEFAULT_DURATION_SECONDS = 2
        const val DEFAULT_SETTLE_DURATION_MILLIS = 1_000
        private const val SETTING_DURATION = "duration"
        private const val SETTING_STYLE_INDEX = "style_index"
        private const val FAST_SPIN_DURATION_MILLIS = 1_000L
    }
}
