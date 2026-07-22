package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinStepper
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelSettingsScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val duration by viewModel.duration.collectAsState()
    val paletteIdx by viewModel.paletteIndex.collectAsState()
    val removeWinner by viewModel.removeWinner.collectAsState()

    var showPaletteDialog by remember { mutableStateOf(false) }

    val currentPalette = WheelPalettes.getPalette(paletteIdx)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Tùy Chỉnh Bánh Xe",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = SpinSpacing.ScreenHorizontal,
                vertical = 14.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                SpinSettingRow(
                    title = "Thời lượng hoạt hình",
                    subtitle = "$duration giây",
                    trailing = {
                        SpinStepper(
                            value = "${duration}s",
                            onMinus = { viewModel.updateDuration(duration - 1) },
                            onPlus = { viewModel.updateDuration(duration + 1) },
                        )
                    },
                )
            }

            item {
                SpinSettingRow(
                    title = "Bảng màu",
                    subtitle = currentPalette.name,
                    onClick = { showPaletteDialog = true },
                    trailing = {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            currentPalette.colors.take(4).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(color),
                                )
                            }
                        }
                    },
                )
            }

            item {
                SpinSettingRow(
                    title = "Xóa người thắng",
                    subtitle = "Tự động loại tùy chọn trúng khỏi lượt quay tiếp theo",
                    trailing = {
                        SpinToggle(
                            checked = removeWinner,
                            onCheckedChange = viewModel::toggleRemoveWinner,
                        )
                    },
                )
            }
        }
    }

    if (showPaletteDialog) {
        PaletteSelectionDialog(
            selectedIdx = paletteIdx,
            onSelectPalette = { idx ->
                viewModel.updatePalette(idx)
                showPaletteDialog = false
            },
            onDismiss = { showPaletteDialog = false },
        )
    }
}

@Composable
private fun PaletteSelectionDialog(
    selectedIdx: Int,
    onSelectPalette: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Chọn Bảng Màu",
                style = MaterialTheme.typography.titleLarge,
                color = SpinColors.TextPrimary,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                WheelPalettes.all.forEachIndexed { index, palette ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SpinRadius.Control))
                            .background(
                                if (index == selectedIdx) SpinColors.Action.copy(alpha = 0.2f)
                                else Color.White.copy(alpha = 0.08f)
                            )
                            .border(
                                width = 1.dp,
                                color = if (index == selectedIdx) SpinColors.Action else Color.Transparent,
                                shape = RoundedCornerShape(SpinRadius.Control),
                            )
                            .clickable { onSelectPalette(index) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = palette.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = SpinColors.TextPrimary,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            palette.colors.take(5).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(color),
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = SpinColors.TextMuted)
            }
        },
        containerColor = SpinColors.Card,
    )
}
