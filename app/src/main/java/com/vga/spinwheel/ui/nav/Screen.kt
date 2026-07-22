package com.vga.spinwheel.ui.nav

sealed class Screen(
    val route: String,
    val title: String,
) {
    data object Home : Screen("home", "Spin Wheel")
    data object Wheel : Screen("wheel", "Bánh Xe")
    data object Finger : Screen("finger", "Chọn Ngón Tay")
    data object Coin : Screen("coin", "Đồng Xu")
    data object Team : Screen("team", "Đội")
    data object Number : Screen("number", "Số Ngẫu Nhiên")
    data object Drawing : Screen("drawing", "Vẽ")
    data object Bottle : Screen("bottle", "Quay Chai")
    data object Dice : Screen("dice", "Xúc Xắc")
    data object Card : Screen("card", "Lật Thẻ")
    data object Settings : Screen("settings", "Cài đặt")
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
