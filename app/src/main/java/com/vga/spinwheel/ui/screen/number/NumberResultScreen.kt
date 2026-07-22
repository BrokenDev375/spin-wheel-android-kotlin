package com.vga.spinwheel.ui.screen.number

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun NumberResultScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel()
) {
    val lastResult by viewModel.lastResult.collectAsState()
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(216.dp)
                        .background(Color(0xFF292640)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lastResult ?: "N/A",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            // Share Button
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Kết quả số ngẫu nhiên của tôi: ${lastResult ?: ""}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF39A9F2),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(9.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 13.dp),
                modifier = Modifier.width(180.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
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
                onClick = {
                    viewModel.clearLastResult()
                    navController.popBackStack() 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDE3D2D),
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
