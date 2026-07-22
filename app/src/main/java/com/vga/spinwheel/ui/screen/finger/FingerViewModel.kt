package com.vga.spinwheel.ui.screen.finger

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

sealed interface FingerStage {
    data object Waiting : FingerStage
    data class CountingDown(val secondsLeft: Int) : FingerStage
    data object QuickResult : FingerStage
    data object FinalResult : FingerStage
}

data class FingerUiState(
    val fingerCount: Int = FingerViewModel.DEFAULT_FINGER_COUNT,
    val points: List<FingerPoint> = emptyList(),
    val stage: FingerStage = FingerStage.Waiting,
    val winnerId: Long? = null,
    val runId: Long = 0L,
) {
    val winner: FingerPoint?
        get() = points.firstOrNull { it.id == winnerId }
}

@HiltViewModel
class FingerViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FingerUiState(
            fingerCount = FingerRoundRules.clampFingerCount(
                settingsRepository.getInt(
                    RandomFeature.FINGER,
                    SETTING_FINGER_COUNT,
                    DEFAULT_FINGER_COUNT,
                )
            )
        )
    )
    val uiState: StateFlow<FingerUiState> = _uiState.asStateFlow()

    private var roundJob: Job? = null

    fun selectFingerCount(count: Int) {
        val clamped = FingerRoundRules.clampFingerCount(count)
        roundJob?.cancel()
        _uiState.update { state ->
            FingerUiState(
                fingerCount = clamped,
                runId = state.runId + 1,
            )
        }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.FINGER, SETTING_FINGER_COUNT, clamped)
        }
    }

    fun onTouchesChanged(
        touches: List<FingerTouchInput>,
        width: Float,
        height: Float,
    ) {
        val state = _uiState.value
        if (state.stage == FingerStage.QuickResult || state.stage == FingerStage.FinalResult) {
            return
        }

        val points = FingerRoundRules.normalizeTouches(
            touches = touches,
            width = width,
            height = height,
            fingerCount = state.fingerCount,
        )

        if (state.stage is FingerStage.CountingDown) {
            if (FingerRoundRules.hasRequiredTouches(points, state.fingerCount)) {
                _uiState.update { it.copy(points = points) }
            } else {
                cancelRound(points)
            }
            return
        }

        _uiState.update {
            it.copy(
                points = points,
                winnerId = null,
            )
        }

        if (FingerRoundRules.hasRequiredTouches(points, state.fingerCount)) {
            beginCountdown()
        }
    }

    fun retry() {
        cancelRound()
    }

    fun cancelRound() {
        cancelRound(points = emptyList())
    }

    private fun cancelRound(points: List<FingerPoint>) {
        roundJob?.cancel()
        _uiState.update { state ->
            state.copy(
                points = points,
                stage = FingerStage.Waiting,
                winnerId = null,
                runId = state.runId + 1,
            )
        }
    }

    private fun beginCountdown() {
        roundJob?.cancel()
        val runId = _uiState.value.runId + 1
        _uiState.update {
            it.copy(
                stage = FingerStage.CountingDown(secondsLeft = 2),
                runId = runId,
            )
        }

        roundJob = viewModelScope.launch {
            delay(ONE_SECOND_MS)
            if (!isActiveRun(runId)) return@launch
            _uiState.update {
                it.copy(stage = FingerStage.CountingDown(secondsLeft = 1))
            }

            delay(ONE_SECOND_MS)
            if (!isActiveRun(runId)) return@launch
            val resultState = _uiState.value
            val winner = FingerRoundRules.chooseWinner(resultState.points)
            _uiState.update {
                it.copy(
                    stage = FingerStage.QuickResult,
                    winnerId = winner.id,
                )
            }

            delay(QUICK_RESULT_MS)
            if (!isActiveRun(runId)) return@launch
            _uiState.update {
                it.copy(stage = FingerStage.FinalResult)
            }
        }
    }

    private fun isActiveRun(runId: Long): Boolean =
        _uiState.value.runId == runId

    companion object {
        const val DEFAULT_FINGER_COUNT = 2
        private const val SETTING_FINGER_COUNT = "finger_count"
        private const val ONE_SECOND_MS = 1_000L
        private const val QUICK_RESULT_MS = 1_800L
    }
}
