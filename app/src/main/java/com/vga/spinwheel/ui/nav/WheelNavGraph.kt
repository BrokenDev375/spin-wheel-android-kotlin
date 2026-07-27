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
    composable(Screen.Wheel.route) { backStackEntry ->
        val viewModel: WheelViewModel = hiltViewModel(backStackEntry)
        WheelHomeScreen(
            viewModel = viewModel,
            onBack = onBack,
            onAddWheel = {
                viewModel.prepareNewForm()
                navController.navigate(Screen.WheelAdd.route)
            },
            onEditWheel = { wheelId ->
                viewModel.prepareEditForm(wheelId)
                navController.navigate(Screen.wheelEdit(wheelId))
            },
            onSpinWheel = { wheelId ->
                navController.navigate(Screen.wheelSpin(wheelId))
            },
        )
    }

    composable(Screen.WheelAdd.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.WheelEdit.route,
        arguments = listOf(
            navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""

        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.WheelSpin.route,
        arguments = listOf(
            navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""

        WheelSpinScreen(
            wheelId = wheelId,
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenSettings = { navController.navigate(Screen.wheelSettings(wheelId)) },
            onOpenHistory = { navController.navigate(Screen.wheelHistory(wheelId)) },
            onResult = { wId, rId ->
                navController.navigate(Screen.wheelResult(wId, rId)) {
                    popUpTo(Screen.wheelSpin(wId)) { inclusive = true }
                }
            },
        )
    }

    composable(
        route = Screen.WheelSettings.route,
        arguments = listOf(
            navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""

        WheelSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenPalette = { navController.navigate(Screen.wheelPalette(wheelId)) },
        )
    }

    composable(
        route = Screen.WheelPalette.route,
        arguments = listOf(
            navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelPaletteScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.WheelResult.route,
        arguments = listOf(
            navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType },
            navArgument(Screen.ARG_RESULT_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)
        val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""
        val resultId = backStackEntry.arguments?.getString(Screen.ARG_RESULT_ID) ?: ""

        WheelResultScreen(
            wheelId = wheelId,
            resultId = resultId,
            viewModel = viewModel,
            onRetry = {
                navController.navigate(Screen.wheelSpin(wheelId)) {
                    popUpTo(Screen.Wheel.route)
                }
            },
            onHome = {
                navController.popBackStack(Screen.Wheel.route, inclusive = false)
            },
        )
    }

    composable(
        route = Screen.WheelHistory.route,
        arguments = listOf(
            navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType }
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Wheel.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelHistoryScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
