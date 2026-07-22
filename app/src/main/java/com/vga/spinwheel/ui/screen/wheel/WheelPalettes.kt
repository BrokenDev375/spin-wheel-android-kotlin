package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.ui.graphics.Color

data class WheelPalette(
    val id: Int,
    val name: String,
    val colors: List<Color>,
)

object WheelPalettes {
    val Default = WheelPalette(
        id = 0,
        name = "Rực Rỡ",
        colors = listOf(
            Color(0xFFFF5252),
            Color(0xFFFF4081),
            Color(0xFFE040FB),
            Color(0xFF7C4DFF),
            Color(0xFF536DFE),
            Color(0xFF448AFF),
            Color(0xFF40C4FF),
            Color(0xFF1DE9B6),
            Color(0xFF64DD17),
            Color(0xFFFFD600),
            Color(0xFFFFAB00),
            Color(0xFFFF6D00),
        ),
    )

    val Pastel = WheelPalette(
        id = 1,
        name = "Pastel Nhẹ Nhàng",
        colors = listOf(
            Color(0xFFFFB7B2),
            Color(0xFFFFDAC1),
            Color(0xFFE2F0CB),
            Color(0xFFB5EAD7),
            Color(0xFFC7CEEA),
            Color(0xFFF3C4FB),
            Color(0xFFFEE1E8),
        ),
    )

    val Neon = WheelPalette(
        id = 2,
        name = "Neon Đêm",
        colors = listOf(
            Color(0xFF00F5D4),
            Color(0xFF7B2CBF),
            Color(0xFFF15BB5),
            Color(0xFFFEE440),
            Color(0xFF00BBF9),
            Color(0xFF9B5DE5),
        ),
    )

    val Sunset = WheelPalette(
        id = 3,
        name = "Hoàng Hôn",
        colors = listOf(
            Color(0xFFFF4E50),
            Color(0xFFF9D423),
            Color(0xFFFC913A),
            Color(0xFFFF6B6B),
            Color(0xFFC7F464),
            Color(0xFF4ECDC4),
        ),
    )

    val Ocean = WheelPalette(
        id = 4,
        name = "Đại Dương",
        colors = listOf(
            Color(0xFF006699),
            Color(0xFF2980B9),
            Color(0xFF3498DB),
            Color(0xFF1ABC9C),
            Color(0xFF16A085),
            Color(0xFF2C3E50),
        ),
    )

    val all: List<WheelPalette> = listOf(Default, Pastel, Neon, Sunset, Ocean)

    fun getPalette(index: Int): WheelPalette {
        return all.getOrElse(index.coerceIn(0, all.lastIndex)) { Default }
    }
}
