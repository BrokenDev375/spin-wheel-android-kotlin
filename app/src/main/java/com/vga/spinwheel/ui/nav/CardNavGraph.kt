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
    composable(Screen.Card.route) { backStackEntry ->
        val viewModel: CardViewModel = hiltViewModel(backStackEntry)

        CardScreen(
            viewModel = viewModel,
            onBack = onBack,
            onOpenSettings = { navController.navigate(Screen.CardSettings.route) },
            onHome = {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
        )
    }

    composable(Screen.CardSettings.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Card.route)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)

        CardSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenLabels = { navController.navigate(Screen.CardLabel.route) },
        )
    }

    composable(Screen.CardLabel.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Card.route)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)

        CardLabelScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
