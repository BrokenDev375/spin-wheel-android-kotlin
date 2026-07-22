package com.vga.spinwheel.data.model

data class Wheel(
    val id: String,
    val name: String,
    val items: List<WheelItem>,
    val createdAt: Long,
    val updatedAt: Long,
)
