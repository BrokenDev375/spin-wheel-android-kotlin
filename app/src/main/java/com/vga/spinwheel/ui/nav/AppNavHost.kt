package com.vga.spinwheel.ui.nav

import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vga.spinwheel.ui.screen.home.HomeScreen
import com.vga.spinwheel.ui.screen.finger.FingerScreen
import com.vga.spinwheel.ui.screen.finger.FingerViewModel
import com.vga.spinwheel.ui.screen.settings.SettingsRoute
import com.brian.base_application.language.LanguageActivity
import com.vga.spinwheel.advertisement.AdManager
import com.vga.spinwheel.core.MainActivity
import com.vga.spinwheel.platform.IapLauncher
import com.vga.spinwheel.R

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val shareText = stringResource(R.string.share_app_text)
    val shareChooserTitle = stringResource(R.string.share_chooser_title)
    val rateUnavailableText = stringResource(R.string.rate_unavailable)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onFeatureClick = { screen ->
                    val activity = context as? Activity
                    if (activity == null) {
                        navController.navigate(screen.route)
                    } else {
                        AdManager.showInter(activity, "inter_home") {
                            navController.navigate(screen.route)
                        }
                    }
                },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onPaymentClick = { IapLauncher.open(context) },
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
            SettingsRoute(
                onBack = { navController.popBackStack() },
                onShareClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, shareChooserTitle))
                },
                onLanguageClick = {
                    (context as? FragmentActivity)?.let { activity ->
                        LanguageActivity.start(activity, MainActivity::class.java)
                    }
                },
                onPremiumClick = { IapLauncher.open(context) },
                onRateClick = {
                    openStoreListing(context, rateUnavailableText)
                },
            )
        }

    }
}

private fun openStoreListing(context: android.content.Context, fallbackMessage: String) {
    val packageName = context.packageName
    val marketIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("market://details?id=$packageName"),
    )
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
    )
    runCatching {
        context.startActivity(marketIntent)
    }.recoverCatching {
        context.startActivity(webIntent)
    }.onFailure {
        Toast.makeText(context, fallbackMessage, Toast.LENGTH_SHORT).show()
    }
}
