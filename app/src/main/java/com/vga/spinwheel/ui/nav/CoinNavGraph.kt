package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.vga.spinwheel.ui.screen.coin.CoinHomeScreen
import com.vga.spinwheel.ui.screen.coin.CoinLabelScreen
import com.vga.spinwheel.ui.screen.coin.CoinResultScreen
import com.vga.spinwheel.ui.screen.coin.CoinSettingsScreen
import com.vga.spinwheel.ui.screen.coin.CoinViewModel

fun NavGraphBuilder.coinGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    navigation(
        startDestination = Screen.Coin.route + "/home",
        route = Screen.Coin.route
    ) {
        composable(Screen.Coin.route + "/home") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Coin.route)
            }
            val viewModel: CoinViewModel = hiltViewModel(parentEntry)
            CoinHomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.CoinSettings.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Coin.route)
            }
            val viewModel: CoinViewModel = hiltViewModel(parentEntry)
            CoinSettingsScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.CoinLabel.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Coin.route)
            }
            val viewModel: CoinViewModel = hiltViewModel(parentEntry)
            CoinLabelScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.CoinResult.route,
            arguments = listOf(navArgument(Screen.ARG_IS_HEADS) { type = NavType.BoolType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Coin.route)
            }
            val viewModel: CoinViewModel = hiltViewModel(parentEntry)
            val isHeads = backStackEntry.arguments?.getBoolean(Screen.ARG_IS_HEADS) ?: true
            CoinResultScreen(navController = navController, isHeads = isHeads, viewModel = viewModel)
        }
    }
}
