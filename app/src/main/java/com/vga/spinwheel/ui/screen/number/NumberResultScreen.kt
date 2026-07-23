package com.vga.spinwheel.ui.screen.number

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.nav.Screen

@Composable
fun NumberResultScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel()
) {
    val lastResult by viewModel.lastResult.collectAsState()
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
                putExtra(Intent.EXTRA_TEXT, "Kết quả số ngẫu nhiên của tôi: ${lastResult ?: ""}")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
        },
        onRetry = {
            viewModel.clearLastResult()
            navController.popBackStack()
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF292640)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = lastResult ?: "N/A",
                color = Color.White,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
