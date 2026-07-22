package com.vga.spinwheel.ui.screen.wheel

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelResultScreen(
    wheelId: String,
    resultId: String,
    viewModel: WheelViewModel,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentWheel by viewModel.currentWheel.collectAsState()
    val spinStatus by viewModel.spinStatus.collectAsState()
    val activeItems by viewModel.activeItems.collectAsState()
    val paletteIdx by viewModel.paletteIndex.collectAsState()

    val palette = WheelPalettes.getPalette(paletteIdx)

    val winnerName = (spinStatus as? SpinStatus.Finished)?.winner?.name
        ?: viewModel.history.collectAsState().value.firstOrNull { it.id == resultId }?.value
        ?: "Kết Quả"

    fun shareResult() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Kết quả quay bánh xe")
            putExtra(
                Intent.EXTRA_TEXT,
                "Bánh xe '${currentWheel?.name ?: "Bánh Xe"}' đã quay trúng: ★ $winnerName ★! Tải ứng dụng Spin Wheel ngay!"
            )
        }
        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Kết Quả",
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Home,
                        contentDescription = "Trang chủ",
                        onClick = onHome,
                        tint = Color.White,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Main Result Container Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2845)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Result Winner Title / Number
                    Text(
                        text = winnerName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stopped Wheel Canvas Display
                    WheelCanvas(
                        items = activeItems,
                        palette = palette,
                        spinStatus = spinStatus,
                        durationSeconds = 0,
                        onSpinFinished = {},
                        onClickSpin = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                    )
                }
            }

            // Share Pill Button & Retry Long Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Light Blue Share Pill Button
                Button(
                    onClick = { shareResult() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6)),
                    modifier = Modifier.height(46.dp),
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Share,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Chia sẻ kết quả",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Long Orange Retry Button
                Button(
                    onClick = {
                        viewModel.resetSpin()
                        onRetry()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF6C00),
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = "Thử lại",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
