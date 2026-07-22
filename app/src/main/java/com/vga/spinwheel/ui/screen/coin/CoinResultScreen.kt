package com.vga.spinwheel.ui.screen.coin

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun CoinResultScreen(
    navController: NavController,
    isHeads: Boolean,
    viewModel: CoinViewModel = hiltViewModel()
) {
    val skin by viewModel.currentSkin.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SpinTopBar(
                title = "Kết Quả",
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Home,
                        contentDescription = "Home",
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                }
            )
        },
        containerColor = SpinColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Main Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF3D3D3C))
                    .border(1.5.dp, Color.White.copy(alpha = 0.62f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                // The blue background specifically behind the coin
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(216.dp) // 54% of 400
                        .background(Color(0xFF292640)), // Dark blue matching the screen bg
                    contentAlignment = Alignment.Center
                ) {
                    if (isHeads) {
                        Image(
                            painter = painterResource(id = skin.headDrawable),
                            contentDescription = "Coin Head",
                            modifier = Modifier.size(320.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = skin.tailDrawable),
                            contentDescription = "Coin Tail",
                            modifier = Modifier
                                .size(320.dp)
                                .graphicsLayer {
                                    // Fix reversed text on tail side if we used a single model with 180 flip
                                    rotationY = 180f
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            // Share Button
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Tôi vừa tung được mặt ${if (isHeads) "Sấp" else "Ngửa"}!")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF39A9F2), // Blue share button
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(9.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 13.dp),
                modifier = Modifier.width(180.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    // Using SpinIconGlyph.Share if available, or just text
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_share), 
                        contentDescription = "Share",
                        modifier = Modifier.size(26.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Chia sẻ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Retry Button
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDE3D2D), // Red/Orange button
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Thử lại",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
