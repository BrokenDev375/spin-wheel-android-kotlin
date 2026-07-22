package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vga.spinwheel.ui.screen.wheel.WheelAddEditScreen
import com.vga.spinwheel.ui.screen.wheel.WheelHistoryScreen
import com.vga.spinwheel.ui.screen.wheel.WheelHomeScreen
import com.vga.spinwheel.ui.screen.wheel.WheelPaletteScreen
import com.vga.spinwheel.ui.screen.wheel.WheelResultScreen
import com.vga.spinwheel.ui.screen.wheel.WheelSettingsScreen
import com.vga.spinwheel.ui.screen.wheel.WheelSpinScreen
import com.vga.spinwheel.ui.screen.wheel.WheelViewModel

fun NavGraphBuilder.wheelNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    composable(WheelRoutes.HOME) { backStackEntry ->
        val viewModel: WheelViewModel = hiltViewModel(backStackEntry)
        WheelHomeScreen(
            viewModel = viewModel,
            onBack = onBack,
            onAddWheel = {
                viewModel.prepareNewForm()
                navController.navigate(WheelRoutes.ADD)
            },
            onEditWheel = { wheelId ->
                viewModel.prepareEditForm(wheelId)
                navController.navigate(WheelRoutes.edit(wheelId))
            },
            onSpinWheel = { wheelId ->
                navController.navigate(WheelRoutes.spin(wheelId))
            },
        )
    }

    composable(WheelRoutes.ADD) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = WheelRoutes.EDIT,
        arguments = listOf(
            navArgument(WheelRoutes.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(WheelRoutes.ARG_WHEEL_ID) ?: ""

        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = WheelRoutes.SPIN,
        arguments = listOf(
            navArgument(WheelRoutes.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(WheelRoutes.ARG_WHEEL_ID) ?: ""

        WheelSpinScreen(
            wheelId = wheelId,
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenSettings = { navController.navigate(WheelRoutes.settings(wheelId)) },
            onOpenHistory = { navController.navigate(WheelRoutes.history(wheelId)) },
            onResult = { wId, rId ->
                navController.navigate(WheelRoutes.result(wId, rId)) {
                    popUpTo(WheelRoutes.spin(wId)) { inclusive = true }
                }
            },
        )
    }

    composable(
        route = WheelRoutes.SETTINGS,
        arguments = listOf(
            navArgument(WheelRoutes.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(WheelRoutes.ARG_WHEEL_ID) ?: ""

        WheelSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenPalette = { navController.navigate(WheelRoutes.palette(wheelId)) },
        )
    }

    composable(
        route = WheelRoutes.PALETTE,
        arguments = listOf(
            navArgument(WheelRoutes.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelPaletteScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }

    composable(
        route = WheelRoutes.RESULT,
        arguments = listOf(
            navArgument(WheelRoutes.ARG_WHEEL_ID) { type = NavType.StringType },
            navArgument(WheelRoutes.ARG_RESULT_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(WheelRoutes.ARG_WHEEL_ID) ?: ""
        val resultId = backStackEntry.arguments?.getString(WheelRoutes.ARG_RESULT_ID) ?: ""

        WheelResultScreen(
            wheelId = wheelId,
            resultId = resultId,
            viewModel = viewModel,
            onRetry = {
                navController.navigate(WheelRoutes.spin(wheelId)) {
                    popUpTo(WheelRoutes.HOME)
                }
            },
            onHome = {
                navController.popBackStack(WheelRoutes.HOME, inclusive = false)
            },
        )
    }

    composable(
        route = WheelRoutes.HISTORY,
        arguments = listOf(
            navArgument(WheelRoutes.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(WheelRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelHistoryScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
