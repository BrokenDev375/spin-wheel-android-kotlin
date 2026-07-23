package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

import com.vga.spinwheel.ui.components.SpinScreen

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

    SpinScreen(
        title = "Bánh xe",
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        confirmExitOnBack = true,
        actions = {
            SpinIconButton(
                glyph = SpinIconGlyph.History,
                contentDescription = "Lịch sử",
                onClick = onOpenHistory,
                tint = Color.White,
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(8.dp))

                // Wheel Name Pill Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF3B3754))
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = currentWheel?.name ?: "Bánh Xe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Result Text
                val resultText = when (val status = spinStatus) {
                    is SpinStatus.Finished -> status.winner.name
                    is SpinStatus.Spinning -> "Đang quay..."
                    SpinStatus.Idle -> "???"
                }

                Text(
                    text = resultText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }

            // Wheel Canvas Component
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

            // Bottom Control Action Row (Sliders, Spin, Shuffle, Reset)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Settings / Sliders Icon Button
                Button(
                    onClick = onOpenSettings,
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B3754)),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    SpinIcon(glyph = SpinIconGlyph.Sliders, tint = Color.White, modifier = Modifier.size(22.dp))
                }

                // Primary Spin Button
                Button(
                    onClick = viewModel::startSpin,
                    enabled = spinStatus !is SpinStatus.Spinning && activeItems.size >= 2,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B3754),
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = if (spinStatus is SpinStatus.Spinning) "ĐANG QUAY..." else "NHẤN ĐỂ QUAY",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // Shuffle Button
                Button(
                    onClick = viewModel::shuffleActiveItems,
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B3754)),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    SpinIcon(glyph = SpinIconGlyph.Shuffle, tint = Color.White, modifier = Modifier.size(22.dp))
                }

                // Reset Button
                Button(
                    onClick = viewModel::resetSpin,
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B3754)),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    SpinIcon(glyph = SpinIconGlyph.Reset, tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}
