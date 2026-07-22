package com.vga.spinwheel.ui.nav

import android.content.Intent
import android.app.Activity
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vga.spinwheel.ui.screen.home.HomeScreen
import com.vga.spinwheel.ui.screen.intro.IntroScreen
import com.vga.spinwheel.ui.screen.finger.FingerScreen
import com.vga.spinwheel.ui.screen.finger.FingerViewModel
import com.vga.spinwheel.ui.screen.language.LanguageScreen
import com.vga.spinwheel.ui.screen.payment.PaymentScreen
import com.vga.spinwheel.ui.screen.settings.SettingsScreen
import com.vga.spinwheel.advertisement.AdManager

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onFeatureClick = { screen ->
                    AdManager.showInter(
                        activity = context as? Activity,
                        placement = "inter_home",
                    ) {
                        navController.navigate(screen.route)
                    }
                },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onPaymentClick = { navController.navigate(Screen.Payment.route) },
            )
        }

        wheelNavGraph(
            navController = navController,
            onBack = { navController.popBackStack() },
        )

        teamNavGraph(
            navController = navController,
            onBack = { navController.popBackStack() },
        )

        bottleNavGraph(
            navController = navController,
            onBack = { navController.popBackStack() },
        )

        cardNavGraph(
            navController = navController,
            onBack = { navController.popBackStack() },
        )

        composable(Screen.Finger.route) {
            val viewModel: FingerViewModel = hiltViewModel()
            FingerScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
            )
        }
        coinGraph(
            navController = navController,
            onBack = { navController.popBackStack() },
        )
        numberGraph(navController = navController)
        drawingNavGraph(navController = navController)
        diceGraph(navController = navController)

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onShareClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Spin Wheel & Random Tools")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share"))
                },
                onLanguageClick = { navController.navigate(Screen.Language.route) },
                onRateClick = {
                    Toast.makeText(context, "Đánh giá ứng dụng mock", Toast.LENGTH_SHORT).show()
                },
            )
        }

        composable(Screen.Language.route) {
            LanguageScreen(
                onDone = { navController.popBackStack() },
            )
        }

        composable(Screen.Payment.route) {
            PaymentScreen(
                onClose = { navController.popBackStack() },
                onRestore = {
                    Toast.makeText(context, "Restore mock", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }
}
