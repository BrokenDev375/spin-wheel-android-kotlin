package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vga.spinwheel.ui.screen.card.CardLabelScreen
import com.vga.spinwheel.ui.screen.card.CardScreen
import com.vga.spinwheel.ui.screen.card.CardSettingsScreen
import com.vga.spinwheel.ui.screen.card.CardViewModel

fun NavGraphBuilder.cardNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    composable(CardRoutes.HOME) { backStackEntry ->
        val viewModel: CardViewModel = hiltViewModel(backStackEntry)

        CardScreen(
            viewModel = viewModel,
            onBack = onBack,
            onOpenSettings = { navController.navigate(CardRoutes.SETTINGS) },
            onHome = {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
        )
    }

    composable(CardRoutes.SETTINGS) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(CardRoutes.HOME)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)

        CardSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenLabels = { navController.navigate(CardRoutes.LABEL) },
        )
    }

    composable(CardRoutes.LABEL) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(CardRoutes.HOME)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)

        CardLabelScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
