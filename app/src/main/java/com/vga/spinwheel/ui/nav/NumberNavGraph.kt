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
    onBack: () -> Unit,
) {
    navigation(
        startDestination = NumberRoutes.HOME,
        route = Screen.Number.route
    ) {
        composable(NumberRoutes.HOME) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberHomeScreen(navController, viewModel, onBack)
        }
        composable(NumberRoutes.SETTINGS) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberSettingsScreen(navController, viewModel)
        }
        composable(NumberRoutes.RESULT) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberResultScreen(navController, viewModel)
        }
        composable(NumberRoutes.HISTORY) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberHistoryScreen(navController, viewModel)
        }
    }
}
