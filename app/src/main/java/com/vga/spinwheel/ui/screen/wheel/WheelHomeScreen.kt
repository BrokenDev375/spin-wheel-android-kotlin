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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.vga.spinwheel.data.model.Wheel
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
fun WheelHomeScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    onAddWheel: () -> Unit,
    onEditWheel: (String) -> Unit,
    onSpinWheel: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    val showAiModal by viewModel.showAiModal.collectAsState()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Bánh Xe",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SpinPrimaryButton(
                    text = "Tạo Bánh Xe Mới",
                    onClick = onAddWheel,
                    modifier = Modifier.weight(1f),
                )
                SpinSecondaryButton(
                    text = "Trình Tạo AI",
                    onClick = { viewModel.showAiModal(true) },
                    modifier = Modifier.weight(1f),
                )
            }

            if (wheels.isEmpty()) {
                WheelEmptyState(
                    onAddWheel = onAddWheel,
                    onAiGenerate = { viewModel.showAiModal(true) },
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(wheels, key = { it.id }) { wheel ->
                        WheelItemCard(
                            wheel = wheel,
                            onClick = { onSpinWheel(wheel.id) },
                            onEdit = { onEditWheel(wheel.id) },
                            onDuplicate = { viewModel.duplicateWheel(wheel.id) },
                            onDelete = { deleteTargetId = wheel.id },
                        )
                    }
                }
            }
        }
    }

    if (deleteTargetId != null) {
        AlertDialog(
            onDismissRequest = { deleteTargetId = null },
            title = { Text("Xác nhận xóa", color = SpinColors.TextPrimary) },
            text = { Text("Bạn có chắc chắn muốn xóa bánh xe này không?", color = SpinColors.TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTargetId?.let { viewModel.deleteWheel(it) }
                        deleteTargetId = null
                    }
                ) {
                    Text("Xóa", color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTargetId = null }) {
                    Text("Hủy", color = SpinColors.TextPrimary)
                }
            },
            containerColor = SpinColors.Card,
        )
    }

    if (showAiModal) {
        WheelAiGenerateDialog(
            topics = viewModel.aiTopics,
            onSelectTopic = { topic ->
                viewModel.generateAiWheel(topic)
                onAddWheel()
            },
            onCustomPrompt = { prompt ->
                viewModel.generateAiCustom(prompt)
                onAddWheel()
            },
            onDismiss = { viewModel.showAiModal(false) },
        )
    }
}

@Composable
private fun WheelEmptyState(
    onAddWheel: () -> Unit,
    onAiGenerate: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SpinIcon(
            glyph = SpinIconGlyph.Wheel,
            tint = SpinColors.Action,
            modifier = Modifier.size(80.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chưa có bánh xe nào",
            style = MaterialTheme.typography.titleLarge,
            color = SpinColors.TextPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Hãy tạo bánh xe đầu tiên của bạn hoặc dùng AI gợi ý để bắt đầu quay!",
            style = MaterialTheme.typography.bodyMedium,
            color = SpinColors.TextMuted,
        )
    }
}

@Composable
private fun WheelItemCard(
    wheel: Wheel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SpinRadius.Card))
            .background(SpinColors.Card)
            .border(1.dp, SpinColors.CardBorder, RoundedCornerShape(SpinRadius.Card))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SpinIcon(
            glyph = SpinIconGlyph.Wheel,
            tint = SpinColors.Premium,
            modifier = Modifier.size(36.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
        ) {
            Text(
                text = wheel.name,
                style = MaterialTheme.typography.titleMedium,
                color = SpinColors.TextPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${wheel.items.size} tùy chọn",
                style = MaterialTheme.typography.bodyMedium,
                color = SpinColors.TextMuted,
            )
        }
        Box {
            SpinIconButton(
                glyph = SpinIconGlyph.More,
                contentDescription = "Menu",
                onClick = { menuExpanded = true },
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(SpinColors.Card),
            ) {
                DropdownMenuItem(
                    text = { Text("Sửa", color = SpinColors.TextPrimary) },
                    onClick = { menuExpanded = false; onEdit() },
                )
                DropdownMenuItem(
                    text = { Text("Nhân bản", color = SpinColors.TextPrimary) },
                    onClick = { menuExpanded = false; onDuplicate() },
                )
                DropdownMenuItem(
                    text = { Text("Xóa", color = Color(0xFFFF5252)) },
                    onClick = { menuExpanded = false; onDelete() },
                )
            }
        }
    }
}
