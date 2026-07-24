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
import com.vga.spinwheel.ui.screen.drawing.DrawingAddEditScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingAiFormScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingHomeScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingPaletteScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingResultScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingSettingsScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingSpinScreen
import com.vga.spinwheel.ui.screen.drawing.DrawingViewModel

fun NavGraphBuilder.drawingNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    navigation(
        startDestination = DrawingRoutes.HOME,
        route = Screen.Drawing.route
    ) {
        composable(DrawingRoutes.HOME) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            DrawingHomeScreen(
                viewModel = viewModel,
                onBack = onBack,
                onAddWheel = { 
                    viewModel.prepareNewForm()
                    navController.navigate(DrawingRoutes.ADD) 
                },
                onAiGenerate = { navController.navigate(DrawingRoutes.AI_FORM) },
                onEditWheel = { wheelId ->
                    viewModel.prepareEditForm(wheelId)
                    navController.navigate(DrawingRoutes.edit(wheelId))
                },
                onSpinWheel = { wheelId ->
                    navController.navigate(DrawingRoutes.spin(wheelId))
                }
            )
        }

        composable(DrawingRoutes.ADD) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            DrawingAddEditScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = DrawingRoutes.EDIT,
            arguments = listOf(navArgument(DrawingRoutes.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            DrawingAddEditScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(DrawingRoutes.AI_FORM) { backStackEntry ->
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
            route = DrawingRoutes.SPIN,
            arguments = listOf(navArgument(DrawingRoutes.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelId = backStackEntry.arguments?.getString(DrawingRoutes.ARG_WHEEL_ID) ?: ""
            
            DrawingSpinScreen(
                wheelId = wheelId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(DrawingRoutes.settings(wheelId)) },
                onResult = { wId ->
                    navController.navigate(DrawingRoutes.result(wId)) {
                        popUpTo(DrawingRoutes.spin(wId)) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = DrawingRoutes.SETTINGS,
            arguments = listOf(navArgument(DrawingRoutes.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelId = backStackEntry.arguments?.getString(DrawingRoutes.ARG_WHEEL_ID) ?: ""

            DrawingSettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenPalette = { navController.navigate(DrawingRoutes.palette(wheelId)) }
            )
        }

        composable(
            route = DrawingRoutes.PALETTE,
            arguments = listOf(navArgument(DrawingRoutes.ARG_WHEEL_ID) { type = NavType.StringType })
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
            route = DrawingRoutes.RESULT,
            arguments = listOf(navArgument(DrawingRoutes.ARG_WHEEL_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Drawing.route)
            }
            val viewModel: DrawingViewModel = hiltViewModel(parentEntry)
            val wheelId = backStackEntry.arguments?.getString(DrawingRoutes.ARG_WHEEL_ID) ?: ""

            DrawingResultScreen(
                wheelId = wheelId,
                viewModel = viewModel,
                onRetry = {
                    navController.popBackStack(DrawingRoutes.HOME, inclusive = false)
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
