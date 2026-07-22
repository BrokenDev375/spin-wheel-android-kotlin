package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vga.spinwheel.ui.screen.home.HomeScreen
import com.vga.spinwheel.ui.screen.placeholder.PlaceholderScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onFeatureClick = { screen -> navController.navigate(screen.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onPaymentClick = { navController.navigate(Screen.Payment.route) },
            )
        }

        placeholder(Screen.Wheel) { navController.popBackStack() }
        placeholder(Screen.Finger) { navController.popBackStack() }
        placeholder(Screen.Coin) { navController.popBackStack() }
        placeholder(Screen.Team) { navController.popBackStack() }
        placeholder(Screen.Number) { navController.popBackStack() }
        placeholder(Screen.Drawing) { navController.popBackStack() }
        placeholder(Screen.Bottle) { navController.popBackStack() }
        placeholder(Screen.Dice) { navController.popBackStack() }
        placeholder(Screen.Card) { navController.popBackStack() }
        placeholder(Screen.Settings) { navController.popBackStack() }
        placeholder(Screen.Payment) { navController.popBackStack() }
    }
}

private fun NavGraphBuilder.placeholder(
    screen: Screen,
    onBack: () -> Unit,
) {
    composable(screen.route) {
        PlaceholderScreen(
            title = screen.title,
            onBack = onBack,
        )
    }
}
