package com.vga.spinwheel.ui.screen.number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.RandomResult
import com.vga.spinwheel.data.repo.NumberSettingsRepository
import com.vga.spinwheel.data.repo.RandomHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

@HiltViewModel
class NumberViewModel @Inject constructor(
    private val settingsRepository: NumberSettingsRepository,
    private val historyRepository: RandomHistoryRepository,
) : ViewModel() {

    val min: StateFlow<Int> = settingsRepository.observeMin()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val max: StateFlow<Int> = settingsRepository.observeMax()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 10)

    val count: StateFlow<Int> = settingsRepository.observeCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val allowDuplicates: StateFlow<Boolean> = settingsRepository.observeAllowDuplicates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val duration: StateFlow<Int> = settingsRepository.observeDuration()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val history: StateFlow<List<RandomResult>> = historyRepository.observeHistory(RandomFeature.NUMBER)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _lastResult = MutableStateFlow<String?>(null)
    val lastResult = _lastResult.asStateFlow()
    
    // Temp settings
    private val _tempMin = MutableStateFlow(1)
    val tempMin = _tempMin.asStateFlow()

    private val _tempMax = MutableStateFlow(10)
    val tempMax = _tempMax.asStateFlow()

    private val _tempCount = MutableStateFlow(1)
    val tempCount = _tempCount.asStateFlow()

    private val _tempAllowDuplicates = MutableStateFlow(false)
    val tempAllowDuplicates = _tempAllowDuplicates.asStateFlow()

    private val _tempDuration = MutableStateFlow(1)
    val tempDuration = _tempDuration.asStateFlow()

    fun initTempSettings() {
        _tempMin.value = min.value
        _tempMax.value = max.value
        _tempCount.value = count.value
        _tempAllowDuplicates.value = allowDuplicates.value
        _tempDuration.value = duration.value
    }

    fun updateTempSettings(
        min: Int? = null,
        max: Int? = null,
        count: Int? = null,
        allowDuplicates: Boolean? = null,
        duration: Int? = null
    ) {
        min?.let { _tempMin.value = it }
        max?.let { _tempMax.value = it }
        count?.let { _tempCount.value = it }
        allowDuplicates?.let { _tempAllowDuplicates.value = it }
        duration?.let { _tempDuration.value = it }
    }

    fun saveSettings() {
        viewModelScope.launch {
            var finalMin = _tempMin.value
            var finalMax = _tempMax.value
            if (finalMin > finalMax) {
                val temp = finalMin
                finalMin = finalMax
                finalMax = temp
            }
            settingsRepository.setMin(finalMin)
            settingsRepository.setMax(finalMax)
            settingsRepository.setCount(_tempCount.value)
            settingsRepository.setAllowDuplicates(_tempAllowDuplicates.value)
            settingsRepository.setDuration(_tempDuration.value)
        }
    }

    fun generateNumbers(): List<Int> {
        val currentMin = min.value
        val currentMax = max.value
        val currentCount = count.value
        val currentAllowDuplicates = allowDuplicates.value

        var normalizedMin = currentMin
        var normalizedMax = currentMax
        if (normalizedMin > normalizedMax) {
            normalizedMin = currentMax
            normalizedMax = currentMin
        }

        val rangeSize = normalizedMax - normalizedMin + 1
        var actualCount = currentCount

        if (!currentAllowDuplicates && actualCount > rangeSize) {
            actualCount = rangeSize
        }

        val results = mutableListOf<Int>()
        if (currentAllowDuplicates) {
            for (i in 0 until actualCount) {
                if (normalizedMin == normalizedMax) {
                    results.add(normalizedMin)
                } else {
                    results.add(Random.nextInt(normalizedMin, normalizedMax + 1))
                }
            }
        } else {
            val pool = (normalizedMin..normalizedMax).toMutableList()
            pool.shuffle()
            results.addAll(pool.take(actualCount))
        }

        val resultStr = results.joinToString(", ")
        _lastResult.value = resultStr
        return results
    }

    fun saveResultToHistory(resultStr: String) {
        viewModelScope.launch {
            historyRepository.addResult(
                feature = RandomFeature.NUMBER,
                title = "Số Ngẫu Nhiên",
                value = resultStr
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearFeature(RandomFeature.NUMBER)
        }
    }
    
    fun clearLastResult() {
        _lastResult.value = null
    }
}
