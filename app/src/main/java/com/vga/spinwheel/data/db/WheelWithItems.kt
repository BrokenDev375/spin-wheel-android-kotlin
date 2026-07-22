package com.vga.spinwheel.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class WheelWithItems(
    @Embedded val wheel: WheelEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "wheelId",
    )
    val items: List<WheelItemEntity>,
)
