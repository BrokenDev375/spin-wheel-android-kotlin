package com.vga.spinwheel.ui.screen.wheel

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.theme.SpinColors

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

    SpinResultScreen(
        title = "Kết Quả",
        onHome = onHome,
        onShare = { shareResult() },
        onRetry = {
            viewModel.resetSpin()
            onRetry()
        },
        cardHeight = 500.dp,
        cardContentPadding = 0.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(44.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpinColors.Background)
                    .padding(top = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = winnerName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(10.dp))

                WheelCanvas(
                    items = activeItems,
                    palette = palette,
                    spinStatus = spinStatus,
                    durationSeconds = 0,
                    onSpinFinished = {},
                    onClickSpin = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
