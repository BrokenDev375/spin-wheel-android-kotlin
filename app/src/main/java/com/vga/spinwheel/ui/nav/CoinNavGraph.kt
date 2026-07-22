package com.vga.spinwheel.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.vga.spinwheel.ui.screen.coin.CoinHomeScreen
import com.vga.spinwheel.ui.screen.coin.CoinLabelScreen
import com.vga.spinwheel.ui.screen.coin.CoinResultScreen
import com.vga.spinwheel.ui.screen.coin.CoinSettingsScreen

fun NavGraphBuilder.coinGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    navigation(
        startDestination = CoinRoutes.HOME,
        route = Screen.Coin.route
    ) {
        composable(CoinRoutes.HOME) {
            CoinHomeScreen(navController)
        }
        composable(CoinRoutes.SETTINGS) {
            CoinSettingsScreen(navController)
        }
        composable(CoinRoutes.LABEL) {
            CoinLabelScreen(navController)
        }
        composable(
            route = "${CoinRoutes.RESULT}/{isHeads}",
            arguments = listOf(navArgument("isHeads") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isHeads = backStackEntry.arguments?.getBoolean("isHeads") ?: true
            CoinResultScreen(navController, isHeads)
        }
    }
}
