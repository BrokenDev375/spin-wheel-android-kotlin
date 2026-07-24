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
import androidx.navigation.NavController
import com.vga.spinwheel.ui.screen.home.HomeScreen
import com.vga.spinwheel.ui.screen.finger.FingerScreen
import com.vga.spinwheel.ui.screen.finger.FingerViewModel
import com.vga.spinwheel.ui.screen.settings.SettingsRoute
import com.brian.base_application.language.LanguageActivity
import com.vga.spinwheel.advertisement.AdManager
import com.vga.spinwheel.core.MainActivity
import com.vga.spinwheel.platform.IapLauncher
import com.vga.spinwheel.R
import android.content.Context
import android.content.ContextWrapper

tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

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
        val onBackWithAd: () -> Unit = {
            val activity = context.findActivity()
            if (activity == null) {
                navController.popBackStack()
            } else {
                AdManager.showInter(activity, "inter_back") {
                    navController.popBackStack()
                }
            }
        }

        val onHomeWithAd: () -> Unit = {
            val activity = context.findActivity()
            if (activity == null) {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            } else {
                AdManager.showInter(activity, "inter_back") {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            }
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onFeatureClick = { screen ->
                    val activity = context.findActivity()
                    if (activity == null) {
                        navController.navigate(screen.route)
                    } else {
                        AdManager.showInter(activity, "inter_home") {
                            navController.navigate(screen.route)
                        }
                    }
                },
                onSettingsClick = { navController.navigateSingleTop(Screen.Settings.route) },
                onPaymentClick = { IapLauncher.open(context) },
            )
        }

        wheelNavGraph(
            navController = navController,
            onBack = onBackWithAd,
        )

        teamNavGraph(
            navController = navController,
            onBack = onBackWithAd,
        )

        bottleNavGraph(
            navController = navController,
            onBack = onBackWithAd,
        )

        cardNavGraph(
            navController = navController,
            onBack = onBackWithAd,
        )

        composable(Screen.Finger.route) {
            val viewModel: FingerViewModel = hiltViewModel()
            FingerScreen(
                viewModel = viewModel,
                onBack = onBackWithAd,
                onHome = onHomeWithAd,
            )
        }
        coinGraph(
            navController = navController,
            onBack = onBackWithAd,
        )
        numberGraph(
            navController = navController,
            onBack = onBackWithAd,
        )
        drawingNavGraph(
            navController = navController,
            onBack = onBackWithAd,
        )
        diceGraph(
            navController = navController,
            onBack = onBackWithAd,
            onHome = onHomeWithAd,
        )

        composable(Screen.Settings.route) {
            SettingsRoute(
                onBack = { navController.popBackStackSafely() },
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
                onRateClick = {
                    openStoreListing(context, rateUnavailableText)
                },
            )
        }

    }
}

private fun NavController.navigateSingleTop(route: String) {
    if (currentDestination?.route == route) return
    navigate(route) {
        launchSingleTop = true
    }
}

private fun NavController.popBackStackSafely(): Boolean {
    if (currentDestination?.route == Screen.Home.route) return false
    val popped = popBackStack()
    if (!popped && currentDestination == null) {
        navigateSingleTop(Screen.Home.route)
    }
    return popped
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
