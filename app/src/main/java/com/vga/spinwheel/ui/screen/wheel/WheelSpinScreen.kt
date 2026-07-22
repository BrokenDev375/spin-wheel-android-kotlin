package com.vga.spinwheel.ui.screen.wheel

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinBottomActionBar
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.components.SpinSecondaryButton
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelSpinScreen(
    wheelId: String,
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHistory: () -> Unit,
    onResult: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentWheel by viewModel.currentWheel.collectAsState()
    val activeItems by viewModel.activeItems.collectAsState()
    val spinStatus by viewModel.spinStatus.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val paletteIdx by viewModel.paletteIndex.collectAsState()

    val palette = WheelPalettes.getPalette(paletteIdx)

    LaunchedEffect(wheelId) {
        viewModel.loadWheelForSpin(wheelId)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = currentWheel?.name ?: "Bánh Xe",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.History,
                        contentDescription = "Lịch sử",
                        onClick = onOpenHistory,
                    )
                    SpinIconButton(
                        glyph = SpinIconGlyph.Settings,
                        contentDescription = "Cài đặt",
                        onClick = onOpenSettings,
                    )
                },
            )
        },
        bottomBar = {
            SpinBottomActionBar {
                SpinSecondaryButton(
                    text = "Xáo trộn",
                    onClick = viewModel::shuffleActiveItems,
                    modifier = Modifier.weight(1f),
                )
                SpinPrimaryButton(
                    text = if (spinStatus is SpinStatus.Spinning) "ĐANG QUAY..." else "NHẤN ĐỂ QUAY",
                    onClick = viewModel::startSpin,
                    enabled = spinStatus !is SpinStatus.Spinning && activeItems.size >= 2,
                    modifier = Modifier.weight(2f),
                )
                SpinSecondaryButton(
                    text = "Khôi phục",
                    onClick = viewModel::resetSpin,
                    modifier = Modifier.weight(1f),
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
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Result Display Banner
            val resultText = when (val status = spinStatus) {
                is SpinStatus.Finished -> status.winner.name
                is SpinStatus.Spinning -> "Đang quay..."
                SpinStatus.Idle -> "???"
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.headlineLarge,
                    color = if (spinStatus is SpinStatus.Finished) SpinColors.Premium else SpinColors.TextPrimary,
                    textAlign = TextAlign.Center,
                )
            }

            // Canvas Spin Wheel
            WheelCanvas(
                items = activeItems,
                palette = palette,
                spinStatus = spinStatus,
                durationSeconds = duration,
                onSpinFinished = { winner ->
                    viewModel.onSpinCompleted(winner)
                },
                onClickSpin = {
                    if (spinStatus !is SpinStatus.Spinning && activeItems.size >= 2) {
                        viewModel.startSpin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            // When spin is finished, navigate to result screen
            if (spinStatus is SpinStatus.Finished) {
                val finishedStatus = spinStatus as SpinStatus.Finished
                LaunchedEffect(finishedStatus.resultId) {
                    onResult(wheelId, finishedStatus.resultId)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
