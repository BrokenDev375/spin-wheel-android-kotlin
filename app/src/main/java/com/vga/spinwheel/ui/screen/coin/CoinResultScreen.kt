package com.vga.spinwheel.ui.screen.coin

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.nav.Screen

@Composable
fun CoinResultScreen(
    navController: NavController,
    isHeads: Boolean,
    viewModel: CoinViewModel = hiltViewModel()
) {
    val skin by viewModel.currentSkin.collectAsState()
    val context = LocalContext.current

    SpinResultScreen(
        title = "Kết Quả",
        onHome = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        },
        onShare = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Tôi vừa tung được mặt ${if (isHeads) "Sấp" else "Ngửa"}!")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
        },
        onRetry = { navController.popBackStack() },
        cardHeight = 450.dp,
        cardContentPadding = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(66.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(Color(0xFF292640)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        id = if (isHeads) skin.headDrawable else skin.tailDrawable
                    ),
                    contentDescription = if (isHeads) "Coin Head" else "Coin Tail",
                    modifier = Modifier
                        .size(320.dp)
                        .graphicsLayer { rotationY = 180f }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
