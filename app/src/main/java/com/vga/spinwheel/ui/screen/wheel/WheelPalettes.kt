package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.ui.graphics.Color

data class WheelPalette(
    val id: Int,
    val name: String,
    val colors: List<Color>,
)

object WheelPalettes {
    val Classic = WheelPalette(
        id = 0,
        name = "Cổ điển",
        colors = listOf(
            Color(0xFFFFC107), // Yellow
            Color(0xFFFF5252), // Orange-Red
            Color(0xFF607D8B), // Grey
            Color(0xFF1DE9B6), // Teal
        ),
    )

    val Vibrant = WheelPalette(
        id = 1,
        name = "Sáng rực",
        colors = listOf(
            Color(0xFFFFC107), // Yellow
            Color(0xFFFF3D00), // Bright Red
            Color(0xFF2979FF), // Bright Blue
            Color(0xFF00E676), // Bright Green
        ),
    )

    val Pastel = WheelPalette(
        id = 2,
        name = "Nhẹ nhàng",
        colors = listOf(
            Color(0xFFFFF176), // Pastel Yellow
            Color(0xFFFF80AB), // Pastel Pink
            Color(0xFF82B1FF), // Pastel Blue
            Color(0xFFA7FFEB), // Pastel Green
        ),
    )

    val Dark = WheelPalette(
        id = 3,
        name = "Tối",
        colors = listOf(
            Color(0xFF90A4AE), // Light Grey
            Color(0xFF546E7A), // Mid Grey
            Color(0xFF37474F), // Dark Grey
            Color(0xFF212121), // Charcoal
        ),
    )

    val all: List<WheelPalette> = listOf(Classic, Vibrant, Pastel, Dark)

    fun getPalette(index: Int): WheelPalette {
        return all.getOrElse(index.coerceIn(0, all.lastIndex)) { Classic }
    }
}
