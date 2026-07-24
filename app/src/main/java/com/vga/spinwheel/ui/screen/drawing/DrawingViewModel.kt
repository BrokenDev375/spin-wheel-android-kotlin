package com.vga.spinwheel.ui.screen.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.data.repo.RandomHistoryRepository
import com.vga.spinwheel.data.repo.SettingsRepository
import com.vga.spinwheel.data.repo.WheelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class DrawingFormState(
    val id: String = "",
    val name: String = "",
    val items: List<WheelItem> = emptyList(),
)

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val wheelRepository: WheelRepository,
    private val settingsRepository: SettingsRepository,
    private val historyRepository: RandomHistoryRepository,
) : ViewModel() {

    companion object {
        const val SETTING_DURATION = "drawing_duration"
        const val SETTING_THEME = "drawing_theme"

        val DRAWING_FALLBACK_WHEEL = Wheel(
            id = "drawing-demo",
            name = "demo",
            items = listOf(
                WheelItem(id = "1", name = "Vòng quay miễn phí", priority = 1),
                WheelItem(id = "2", name = "Bảng màu", priority = 1),
                WheelItem(id = "3", name = "Chọn ngẫu nhiên", priority = 1),
                WheelItem(id = "4", name = "Chủ đề vui", priority = 1),
                WheelItem(id = "5", name = "Ý tưởng mới", priority = 1),
            ),
            createdAt = 0L,
            updatedAt = 0L
        )
    }

    val wheels: StateFlow<List<Wheel>> = wheelRepository.observeWheels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val duration: StateFlow<Int> = settingsRepository.observeInt(RandomFeature.DRAWING, SETTING_DURATION, 2)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val themeIndex: StateFlow<Int> = settingsRepository.observeInt(RandomFeature.DRAWING, SETTING_THEME, 0)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _currentWheel = MutableStateFlow<Wheel?>(null)
    val currentWheel: StateFlow<Wheel?> = _currentWheel.asStateFlow()

    private val _lastResult = MutableStateFlow<WheelItem?>(null)
    val lastResult = _lastResult.asStateFlow()
    
    // For temp settings
    private val _tempDuration = MutableStateFlow(2)
    val tempDuration = _tempDuration.asStateFlow()

    private val _tempThemeIndex = MutableStateFlow(0)
    val tempThemeIndex = _tempThemeIndex.asStateFlow()

    // Form state
    private val _formState = MutableStateFlow(DrawingFormState())
    val formState: StateFlow<DrawingFormState> = _formState.asStateFlow()

    fun loadWheelForDrawing(wheelId: String) {
        viewModelScope.launch {
            val wheel = wheelRepository.getWheel(wheelId)
            _currentWheel.value = wheel ?: DRAWING_FALLBACK_WHEEL
        }
    }

    fun initTempSettings() {
        _tempDuration.value = duration.value
        _tempThemeIndex.value = themeIndex.value
    }

    fun setTempDuration(value: Int) {
        _tempDuration.value = value
    }

    fun setTempThemeIndex(value: Int) {
        _tempThemeIndex.value = value
    }

    fun saveSettings() {
        viewModelScope.launch {
            settingsRepository.putInt(RandomFeature.DRAWING, SETTING_DURATION, _tempDuration.value)
            settingsRepository.putInt(RandomFeature.DRAWING, SETTING_THEME, _tempThemeIndex.value)
        }
    }

    fun drawItem() {
        val wheel = _currentWheel.value ?: return
        if (wheel.items.isEmpty()) return

        val winner = wheel.items.random()
        _lastResult.value = winner
        
        viewModelScope.launch {
            historyRepository.addResult(
                feature = RandomFeature.DRAWING,
                title = wheel.name,
                value = winner.name
            )
        }
    }

    fun prepareNewForm() {
        _formState.value = DrawingFormState(
            id = UUID.randomUUID().toString(),
            name = "",
            items = listOf(
                WheelItem(id = UUID.randomUUID().toString(), name = "Tùy chọn 1", priority = 1),
                WheelItem(id = UUID.randomUUID().toString(), name = "Tùy chọn 2", priority = 1),
            )
        )
    }

    fun prepareEditForm(wheelId: String) {
        viewModelScope.launch {
            val wheel = wheelRepository.getWheel(wheelId)
            if (wheel != null) {
                _formState.value = DrawingFormState(
                    id = wheel.id,
                    name = wheel.name,
                    items = wheel.items
                )
            }
        }
    }

    fun generateAiWheel(topic: String) {
        val items = mutableListOf<WheelItem>()
        for (i in 1..5) {
            items.add(WheelItem(id = UUID.randomUUID().toString(), name = "$topic $i", priority = 1))
        }
        val now = System.currentTimeMillis()
        val wheel = Wheel(
            id = UUID.randomUUID().toString(),
            name = "Gợi ý: $topic",
            items = items,
            createdAt = now,
            updatedAt = now
        )
        viewModelScope.launch {
            wheelRepository.upsertWheel(wheel)
        }
    }

    fun updateFormName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun addItem() {
        val currentItems = _formState.value.items.toMutableList()
        currentItems.add(WheelItem(id = UUID.randomUUID().toString(), name = "", priority = 1))
        _formState.value = _formState.value.copy(items = currentItems)
    }

    fun updateItemName(id: String, name: String) {
        val currentItems = _formState.value.items.toMutableList()
        val index = currentItems.indexOfFirst { it.id == id }
        if (index != -1) {
            currentItems[index] = currentItems[index].copy(name = name)
            _formState.value = _formState.value.copy(items = currentItems)
        }
    }

    fun removeItem(id: String) {
        val currentItems = _formState.value.items.toMutableList()
        currentItems.removeAll { it.id == id }
        _formState.value = _formState.value.copy(items = currentItems)
    }

    fun saveWheel(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _formState.value
        if (state.name.isBlank()) {
            onError("Tên danh sách không được để trống")
            return
        }
        val validItems = state.items.filter { it.name.isNotBlank() }
        if (validItems.size < 2) {
            onError("Cần ít nhất 2 mục")
            return
        }
        val now = System.currentTimeMillis()
        val wheelToSave = Wheel(
            id = state.id,
            name = state.name,
            items = validItems,
            createdAt = now,
            updatedAt = now
        )
        viewModelScope.launch {
            val existing = wheelRepository.getWheel(state.id)
            if (existing != null) {
                wheelRepository.upsertWheel(wheelToSave.copy(createdAt = existing.createdAt))
            } else {
                wheelRepository.upsertWheel(wheelToSave)
            }
            onSuccess()
        }
    }

    fun deleteWheel(id: String) {
        viewModelScope.launch {
            wheelRepository.deleteWheel(id)
        }
    }

    fun cloneWheel(id: String) {
        viewModelScope.launch {
            val existing = wheelRepository.getWheel(id)
                ?: DRAWING_FALLBACK_WHEEL.takeIf { id == it.id }
                ?: return@launch
            val now = System.currentTimeMillis()
            val cloned = existing.copy(
                id = UUID.randomUUID().toString(),
                name = existing.name + " (Sao chép)",
                createdAt = now,
                updatedAt = now,
            )
            wheelRepository.upsertWheel(cloned)
        }
    }
}
