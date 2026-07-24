package com.vga.spinwheel.ui.screen.bottle

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.vga.spinwheel.R

data class BottleStyle(
    @DrawableRes val drawableRes: Int,
    val cardStart: Color,
    val cardEnd: Color,
)

object BottleStyles {
    val all = listOf(
        BottleStyle(
            drawableRes = R.drawable.bottle_style_0,
            cardStart = Color(0xFFF1D4CC),
            cardEnd = Color(0xFFDFE8F4),
        ),
        BottleStyle(
            drawableRes = R.drawable.bottle_style_1,
            cardStart = Color(0xFFDBFFD0),
            cardEnd = Color(0xFFF2F2F2),
        ),
        BottleStyle(
            drawableRes = R.drawable.bottle_style_2,
            cardStart = Color(0xFFD6F5FF),
            cardEnd = Color(0xFFF2F4FF),
        ),
        BottleStyle(
            drawableRes = R.drawable.bottle_style_3,
            cardStart = Color(0xFFFFF0CC),
            cardEnd = Color(0xFFF7F7F7),
        ),
        BottleStyle(
            drawableRes = R.drawable.bottle_style_4,
            cardStart = Color(0xFFDCD9FF),
            cardEnd = Color(0xFFF3F3F9),
        ),
        BottleStyle(
            drawableRes = R.drawable.bottle_style_5,
            cardStart = Color(0xFFFFD7DE),
            cardEnd = Color(0xFFFFF2F2),
        ),
    )

    fun get(index: Int): BottleStyle =
        all[BottleRoundRules.clampStyleIndex(index, all.size)]
}
