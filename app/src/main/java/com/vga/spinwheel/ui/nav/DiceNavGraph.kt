package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vga.spinwheel.ui.screen.dice.DiceHomeScreen
import com.vga.spinwheel.ui.screen.dice.DiceLabelScreen
import com.vga.spinwheel.ui.screen.dice.DicePreviewScreen
import com.vga.spinwheel.ui.screen.dice.DiceSettingsScreen
import com.vga.spinwheel.ui.screen.dice.DiceViewModel

fun NavGraphBuilder.diceGraph(
    navController: NavController,
    onBack: () -> Unit,
    onHome: () -> Unit,
) {
    navigation(
        startDestination = Screen.DiceHome.route,
        route = Screen.Dice.route
    ) {
        composable(Screen.DiceHome.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DiceHomeScreen(
                viewModel = viewModel,
                onBack = onBack,
                onOpenSettings = { navController.navigate(Screen.DiceSettings.route) },
                onPreview = { navController.navigate(Screen.DicePreview.route) }
            )
        }

        composable(Screen.DiceSettings.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DiceSettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenLabel = { navController.navigate(Screen.DiceLabel.route) }
            )
        }

        composable(Screen.DiceLabel.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DiceLabelScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.DicePreview.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DicePreviewScreen(
                viewModel = viewModel,
                onHome = onHome,
                onRetry = {
                    navController.popBackStack(Screen.DiceHome.route, inclusive = false)
                }
            )
        }
    }
}
