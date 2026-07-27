package com.vga.spinwheel.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vga.spinwheel.ui.screen.number.NumberHomeScreen
import com.vga.spinwheel.ui.screen.number.NumberSettingsScreen
import com.vga.spinwheel.ui.screen.number.NumberResultScreen
import com.vga.spinwheel.ui.screen.number.NumberHistoryScreen

fun NavGraphBuilder.numberGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    navigation(
        startDestination = Screen.NumberHome.route,
        route = Screen.Number.route
    ) {
        composable(Screen.NumberHome.route) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberHomeScreen(navController, viewModel, onBack)
        }
        composable(Screen.NumberSettings.route) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberSettingsScreen(navController, viewModel)
        }
        composable(Screen.NumberResult.route) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberResultScreen(navController, viewModel)
        }
        composable(Screen.NumberHistory.route) { backStackEntry ->
            val parentEntry = androidx.compose.runtime.remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Number.route)
            }
            val viewModel: com.vga.spinwheel.ui.screen.number.NumberViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
            NumberHistoryScreen(navController, viewModel)
        }
    }
}
