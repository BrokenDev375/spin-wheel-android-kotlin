package com.vga.spinwheel.ui.screen.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.Wheel
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
    val groupSize: Int = TeamViewModel.DEFAULT_GROUP_SIZE,
    val durationSeconds: Int = TeamViewModel.DEFAULT_DURATION_SECONDS,
    val seedEnabled: Boolean = false,
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
            groupSize = TeamRoundRules.clampGroupSize(
                settingsRepository.getInt(
                    RandomFeature.TEAM,
                    SETTING_GROUP_SIZE,
                    DEFAULT_GROUP_SIZE,
                )
            ),
            durationSeconds = TeamRoundRules.clampDuration(
                settingsRepository.getInt(
                    RandomFeature.TEAM,
                    SETTING_DURATION,
                    DEFAULT_DURATION_SECONDS,
                )
            ),
            seedEnabled = settingsRepository.getBoolean(
                RandomFeature.TEAM,
                SETTING_SEED_ENABLED,
                false,
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

    fun updateGroupSize(value: Int) {
        val clamped = TeamRoundRules.clampGroupSize(value)
        _uiState.update { it.copy(groupSize = clamped) }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.TEAM, SETTING_GROUP_SIZE, clamped)
        }
    }

    fun updateDuration(value: Int) {
        val clamped = TeamRoundRules.clampDuration(value)
        _uiState.update { it.copy(durationSeconds = clamped) }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.TEAM, SETTING_DURATION, clamped)
        }
    }

    fun toggleSeedEnabled(value: Boolean) {
        _uiState.update { it.copy(seedEnabled = value) }
        viewModelScope.launch {
            settingsRepository.putBoolean(RandomFeature.TEAM, SETTING_SEED_ENABLED, value)
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
            var tick = 0L

            while (System.currentTimeMillis() - startAt < durationMs) {
                if (!isActiveRun(runId)) return@launch
                val shuffled = TeamRoundRules.shuffledMembers(
                    members = members,
                    seedEnabled = false,
                    seed = seedFor(list, tick),
                )
                _uiState.update {
                    it.copy(teams = TeamRoundRules.createTeams(shuffled, it.groupSize))
                }
                tick++
                delay(ANIMATION_TICK_MS)
            }

            if (!isActiveRun(runId)) return@launch
            val finalMembers = TeamRoundRules.shuffledMembers(
                members = members,
                seedEnabled = _uiState.value.seedEnabled,
                seed = seedFor(list, 0L),
            )
            _uiState.update {
                it.copy(
                    teams = TeamRoundRules.createTeams(finalMembers, it.groupSize),
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

    fun retryMatching() {
        resetMatching()
        startMatching()
    }

    fun shareText(): String {
        val state = _uiState.value
        val title = state.currentList?.name ?: "Doi"
        val teamText = state.teams.joinToString(separator = "\n\n") { team ->
            "${team.title}\n" + team.members.joinToString(separator = "\n") { "- $it" }
        }
        return "$title\n\n$teamText"
    }

    private fun isActiveRun(runId: Long): Boolean =
        _uiState.value.runId == runId

    private fun seedFor(list: Wheel, offset: Long): Long =
        list.id.hashCode().toLong() + list.updatedAt + offset

    companion object {
        const val DEFAULT_GROUP_SIZE = 3
        const val DEFAULT_DURATION_SECONDS = 5
        private const val SETTING_GROUP_SIZE = "group_size"
        private const val SETTING_DURATION = "duration"
        private const val SETTING_SEED_ENABLED = "seed_enabled"
        private const val ANIMATION_TICK_MS = 140L
    }
}
