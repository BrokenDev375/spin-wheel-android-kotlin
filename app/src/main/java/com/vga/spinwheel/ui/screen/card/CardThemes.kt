package com.vga.spinwheel.ui.screen.card

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.vga.spinwheel.R

data class CardFaceStyle(
    @DrawableRes val drawableRes: Int? = null,
    val fallback: CardFaceFallback = CardFaceFallback.None,
)

data class CardTheme(
    val id: String,
    val name: String,
    val labelBackground: Color,
    val labelContent: Color,
    val winner: CardFaceStyle,
    val loser: CardFaceStyle,
)

enum class CardFaceFallback {
    None,
    MonochromeLoser,
}

object CardThemes {
    val all = listOf(
        CardTheme(
            id = "theme-witch",
            name = "Witch",
            labelBackground = Color(0xFFC4B5FD),
            labelContent = Color(0xFF1E1B4B),
            winner = CardFaceStyle(
                drawableRes = R.drawable.card_winner_magician,
            ),
            loser = CardFaceStyle(
                drawableRes = R.drawable.card_loser_witch,
            ),
        ),
        CardTheme(
            id = "theme-skull",
            name = "Skull",
            labelBackground = Color(0xFFE5E5E5),
            labelContent = Color(0xFF171717),
            winner = CardFaceStyle(
                drawableRes = R.drawable.card_winner_skull,
            ),
            loser = CardFaceStyle(
                fallback = CardFaceFallback.MonochromeLoser,
            ),
        ),
        CardTheme(
            id = "theme-joker1",
            name = "Joker",
            labelBackground = Color(0xFFD9F99D),
            labelContent = Color(0xFF14532D),
            winner = CardFaceStyle(
                drawableRes = R.drawable.card_winner_casino,
            ),
            loser = CardFaceStyle(
                drawableRes = R.drawable.card_loser_casino,
            ),
        ),
        CardTheme(
            id = "theme-pirate",
            name = "Pirate",
            labelBackground = Color(0xFFA5F3FC),
            labelContent = Color(0xFF164E63),
            winner = CardFaceStyle(
                drawableRes = R.drawable.card_winner_pirate,
            ),
            loser = CardFaceStyle(
                drawableRes = R.drawable.card_loser_pirate,
            ),
        ),
    )

    fun get(index: Int): CardTheme =
        all[CardRoundRules.clampThemeIndex(index, all.size)]
}
