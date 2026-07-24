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

object DiceRoutes {
    const val HOME = "dice_home"
    const val SETTINGS = "dice_settings"
    const val LABEL = "dice_label"
    const val PREVIEW = "dice_preview"
}

fun NavGraphBuilder.diceGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    navigation(
        startDestination = DiceRoutes.HOME,
        route = Screen.Dice.route
    ) {
        composable(DiceRoutes.HOME) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DiceHomeScreen(
                viewModel = viewModel,
                onBack = onBack,
                onOpenSettings = { navController.navigate(DiceRoutes.SETTINGS) },
                onPreview = { navController.navigate(DiceRoutes.PREVIEW) }
            )
        }

        composable(DiceRoutes.SETTINGS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DiceSettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenLabel = { navController.navigate(DiceRoutes.LABEL) }
            )
        }

        composable(DiceRoutes.LABEL) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DiceLabelScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(DiceRoutes.PREVIEW) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dice.route)
            }
            val viewModel: DiceViewModel = hiltViewModel(parentEntry)
            DicePreviewScreen(
                viewModel = viewModel,
                onHome = {
                    onBack()
                },
                onRetry = {
                    navController.popBackStack(DiceRoutes.HOME, inclusive = false)
                }
            )
        }
    }
}
