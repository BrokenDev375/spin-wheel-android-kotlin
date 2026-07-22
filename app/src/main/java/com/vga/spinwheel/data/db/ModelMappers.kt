package com.vga.spinwheel.data.db

import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.RandomResult
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem

internal fun WheelWithItems.toModel(): Wheel = Wheel(
    id = wheel.id,
    name = wheel.name,
    items = items
        .sortedBy { it.sortOrder }
        .map { it.toModel() },
    createdAt = wheel.createdAt,
    updatedAt = wheel.updatedAt,
)

internal fun WheelItemEntity.toModel(): WheelItem = WheelItem(
    id = id,
    name = name,
    priority = priority,
)

internal fun Wheel.toEntity(): WheelEntity = WheelEntity(
    id = id,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

internal fun Wheel.toItemEntities(): List<WheelItemEntity> = items.mapIndexed { index, item ->
    WheelItemEntity(
        id = item.id,
        wheelId = id,
        name = item.name,
        priority = item.priority,
        sortOrder = index,
    )
}

internal fun RandomHistoryEntity.toModel(): RandomResult = RandomResult(
    id = id,
    feature = RandomFeature.fromStorageKey(feature),
    sourceId = sourceId,
    title = title,
    value = value,
    payload = payload,
    createdAt = createdAt,
)

internal fun RandomResult.toEntity(): RandomHistoryEntity = RandomHistoryEntity(
    id = id,
    feature = feature.storageKey,
    sourceId = sourceId,
    title = title,
    value = value,
    payload = payload,
    createdAt = createdAt,
)
