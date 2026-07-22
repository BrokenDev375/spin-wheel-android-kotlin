package com.vga.spinwheel.ui.screen.card

import androidx.compose.ui.graphics.Color

data class CardFaceStyle(
    val background: Color,
    val border: Color,
    val content: Color,
    val mark: String,
    val text: String,
)

data class CardTheme(
    val id: String,
    val name: String,
    val labelBackground: Color,
    val labelContent: Color,
    val winner: CardFaceStyle,
    val loser: CardFaceStyle,
)

object CardThemes {
    val all = listOf(
        CardTheme(
            id = "theme-witch",
            name = "Witch",
            labelBackground = Color(0xFFC4B5FD),
            labelContent = Color(0xFF1E1B4B),
            winner = CardFaceStyle(
                background = Color(0xFFFFD43B),
                border = Color(0xFFFF9D00),
                content = Color.Black,
                mark = "M",
                text = "Thắng",
            ),
            loser = CardFaceStyle(
                background = Color(0xFF5A4B66),
                border = Color(0xFFB09BB8),
                content = Color.White,
                mark = "W",
                text = "Thua",
            ),
        ),
        CardTheme(
            id = "theme-skull",
            name = "Skull",
            labelBackground = Color(0xFFE5E5E5),
            labelContent = Color(0xFF171717),
            winner = CardFaceStyle(
                background = Color(0xFF262626),
                border = Color(0xFF000000),
                content = Color.White,
                mark = "S",
                text = "Thắng",
            ),
            loser = CardFaceStyle(
                background = Color(0xFFD9D9D9),
                border = Color(0xFF8C8C8C),
                content = Color.Black,
                mark = "H",
                text = "Thua",
            ),
        ),
        CardTheme(
            id = "theme-joker1",
            name = "Joker",
            labelBackground = Color(0xFFD9F99D),
            labelContent = Color(0xFF14532D),
            winner = CardFaceStyle(
                background = Color(0xFFFEE2E2),
                border = Color(0xFFFCA5A5),
                content = Color.Black,
                mark = "T",
                text = "Thắng",
            ),
            loser = CardFaceStyle(
                background = Color(0xFFE0F2FE),
                border = Color(0xFF7DD3FC),
                content = Color.Black,
                mark = "J",
                text = "Thua",
            ),
        ),
        CardTheme(
            id = "theme-pirate",
            name = "Pirate",
            labelBackground = Color(0xFFA5F3FC),
            labelContent = Color(0xFF164E63),
            winner = CardFaceStyle(
                background = Color(0xFFFEF08A),
                border = Color(0xFFFDE047),
                content = Color.Black,
                mark = "$",
                text = "Thắng",
            ),
            loser = CardFaceStyle(
                background = Color(0xFFCFFAFE),
                border = Color(0xFF67E8F9),
                content = Color.Black,
                mark = "P",
                text = "Thua",
            ),
        ),
    )

    fun get(index: Int): CardTheme =
        all[CardRoundRules.clampThemeIndex(index, all.size)]
}
