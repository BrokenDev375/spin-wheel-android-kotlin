package com.vga.spinwheel.ui.screen.dice

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DicePreviewScreen(
    viewModel: DiceViewModel,
    onHome: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val results = uiState.currentResults.ifEmpty { List(uiState.diceCount) { 1 } }
    val total = uiState.currentResults.sum()

    SpinResultScreen(
        title = "Kết Quả",
        onHome = onHome,
        onShare = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val valuesText = results.joinToString(", ")
                putExtra(Intent.EXTRA_TEXT, "Tổng điểm Xúc Xắc: $total ($valuesText) - app Spin Wheel")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share"))
        },
        onRetry = {
            viewModel.resetResults()
            onRetry()
        },
        shareText = "Chia sẻ kết quả",
        cardHeight = 450.dp,
        cardContentPadding = 0.dp,
        cardBackgroundColor = SpinColors.Background,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 50.dp, height = 46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = total.toString(),
                    color = Color(0xFFEC9213),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Black,
                )
            }

            DiceGrid(
                values = results,
                styleIndex = uiState.styleIndex,
            )
        }
    }
}
