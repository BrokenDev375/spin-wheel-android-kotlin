package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vga.spinwheel.ui.screen.bottle.BottleLabelScreen
import com.vga.spinwheel.ui.screen.bottle.BottleScreen
import com.vga.spinwheel.ui.screen.bottle.BottleSettingsScreen
import com.vga.spinwheel.ui.screen.bottle.BottleViewModel

fun NavGraphBuilder.bottleNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    composable(Screen.Bottle.route) { backStackEntry ->
        val viewModel: BottleViewModel = hiltViewModel(backStackEntry)

        BottleScreen(
            viewModel = viewModel,
            onBack = onBack,
            onOpenSettings = { navController.navigate(Screen.BottleSettings.route) },
            onHome = {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
        )
    }

    composable(Screen.BottleSettings.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Bottle.route)
        }
        val viewModel: BottleViewModel = hiltViewModel(parentEntry)

        BottleSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenLabels = { navController.navigate(Screen.BottleLabel.route) },
        )
    }

    composable(Screen.BottleLabel.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Bottle.route)
        }
        val viewModel: BottleViewModel = hiltViewModel(parentEntry)

        BottleLabelScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
