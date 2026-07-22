package com.vga.spinwheel.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wheel_items",
    foreignKeys = [
        ForeignKey(
            entity = WheelEntity::class,
            parentColumns = ["id"],
            childColumns = ["wheelId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["wheelId"]),
    ],
)
data class WheelItemEntity(
    @PrimaryKey val id: String,
    val wheelId: String,
    val name: String,
    val priority: Int,
    val sortOrder: Int,
)
