package com.vga.spinwheel.ui.screen.bottle

import androidx.compose.ui.graphics.Color

data class BottleStyle(
    val cap: Color,
    val glass: Color,
    val glassDark: Color,
    val label: Color,
    val cardStart: Color,
    val cardEnd: Color,
)

object BottleStyles {
    val all = listOf(
        BottleStyle(
            cap = Color(0xFF2D6F6E),
            glass = Color(0xFF4E9894),
            glassDark = Color(0xFF2F7778),
            label = Color(0xFFF5D996),
            cardStart = Color(0xFFF1D4CC),
            cardEnd = Color(0xFFDFE8F4),
        ),
        BottleStyle(
            cap = Color(0xFF705014),
            glass = Color(0xFF73A62C),
            glassDark = Color(0xFF315A13),
            label = Color(0xFFD33D33),
            cardStart = Color(0xFFDBFFD0),
            cardEnd = Color(0xFFF2F2F2),
        ),
        BottleStyle(
            cap = Color(0xFFB77C28),
            glass = Color(0xFFD8781F),
            glassDark = Color(0xFF7A3F13),
            label = Color(0xFFF4DFAD),
            cardStart = Color(0xFFD6F5FF),
            cardEnd = Color(0xFFF2F4FF),
        ),
        BottleStyle(
            cap = Color(0xFF1D3C1C),
            glass = Color(0xFF55763A),
            glassDark = Color(0xFF183B1B),
            label = Color(0xFF294D1D),
            cardStart = Color(0xFFFFF0CC),
            cardEnd = Color(0xFFF7F7F7),
        ),
        BottleStyle(
            cap = Color(0xFF8D5A1A),
            glass = Color(0xFFBF7B22),
            glassDark = Color(0xFF6B3B13),
            label = Color(0xFFDCA238),
            cardStart = Color(0xFFDCD9FF),
            cardEnd = Color(0xFFF3F3F9),
        ),
        BottleStyle(
            cap = Color(0xFF6B5617),
            glass = Color(0xFF9FBE22),
            glassDark = Color(0xFF4F6D12),
            label = Color(0xFFEFE172),
            cardStart = Color(0xFFFFD7DE),
            cardEnd = Color(0xFFFFF2F2),
        ),
    )

    fun get(index: Int): BottleStyle =
        all[BottleRoundRules.clampStyleIndex(index, all.size)]
}
