package com.vga.spinwheel.ui.screen.coin

import androidx.annotation.DrawableRes
import com.vga.spinwheel.R

data class CoinSkin(
    val id: Int,
    @DrawableRes val headDrawable: Int,
    @DrawableRes val tailDrawable: Int,
    val name: String,
)

object CoinSkins {
    val Default = CoinSkin(
        id = 0,
        headDrawable = R.drawable.coin_penny_head,
        tailDrawable = R.drawable.coin_penny_tail,
        name = "Penny"
    )

    val Quarter = CoinSkin(
        id = 1,
        headDrawable = R.drawable.coin_quarter_montana,
        tailDrawable = R.drawable.coin_quarter_washington,
        name = "Quarter"
    )

    val Greek = CoinSkin(
        id = 2,
        headDrawable = R.drawable.coin_greek_head,
        tailDrawable = R.drawable.coin_greek_tail,
        name = "Greek"
    )

    val Poker = CoinSkin(
        id = 3,
        headDrawable = R.drawable.coin_poker_50,
        tailDrawable = R.drawable.coin_poker_heart,
        name = "Poker"
    )

    val Token = CoinSkin(
        id = 4,
        headDrawable = R.drawable.coin_token_dollar,
        tailDrawable = R.drawable.coin_token_paw,
        name = "Token"
    )

    val AllSkins = listOf(Default, Quarter, Greek, Poker, Token)
}
