package com.vga.spinwheel.ui.screen.number

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.SpinColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberResultScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel()
) {
    val lastResult by viewModel.lastResult.collectAsState()
    val context = LocalContext.current
    val numberTitle = stringResource(R.string.randerNum)
    val shareTitle = stringResource(R.string.sharereust)

    SpinResultScreen(
        modifier = Modifier.statusBarsPadding(),
        onHome = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        },
        onShare = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$numberTitle: ${lastResult ?: ""}")
            }
            context.startActivity(Intent.createChooser(shareIntent, shareTitle))
        },
        onRetry = {
            viewModel.clearLastResult()
            navController.popBackStack()
        },
        cardHeight = 520.dp,
        cardContentPadding = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(SpinColors.Background),
                contentAlignment = Alignment.Center,
            ) {
                NumberMachine(
                    modifier = Modifier.width(190.dp),
                    spreadBalls = true,
                )
            }

            if (lastResult != null) {
                val numbers = lastResult!!.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                FlowRow(
                    modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    numbers.forEach { num ->
                        NumberBall(
                            number = num,
                            size = 56.dp
                        )
                    }
                }
            }
        }
    }
}
