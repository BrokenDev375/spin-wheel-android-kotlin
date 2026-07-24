package com.vga.spinwheel.ui.nav

import androidx.annotation.StringRes
import com.vga.spinwheel.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
) {
    data object Home : Screen("home", R.string.spinwheel)
    data object Wheel : Screen("wheel", R.string.roulette)
    data object Finger : Screen("finger", R.string.fingerChooser)
    data object Coin : Screen("coin", R.string.coin)
    data object Team : Screen("team", R.string.homograft)
    data object Number : Screen("number", R.string.randerNum)
    data object Drawing : Screen("drawing", R.string.drawn)
    data object Bottle : Screen("bottle", R.string.spinBottle)
    data object Dice : Screen("dice", R.string.diceRoller)
    data object Card : Screen("card", R.string.card)
    data object Settings : Screen("settings", R.string.settings)
}

val featureScreens = listOf(
    Screen.Wheel,
    Screen.Finger,
    Screen.Coin,
    Screen.Team,
    Screen.Number,
    Screen.Drawing,
    Screen.Bottle,
    Screen.Dice,
    Screen.Card,
)
