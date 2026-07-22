package com.vga.spinwheel.ui.screen.wheel

import com.vga.spinwheel.data.model.WheelItem

object WheelFormRules {
    const val REQUIRED_ITEM_COUNT = 2
    const val MIN_PRIORITY = 1
    const val MAX_PRIORITY = 10

    fun changePriority(
        items: List<WheelItem>,
        itemId: String,
        delta: Int,
    ): List<WheelItem> {
        val index = items.indexOfFirst { it.id == itemId }
        if (index == -1) return items

        val item = items[index]
        val nextPriority = item.priority + delta

        if (delta < 0 && nextPriority < MIN_PRIORITY) {
            return if (index < REQUIRED_ITEM_COUNT) {
                items.mapIndexed { itemIndex, current ->
                    if (itemIndex == index) current.copy(priority = MIN_PRIORITY) else current
                }
            } else {
                items.filterIndexed { itemIndex, _ -> itemIndex != index }
            }
        }

        return items.mapIndexed { itemIndex, current ->
            if (itemIndex == index) {
                current.copy(priority = nextPriority.coerceIn(MIN_PRIORITY, MAX_PRIORITY))
            } else {
                current
            }
        }
    }
}
