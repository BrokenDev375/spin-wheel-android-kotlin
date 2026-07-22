package com.vga.spinwheel.data.model

data class RandomResult(
    val id: String,
    val feature: RandomFeature,
    val title: String,
    val value: String,
    val createdAt: Long,
    val sourceId: String? = null,
    val payload: String? = null,
)
