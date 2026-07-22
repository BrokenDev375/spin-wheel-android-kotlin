package com.vga.spinwheel.ui.screen.wheel

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinBottomActionBar
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.components.SpinSecondaryButton
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
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
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onRetry,
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Home,
                        contentDescription = "Trang chủ",
                        onClick = onHome,
                    )
                },
            )
        },
        bottomBar = {
            SpinBottomActionBar {
                SpinSecondaryButton(
                    text = "Chia sẻ",
                    onClick = { shareResult() },
                    modifier = Modifier.weight(1f),
                )
                SpinPrimaryButton(
                    text = "Thử lại",
                    onClick = {
                        viewModel.resetSpin()
                        onRetry()
                    },
                    modifier = Modifier.weight(1.5f),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.Wheel,
                tint = SpinColors.Premium,
                modifier = Modifier.size(96.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "KẾT QUẢ QUAY",
                style = MaterialTheme.typography.titleMedium,
                color = SpinColors.TextMuted,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(SpinRadius.Card))
                    .background(SpinColors.Card)
                    .padding(vertical = 28.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = winnerName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = SpinColors.Premium,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentWheel?.name ?: "Bánh Xe",
                style = MaterialTheme.typography.bodyMedium,
                color = SpinColors.TextMuted,
            )
        }
    }
}
