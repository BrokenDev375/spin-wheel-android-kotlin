package com.vga.spinwheel.ui.screen.wheel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.data.repo.RandomHistoryRepository
import com.vga.spinwheel.data.repo.SettingsRepository
import com.vga.spinwheel.data.repo.WheelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WheelFormState(
    val id: String? = null,
    val name: String = "",
    val items: List<WheelItem> = emptyList(),
    val nameError: String? = null,
    val itemError: String? = null,
)

sealed interface SpinStatus {
    object Idle : SpinStatus
    data class Spinning(val targetAngle: Float, val winner: WheelItem) : SpinStatus
    data class Finished(val winner: WheelItem, val resultId: String) : SpinStatus
}

data class AiTopic(
    val title: String,
    val options: List<String>,
)

@HiltViewModel
class WheelViewModel @Inject constructor(
    private val wheelRepository: WheelRepository,
    private val settingsRepository: SettingsRepository,
    private val historyRepository: RandomHistoryRepository,
) : ViewModel() {

    val wheels: StateFlow<List<Wheel>> = wheelRepository.observeWheels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history = historyRepository.observeHistory(RandomFeature.WHEEL)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wheel Settings
    private val _duration = MutableStateFlow(
        settingsRepository.getInt(RandomFeature.WHEEL, SETTING_DURATION, 5)
    )
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _paletteIndex = MutableStateFlow(
        settingsRepository.getInt(RandomFeature.WHEEL, SETTING_PALETTE, 0)
    )
    val paletteIndex: StateFlow<Int> = _paletteIndex.asStateFlow()

    private val _removeWinner = MutableStateFlow(
        settingsRepository.getBoolean(RandomFeature.WHEEL, SETTING_REMOVE_WINNER, false)
    )
    val removeWinner: StateFlow<Boolean> = _removeWinner.asStateFlow()

    // Form State (Add / Edit)
    private val _formState = MutableStateFlow(WheelFormState())
    val formState: StateFlow<WheelFormState> = _formState.asStateFlow()

    // Spin State
    private val _currentWheel = MutableStateFlow<Wheel?>(null)
    val currentWheel: StateFlow<Wheel?> = _currentWheel.asStateFlow()

    private val _activeItems = MutableStateFlow<List<WheelItem>>(emptyList())
    val activeItems: StateFlow<List<WheelItem>> = _activeItems.asStateFlow()

    private val _spinStatus = MutableStateFlow<SpinStatus>(SpinStatus.Idle)
    val spinStatus: StateFlow<SpinStatus> = _spinStatus.asStateFlow()

    // Modals
    private val _showAddManyModal = MutableStateFlow(false)
    val showAddManyModal: StateFlow<Boolean> = _showAddManyModal.asStateFlow()

    private val _showAiModal = MutableStateFlow(false)
    val showAiModal: StateFlow<Boolean> = _showAiModal.asStateFlow()

    val aiTopics = listOf(
        AiTopic("Món ăn hôm nay", listOf("Phở Bò", "Bún Chả", "Cơm Tấm", "Bánh Mì", "Lẩu Thái", "Bún Bò Huế", "Pizza", "Sushi")),
        AiTopic("Đi đâu chơi cuối tuần", listOf("Đi Phượt", "Xem Phim", "Cà Phê Sách", "Bowling", "Đi Siêu Thị", "Về Quê")),
        AiTopic("Hình phạt vui party", listOf("Uống 1 ly", "Hát 1 bài", "Nhảy Sexy", "Kể bí mật", "Chống đẩy 10 cái", "Làm mặt xấu")),
        AiTopic("Trò chơi nhóm", listOf("Ma Sói", "Thật hay Thách", "Uno", "Mèo Nổ", "Cờ Tỷ Phú", "Nối Từ")),
    )

    fun loadWheelForSpin(wheelId: String) {
        viewModelScope.launch {
            val wheel = wheelRepository.getWheel(wheelId)
            _currentWheel.value = wheel
            _activeItems.value = wheel?.items ?: emptyList()
            _spinStatus.value = SpinStatus.Idle
        }
    }

    fun prepareNewForm() {
        _formState.value = WheelFormState(
            id = null,
            name = "",
            items = listOf(
                WheelItem(id = "item-1", name = "Tùy chọn 1", priority = 1),
                WheelItem(id = "item-2", name = "Tùy chọn 2", priority = 1),
            ),
        )
    }

    fun prepareEditForm(wheelId: String) {
        viewModelScope.launch {
            val wheel = wheelRepository.getWheel(wheelId)
            if (wheel != null) {
                _formState.value = WheelFormState(
                    id = wheel.id,
                    name = wheel.name,
                    items = wheel.items,
                )
            }
        }
    }

    fun updateFormName(name: String) {
        _formState.value = _formState.value.copy(name = name, nameError = null)
    }

    fun updateItemName(itemId: String, name: String) {
        val updated = _formState.value.items.map { item ->
            if (item.id == itemId) item.copy(name = name) else item
        }
        _formState.value = _formState.value.copy(items = updated, itemError = null)
    }

    fun changeItemPriority(itemId: String, delta: Int) {
        val updated = WheelFormRules.changePriority(
            items = _formState.value.items,
            itemId = itemId,
            delta = delta,
        )
        _formState.value = _formState.value.copy(items = updated, itemError = null)
    }

    fun addSingleItem() {
        val nextIndex = _formState.value.items.size + 1
        val newItem = WheelItem(
            id = "item-${System.currentTimeMillis()}-$nextIndex",
            name = "Tùy chọn $nextIndex",
            priority = 1,
        )
        _formState.value = _formState.value.copy(
            items = _formState.value.items + newItem,
            itemError = null,
        )
    }

    fun removeItem(itemId: String) {
        val updated = _formState.value.items.filterNot { it.id == itemId }
        _formState.value = _formState.value.copy(items = updated)
    }

    fun addManyItems(text: String) {
        val lines = text.split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
        if (lines.isEmpty()) return

        val newItems = lines.mapIndexed { index, line ->
            WheelItem(
                id = "item-${System.currentTimeMillis()}-$index",
                name = line,
                priority = 1,
            )
        }
        _formState.value = _formState.value.copy(
            items = _formState.value.items + newItems,
            itemError = null,
        )
        _showAddManyModal.value = false
    }

    fun generateAiWheel(topic: AiTopic) {
        _formState.value = WheelFormState(
            id = null,
            name = topic.title,
            items = topic.options.mapIndexed { index, name ->
                WheelItem(id = "item-ai-$index", name = name, priority = 1)
            },
        )
        _showAiModal.value = false
    }

    fun generateAiCustom(prompt: String) {
        val trimmed = prompt.trim()
        if (trimmed.isBlank()) return
        val generatedItems = listOf(
            "$trimmed 1", "$trimmed 2", "$trimmed 3", "$trimmed 4"
        ).mapIndexed { index, name ->
            WheelItem(id = "item-custom-$index", name = name, priority = 1)
        }
        _formState.value = WheelFormState(
            id = null,
            name = trimmed,
            items = generatedItems,
        )
        _showAiModal.value = false
    }

    fun showAddManyModal(show: Boolean) { _showAddManyModal.value = show }
    fun showAiModal(show: Boolean) { _showAiModal.value = show }

    fun validateAndSave(onSuccess: () -> Unit) {
        val state = _formState.value
        val nameTrimmed = state.name.trim()
        val validItems = state.items.filter { it.name.trim().isNotBlank() }

        var nameErr: String? = null
        var itemErr: String? = null

        if (nameTrimmed.isBlank()) {
            nameErr = "Vui lòng nhập tên bánh xe!"
        }
        if (validItems.isEmpty()) {
            itemErr = "Vui lòng nhập tên cho các tùy chọn!"
        } else if (validItems.size < 2) {
            itemErr = "Bánh xe cần ít nhất 2 tùy chọn hợp lệ!"
        }

        if (nameErr != null || itemErr != null) {
            _formState.value = state.copy(nameError = nameErr, itemError = itemErr)
            return
        }

        viewModelScope.launch {
            if (state.id != null) {
                val existing = wheelRepository.getWheel(state.id)
                if (existing != null) {
                    val updated = existing.copy(
                        name = nameTrimmed,
                        items = validItems.map { it.copy(name = it.name.trim()) },
                    )
                    wheelRepository.upsertWheel(updated)
                }
            } else {
                wheelRepository.createWheel(
                    name = nameTrimmed,
                    itemNames = validItems.map { it.name.trim() },
                )
            }
            onSuccess()
        }
    }

    fun duplicateWheel(wheelId: String) {
        viewModelScope.launch {
            wheelRepository.duplicateWheel(wheelId)
        }
    }

    fun deleteWheel(wheelId: String) {
        viewModelScope.launch {
            wheelRepository.deleteWheel(wheelId)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearFeature(RandomFeature.WHEEL)
        }
    }

    fun startSpin() {
        val items = _activeItems.value
        if (items.size < 2 || _spinStatus.value is SpinStatus.Spinning) return

        // Priority-weighted random selection
        val totalWeight = items.sumOf { it.priority }
        val randomVal = Random.nextInt(totalWeight)

        var accumulated = 0
        var winningIndex = 0
        for (i in items.indices) {
            accumulated += items[i].priority
            if (randomVal < accumulated) {
                winningIndex = i
                break
            }
        }
        val winner = items[winningIndex]

        // Calculate angle for sector
        val totalSectors = items.size
        val sectorAngle = 360f / totalSectors
        val winnerCenterAngle = winningIndex * sectorAngle + (sectorAngle / 2f)

        // Target angle: Pointer is at top (270 degrees or -90 degrees).
        // Rotation degrees to bring winnerCenterAngle to 270 (top):
        val targetPointerAngle = 270f
        val deltaToTop = (targetPointerAngle - winnerCenterAngle + 360f) % 360f

        val fullSpins = Random.nextInt(5, 9) * 360f
        val finalAngle = fullSpins + deltaToTop

        _spinStatus.value = SpinStatus.Spinning(targetAngle = finalAngle, winner = winner)
    }

    fun onSpinCompleted(winner: WheelItem) {
        val wheel = _currentWheel.value ?: return
        viewModelScope.launch {
            val result = historyRepository.addResult(
                feature = RandomFeature.WHEEL,
                title = wheel.name,
                value = winner.name,
                sourceId = wheel.id,
            )

            if (_removeWinner.value) {
                val remaining = _activeItems.value.filterNot { it.id == winner.id }
                if (remaining.size >= 2) {
                    _activeItems.value = remaining
                }
            }
            _spinStatus.value = SpinStatus.Finished(winner = winner, resultId = result.id)
        }
    }

    fun resetSpin() {
        _currentWheel.value?.let { wheel ->
            _activeItems.value = wheel.items
        }
        _spinStatus.value = SpinStatus.Idle
    }

    fun shuffleActiveItems() {
        _activeItems.value = _activeItems.value.shuffled()
    }

    fun updateDuration(newDuration: Int) {
        val clamped = newDuration.coerceIn(1, 10)
        _duration.value = clamped
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.WHEEL, SETTING_DURATION, clamped)
        }
    }

    fun updatePalette(paletteIdx: Int) {
        _paletteIndex.value = paletteIdx
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.WHEEL, SETTING_PALETTE, paletteIdx)
        }
    }

    fun toggleRemoveWinner(value: Boolean) {
        _removeWinner.value = value
        viewModelScope.launch {
            settingsRepository.putBoolean(RandomFeature.WHEEL, SETTING_REMOVE_WINNER, value)
        }
    }

    companion object {
        private const val SETTING_DURATION = "duration"
        private const val SETTING_PALETTE = "palette_index"
        private const val SETTING_REMOVE_WINNER = "remove_winner"
    }
}
