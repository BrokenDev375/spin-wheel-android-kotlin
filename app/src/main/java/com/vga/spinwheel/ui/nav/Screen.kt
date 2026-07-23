package com.vga.spinwheel.ui.nav

import androidx.annotation.StringRes
import com.vga.spinwheel.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
) {
    data object Home : Screen("home", R.string.screen_home)
    data object Wheel : Screen("wheel", R.string.feature_wheel)
    data object Finger : Screen("finger", R.string.feature_finger)
    data object Coin : Screen("coin", R.string.feature_coin)
    data object Team : Screen("team", R.string.feature_team)
    data object Number : Screen("number", R.string.feature_number)
    data object Drawing : Screen("drawing", R.string.feature_drawing)
    data object Bottle : Screen("bottle", R.string.feature_bottle)
    data object Dice : Screen("dice", R.string.feature_dice)
    data object Card : Screen("card", R.string.feature_card)
    data object Settings : Screen("settings", R.string.screen_settings)
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
