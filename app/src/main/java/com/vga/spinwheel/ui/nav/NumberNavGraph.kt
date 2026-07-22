package com.vga.spinwheel.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vga.spinwheel.ui.screen.number.NumberHomeScreen
import com.vga.spinwheel.ui.screen.number.NumberSettingsScreen
import com.vga.spinwheel.ui.screen.number.NumberResultScreen
import com.vga.spinwheel.ui.screen.number.NumberHistoryScreen

object NumberRoutes {
    const val HOME = "number_home"
    const val SETTINGS = "number_settings"
    const val RESULT = "number_result"
    const val HISTORY = "number_history"
}

fun NavGraphBuilder.numberGraph(
    navController: NavController,
) {
    navigation(
        startDestination = NumberRoutes.HOME,
        route = Screen.Number.route
    ) {
        composable(NumberRoutes.HOME) {
            NumberHomeScreen(navController)
        }
        composable(NumberRoutes.SETTINGS) {
            NumberSettingsScreen(navController)
        }
        composable(NumberRoutes.RESULT) {
            NumberResultScreen(navController)
        }
        composable(NumberRoutes.HISTORY) {
            NumberHistoryScreen(navController)
        }
    }
}
