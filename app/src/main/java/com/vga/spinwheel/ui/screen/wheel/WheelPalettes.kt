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
            Color(0xFFFFCE17),
            Color(0xFFFF6B61),
            Color(0xFF727A87),
            Color(0xFF35CF9E),
        ),
    )

    val Vibrant = WheelPalette(
        id = 1,
        name = "Sáng rực",
        colors = listOf(
            Color(0xFFF9A00E),
            Color(0xFFF34245),
            Color(0xFF4383EA),
            Color(0xFF14BD89),
        ),
    )

    val Pastel = WheelPalette(
        id = 2,
        name = "Nhẹ nhàng",
        colors = listOf(
            Color(0xFFFFD65A),
            Color(0xFFEFA0D2),
            Color(0xFF9FABEE),
            Color(0xFF68DDB0),
        ),
    )

    val Dark = WheelPalette(
        id = 3,
        name = "Tối",
        colors = listOf(
            Color(0xFF6D7582),
            Color(0xFF3A4558),
            Color(0xFF202C3B),
            Color(0xFF525D6E),
        ),
    )

    val all: List<WheelPalette> = listOf(Classic, Vibrant, Pastel, Dark)

    fun getPalette(index: Int): WheelPalette {
        return all.getOrElse(index.coerceIn(0, all.lastIndex)) { Classic }
    }
}
