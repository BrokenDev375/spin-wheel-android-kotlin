package com.vga.spinwheel.ui.screen.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.data.repo.SettingsRepository
import com.vga.spinwheel.data.repo.WheelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface TeamMatchStatus {
    data object Idle : TeamMatchStatus
    data object Matching : TeamMatchStatus
    data object ReadyForPreview : TeamMatchStatus
}

data class TeamUiState(
    val currentList: Wheel? = null,
    val teams: List<TeamGroup> = emptyList(),
    val teamCount: Int = TeamViewModel.DEFAULT_TEAM_COUNT,
    val durationSeconds: Int = TeamViewModel.DEFAULT_DURATION_SECONDS,
    val status: TeamMatchStatus = TeamMatchStatus.Idle,
    val runId: Long = 0L,
)

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val wheelRepository: WheelRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val wheels: StateFlow<List<Wheel>> = wheelRepository.observeWheels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(
        TeamUiState(
            teamCount = TeamRoundRules.clampTeamCount(
                settingsRepository.getInt(
                    RandomFeature.TEAM,
                    SETTING_TEAM_COUNT,
                    DEFAULT_TEAM_COUNT,
                )
            ),
            durationSeconds = TeamRoundRules.clampDuration(
                settingsRepository.getInt(
                    RandomFeature.TEAM,
                    SETTING_DURATION,
                    DEFAULT_DURATION_SECONDS,
                )
            ),
        )
    )
    val uiState: StateFlow<TeamUiState> = _uiState.asStateFlow()

    private var matchingJob: Job? = null

    fun openList(listId: String) {
        loadList(listId = listId, resetSession = true)
    }

    fun loadList(listId: String) {
        loadList(listId = listId, resetSession = false)
    }

    private fun loadList(listId: String, resetSession: Boolean) {
        val current = _uiState.value.currentList
        if (!resetSession && current?.id == listId) return

        if (resetSession) {
            matchingJob?.cancel()
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentList = wheelRepository.getWheel(listId),
                    teams = emptyList(),
                    status = TeamMatchStatus.Idle,
                    runId = it.runId + 1,
                )
            }
        }
    }

    fun duplicateList(listId: String) {
        viewModelScope.launch {
            wheelRepository.duplicateWheel(listId)
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            wheelRepository.deleteWheel(listId)
        }
    }

    fun updateteamCount(value: Int) {
        val clamped = TeamRoundRules.clampTeamCount(value)
        _uiState.update { it.copy(teamCount = clamped) }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.TEAM, SETTING_TEAM_COUNT, clamped)
        }
    }

    fun updateDuration(value: Int) {
        val clamped = TeamRoundRules.clampDuration(value)
        _uiState.update { it.copy(durationSeconds = clamped) }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.TEAM, SETTING_DURATION, clamped)
        }
    }

    fun startMatching() {
        val state = _uiState.value
        val list = state.currentList ?: return
        val members = TeamRoundRules.memberNames(list.items)
        if (members.size < 2 || state.status == TeamMatchStatus.Matching) return

        matchingJob?.cancel()
        val runId = state.runId + 1
        _uiState.update {
            it.copy(
                status = TeamMatchStatus.Matching,
                runId = runId,
            )
        }

        matchingJob = viewModelScope.launch {
            val startAt = System.currentTimeMillis()
            val durationMs = state.durationSeconds * 1_000L

            while (System.currentTimeMillis() - startAt < durationMs) {
                if (!isActiveRun(runId)) return@launch
                val shuffled = TeamRoundRules.shuffledMembers(members)
                _uiState.update {
                    it.copy(teams = TeamRoundRules.createTeams(shuffled, it.teamCount))
                }
                delay(ANIMATION_TICK_MS)
            }

            if (!isActiveRun(runId)) return@launch
            val finalMembers = TeamRoundRules.shuffledMembers(members)
            _uiState.update {
                it.copy(
                    teams = TeamRoundRules.createTeams(finalMembers, it.teamCount),
                    status = TeamMatchStatus.ReadyForPreview,
                )
            }
        }
    }

    fun resetMatching() {
        matchingJob?.cancel()
        _uiState.update {
            it.copy(
                teams = emptyList(),
                status = TeamMatchStatus.Idle,
                runId = it.runId + 1,
            )
        }
    }

    fun saveCurrentListItems(name: String, items: List<WheelItem>) {
        val list = _uiState.value.currentList ?: return
        val cleanedName = name.trim()
        val cleanedItems = items
            .map {
                it.copy(
                    name = it.name.trim(),
                    priority = it.priority.coerceAtLeast(1),
                )
            }
            .filter { it.name.isNotBlank() }

        if (cleanedName.isBlank() || cleanedItems.size < 2) return

        matchingJob?.cancel()
        viewModelScope.launch {
            val updated = wheelRepository.upsertWheel(
                list.copy(
                    name = cleanedName,
                    items = cleanedItems,
                )
            )
            _uiState.update {
                it.copy(
                    currentList = updated,
                    teams = emptyList(),
                    status = TeamMatchStatus.Idle,
                    runId = it.runId + 1,
                )
            }
        }
    }

    fun retryMatching() {
        resetMatching()
    }

    private fun isActiveRun(runId: Long): Boolean =
        _uiState.value.runId == runId

    companion object {
        const val DEFAULT_TEAM_COUNT = 3
        const val DEFAULT_DURATION_SECONDS = 5
        private const val SETTING_TEAM_COUNT = "group_size"
        private const val SETTING_DURATION = "duration"
        private const val ANIMATION_TICK_MS = 140L
    }
}


