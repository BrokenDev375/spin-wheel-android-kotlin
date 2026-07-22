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
    composable(BottleRoutes.HOME) { backStackEntry ->
        val viewModel: BottleViewModel = hiltViewModel(backStackEntry)

        BottleScreen(
            viewModel = viewModel,
            onBack = onBack,
            onOpenSettings = { navController.navigate(BottleRoutes.SETTINGS) },
            onHome = {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
        )
    }

    composable(BottleRoutes.SETTINGS) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(BottleRoutes.HOME)
        }
        val viewModel: BottleViewModel = hiltViewModel(parentEntry)

        BottleSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenLabels = { navController.navigate(BottleRoutes.LABEL) },
        )
    }

    composable(BottleRoutes.LABEL) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(BottleRoutes.HOME)
        }
        val viewModel: BottleViewModel = hiltViewModel(parentEntry)

        BottleLabelScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
