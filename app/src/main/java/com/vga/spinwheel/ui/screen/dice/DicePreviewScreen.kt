package com.vga.spinwheel.ui.screen.dice

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

@Composable
fun DicePreviewScreen(
    viewModel: DiceViewModel,
    onHome: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val total = uiState.currentResults.sum()

    SpinResultScreen(
        title = "Kết Quả",
        onHome = onHome,
        onShare = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val valuesText = uiState.currentResults.joinToString(", ")
                putExtra(Intent.EXTRA_TEXT, "Tổng điểm Xúc Xắc: $total ($valuesText) - app Spin Wheel")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share"))
        },
        onRetry = {
            viewModel.resetResults()
            onRetry()
        },
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Total chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF12966))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = total.toString(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Grid of dice
            val columns = when (uiState.diceCount) {
                1 -> 1
                2 -> 2
                3 -> 2
                4 -> 2
                else -> 3
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.currentResults) { value ->
                    DiceTile(
                        styleIndex = uiState.styleIndex,
                        modifier = Modifier.size(100.dp)
                    ) {
                        DiceFace(
                            value = value,
                            styleIndex = uiState.styleIndex,
                            isShaking = false
                        )
                    }
                }
            }
        }
    }
}
