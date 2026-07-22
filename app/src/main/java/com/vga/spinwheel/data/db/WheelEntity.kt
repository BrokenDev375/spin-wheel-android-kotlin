package com.vga.spinwheel.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wheels")
data class WheelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
)
