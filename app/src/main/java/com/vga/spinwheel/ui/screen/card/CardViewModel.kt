package com.vga.spinwheel.ui.screen.card

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

sealed interface CardStage {
    data object Picking : CardStage
    data object Result : CardStage
}

data class CardSettings(
    val durationSeconds: Int = CardViewModel.DEFAULT_DURATION_SECONDS,
    val totalCards: Int = CardViewModel.DEFAULT_TOTAL_CARDS,
    val winners: Int = CardViewModel.DEFAULT_WINNERS,
    val themeIndex: Int = 0,
)

data class FlipCard(
    val id: Int,
    val isWinner: Boolean,
    val isFlipped: Boolean = false,
)

data class CardUiState(
    val settings: CardSettings = CardSettings(),
    val tempThemeIndex: Int = settings.themeIndex,
    val cards: List<FlipCard> = emptyList(),
    val stage: CardStage = CardStage.Picking,
    val isShuffled: Boolean = false,
    val runId: Long = 0L,
) {
    val allCardsFlipped: Boolean
        get() = cards.isNotEmpty() && cards.all { it.isFlipped }
}

@HiltViewModel
class CardViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val themeCount = CardThemes.all.size

    private val _uiState = MutableStateFlow(loadInitialState())
    val uiState: StateFlow<CardUiState> = _uiState.asStateFlow()

    private var resultJob: Job? = null

    fun shuffleCards() {
        startNewRound(isShuffled = true)
    }

    fun resetCards() {
        startNewRound(isShuffled = false)
    }

    fun flipCard(cardId: Int) {
        val state = _uiState.value
        if (!state.isShuffled || state.stage != CardStage.Picking) return

        val target = state.cards.firstOrNull { it.id == cardId } ?: return
        if (target.isFlipped) return

        resultJob?.cancel()
        val runId = state.runId + 1
        val nextCards = state.cards.map { card ->
            if (card.id == cardId) card.copy(isFlipped = true) else card
        }

        _uiState.update {
            it.copy(
                cards = nextCards,
                runId = runId,
            )
        }

        val isWinFound = target.isWinner || nextCards.count { it.isFlipped && it.isWinner } >= state.settings.winners || nextCards.all { it.isFlipped }
        if (isWinFound) {
            resultJob = viewModelScope.launch {
                delay(300L)
                if (isActiveRun(runId)) {
                    _uiState.update { it.copy(stage = CardStage.Result) }
                }
            }
        }
    }

    fun retryFromResult() {
        startNewRound(isShuffled = false)
    }

    fun updateDuration(value: Int) {
        val clamped = CardRoundRules.clampDurationSeconds(value)
        _uiState.update {
            it.copy(settings = it.settings.copy(durationSeconds = clamped))
        }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.CARD, SETTING_DURATION, clamped)
        }
    }

    fun updateTotalCards(value: Int) {
        val state = _uiState.value
        val totalCards = CardRoundRules.clampTotalCards(value)
        val winners = CardRoundRules.clampWinners(state.settings.winners, totalCards)
        applySettings(
            state.settings.copy(
                totalCards = totalCards,
                winners = winners,
            ),
            persistTotalCards = true,
            persistWinners = true,
        )
    }

    fun updateWinners(value: Int) {
        val state = _uiState.value
        val winners = CardRoundRules.clampWinners(value, state.settings.totalCards)
        applySettings(
            state.settings.copy(winners = winners),
            persistWinners = true,
        )
    }

    fun beginThemeSelection() {
        _uiState.update { it.copy(tempThemeIndex = it.settings.themeIndex) }
    }

    fun selectTempTheme(index: Int) {
        val clamped = CardRoundRules.clampThemeIndex(index, themeCount)
        _uiState.update { it.copy(tempThemeIndex = clamped) }
    }

    fun saveSelectedTheme() {
        val themeIndex = CardRoundRules.clampThemeIndex(_uiState.value.tempThemeIndex, themeCount)
        _uiState.update {
            it.copy(
                settings = it.settings.copy(themeIndex = themeIndex),
                tempThemeIndex = themeIndex,
            )
        }
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.CARD, SETTING_THEME_INDEX, themeIndex)
        }
    }

    private fun applySettings(
        settings: CardSettings,
        persistTotalCards: Boolean = false,
        persistWinners: Boolean = false,
    ) {
        resultJob?.cancel()
        _uiState.update {
            it.copy(
                settings = settings,
                cards = createCards(settings.totalCards, settings.winners),
                stage = CardStage.Picking,
                isShuffled = false,
                runId = it.runId + 1,
            )
        }
        viewModelScope.launch {
            if (persistTotalCards) {
                settingsRepository.putInt(RandomFeature.CARD, SETTING_TOTAL_CARDS, settings.totalCards)
            }
            if (persistWinners) {
                settingsRepository.putInt(RandomFeature.CARD, SETTING_WINNERS, settings.winners)
            }
        }
    }

    private fun startNewRound(isShuffled: Boolean = false) {
        resultJob?.cancel()
        _uiState.update {
            it.copy(
                cards = createCards(it.settings.totalCards, it.settings.winners),
                stage = CardStage.Picking,
                isShuffled = isShuffled,
                runId = it.runId + 1,
            )
        }
    }

    private fun loadInitialState(): CardUiState {
        val totalCards = CardRoundRules.clampTotalCards(
            settingsRepository.getInt(
                RandomFeature.CARD,
                SETTING_TOTAL_CARDS,
                DEFAULT_TOTAL_CARDS,
            )
        )
        val settings = CardSettings(
            durationSeconds = CardRoundRules.clampDurationSeconds(
                settingsRepository.getInt(
                    RandomFeature.CARD,
                    SETTING_DURATION,
                    DEFAULT_DURATION_SECONDS,
                )
            ),
            totalCards = totalCards,
            winners = CardRoundRules.clampWinners(
                settingsRepository.getInt(
                    RandomFeature.CARD,
                    SETTING_WINNERS,
                    DEFAULT_WINNERS,
                ),
                totalCards,
            ),
            themeIndex = CardRoundRules.clampThemeIndex(
                settingsRepository.getInt(
                    RandomFeature.CARD,
                    SETTING_THEME_INDEX,
                    0,
                ),
                themeCount,
            ),
        )

        return CardUiState(
            settings = settings,
            tempThemeIndex = settings.themeIndex,
            cards = createCards(settings.totalCards, settings.winners),
        )
    }

    private fun createCards(totalCards: Int, winners: Int): List<FlipCard> =
        CardRoundRules.randomWinnerFlags(totalCards, winners)
            .mapIndexed { index, isWinner ->
                FlipCard(
                    id = index,
                    isWinner = isWinner,
                )
            }

    private fun isActiveRun(runId: Long): Boolean =
        _uiState.value.runId == runId

    private fun resultDelayMillis(durationSeconds: Int): Long =
        (durationSeconds * 200L).coerceIn(700L, 1_500L)

    companion object {
        const val DEFAULT_DURATION_SECONDS = 2
        const val DEFAULT_TOTAL_CARDS = 4
        const val DEFAULT_WINNERS = 1
        private const val SETTING_DURATION = "duration"
        private const val SETTING_TOTAL_CARDS = "total_cards"
        private const val SETTING_WINNERS = "winners"
        private const val SETTING_THEME_INDEX = "theme_index"
    }
}
