package com.vga.spinwheel.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "random_history",
    indices = [
        Index(value = ["feature"]),
        Index(value = ["sourceId"]),
        Index(value = ["feature", "createdAt"]),
    ],
)
data class RandomHistoryEntity(
    @PrimaryKey val id: String,
    val feature: String,
    val sourceId: String?,
    val title: String,
    val value: String,
    val payload: String?,
    val createdAt: Long,
)
