package com.vga.spinwheel.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vga.spinwheel.ui.screen.placeholder.PlaceholderScreen
import com.vga.spinwheel.ui.screen.wheel.WheelSkeletonScreen

fun NavGraphBuilder.wheelNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    composable(WheelRoutes.HOME) {
        WheelSkeletonScreen(
            onBack = onBack,
            onAddWheel = { navController.navigate(WheelRoutes.ADD) },
            onEditWheel = { navController.navigate(WheelRoutes.edit(SAMPLE_WHEEL_ID)) },
            onSpinWheel = { navController.navigate(WheelRoutes.spin(SAMPLE_WHEEL_ID)) },
            onWheelSettings = { navController.navigate(WheelRoutes.settings(SAMPLE_WHEEL_ID)) },
            onWheelResult = {
                navController.navigate(
                    WheelRoutes.result(
                        wheelId = SAMPLE_WHEEL_ID,
                        resultId = SAMPLE_RESULT_ID,
                    )
                )
            },
            onWheelHistory = { navController.navigate(WheelRoutes.history(SAMPLE_WHEEL_ID)) },
        )
    }

    composable(WheelRoutes.ADD) {
        PlaceholderScreen(
            title = "Them banh xe",
            onBack = { navController.popBackStack() },
        )
    }

    composable(WheelRoutes.EDIT) {
        PlaceholderScreen(
            title = "Sua banh xe",
            onBack = { navController.popBackStack() },
        )
    }

    composable(WheelRoutes.SPIN) {
        PlaceholderScreen(
            title = "Quay banh xe",
            onBack = { navController.popBackStack() },
        )
    }

    composable(WheelRoutes.SETTINGS) {
        PlaceholderScreen(
            title = "Cai dat banh xe",
            onBack = { navController.popBackStack() },
        )
    }

    composable(WheelRoutes.RESULT) {
        PlaceholderScreen(
            title = "Ket qua",
            onBack = { navController.popBackStack() },
        )
    }

    composable(WheelRoutes.HISTORY) {
        PlaceholderScreen(
            title = "Lich su",
            onBack = { navController.popBackStack() },
        )
    }
}

private const val SAMPLE_WHEEL_ID = "sample-wheel-id"
private const val SAMPLE_RESULT_ID = "sample-result-id"
