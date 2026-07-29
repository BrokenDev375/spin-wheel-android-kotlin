package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.vga.spinwheel.ui.screen.drawing.DrawingAiFormScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingHomeScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingPaletteScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingResultScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingSettingsScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingSpinScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingViewModel
import com.vga.spinwheel.ui.screen.wheel.WheelAddEditScreen
import com.vga.spinwheel.ui.screen.wheel.WheelViewModel

fun NavGraphBuilder.drawingNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    navigation(
        startDestination = Screen.DrawingHome.route,
        route = Screen.Drawing.route
    ) {
        composable(Screen.DrawingHome.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelFormViewModel: WheelViewModel = hiltViewModel(parentEntry)
            DrawingHomeScreen(
                viewModel = viewModel,
                onBack = onBack,
                onAddWheel = { 
                    wheelFormViewModel.prepareNewForm()
                    navController.navigate(Screen.DrawingAdd.route) 
                },
                onAiGenerate = { navController.navigate(Screen.DrawingAiForm.route) },
                onEditWheel = { wheelId ->
                    wheelFormViewModel.prepareEditForm(wheelId)
                    navController.navigate(Screen.drawingEdit(wheelId))
                },
                onSpinWheel = { wheelId ->
                    navController.navigate(Screen.drawingSpin(wheelId))
                }
            )
        }

        composable(Screen.DrawingAdd.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: WheelViewModel = hiltViewModel(parentEntry)
            WheelAddEditScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DrawingEdit.route,
            arguments = listOf(navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: WheelViewModel = hiltViewModel(parentEntry)
            WheelAddEditScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.DrawingAiForm.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            DrawingAiFormScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onGenerateSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DrawingSpin.route,
            arguments = listOf(navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""
            
            DrawingSpinScreen(
                wheelId = wheelId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(Screen.drawingSettings(wheelId)) },
                onSelectWheel = { selectedWheelId ->
                    navController.navigate(Screen.drawingSpin(selectedWheelId)) {
                        popUpTo(Screen.drawingSpin(wheelId)) { inclusive = true }
                    }
                },
                onResult = { wId ->
                    navController.navigate(Screen.drawingResult(wId)) {
                        popUpTo(Screen.drawingSpin(wId)) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.DrawingSettings.route,
            arguments = listOf(navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""

            DrawingSettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenPalette = { navController.navigate(Screen.drawingPalette(wheelId)) }
            )
        }

        composable(
            route = Screen.DrawingPalette.route,
            arguments = listOf(navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)

            DrawingPaletteScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSave = {
                    viewModel.saveSettings()
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Screen.DrawingResult.route,
            arguments = listOf(navArgument(Screen.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelId = backStackEntry.arguments?.getString(Screen.ARG_WHEEL_ID) ?: ""

            DrawingResultScreen(
                wheelId = wheelId,
                viewModel = viewModel,
                onRetry = {
                    navController.popBackStack(Screen.DrawingHome.route, inclusive = false)
                },
                onHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
