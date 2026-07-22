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
        headDrawable = R.drawable.ic_coin_head,
        tailDrawable = R.drawable.ic_coin_tail,
        name = "Mặc định"
    )
    
    // Add more placeholder skins to match the 5 options in the UI mockup
    val USCent = CoinSkin(
        id = 1,
        headDrawable = R.drawable.ic_coin_head,
        tailDrawable = R.drawable.ic_coin_tail,
        name = "US Cent"
    )

    val Quarter = CoinSkin(
        id = 2,
        headDrawable = R.drawable.ic_coin_head,
        tailDrawable = R.drawable.ic_coin_tail,
        name = "Quarter"
    )

    val Greek = CoinSkin(
        id = 3,
        headDrawable = R.drawable.ic_coin_head,
        tailDrawable = R.drawable.ic_coin_tail,
        name = "Greek"
    )

    val Poker = CoinSkin(
        id = 4,
        headDrawable = R.drawable.ic_coin_head,
        tailDrawable = R.drawable.ic_coin_tail,
        name = "Poker"
    )

    val AllSkins = listOf(Default, USCent, Quarter, Greek, Poker)
}
