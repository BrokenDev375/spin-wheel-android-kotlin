package com.vga.spinwheel.ui.nav

sealed class Screen(
    val route: String,
    val title: String,
) {
    data object Home : Screen("home", "Spin Wheel")
    data object Wheel : Screen("wheel", "Banh Xe")
    data object Finger : Screen("finger", "Chon Ngon Tay")
    data object Coin : Screen("coin", "Dong Xu")
    data object Team : Screen("team", "Doi")
    data object Number : Screen("number", "So Ngau Nhien")
    data object Drawing : Screen("drawing", "Ve")
    data object Bottle : Screen("bottle", "Quay Chai")
    data object Dice : Screen("dice", "Xuc Xac")
    data object Card : Screen("card", "Lat The")
    data object Settings : Screen("settings", "Setting")
    data object Payment : Screen("payment", "Pro")
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
