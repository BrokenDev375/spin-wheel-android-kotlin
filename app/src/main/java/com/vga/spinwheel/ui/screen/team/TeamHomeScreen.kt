package com.vga.spinwheel.ui.screen.team

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.screen.wheel.WheelAiGenerateDialog
import com.vga.spinwheel.ui.screen.wheel.WheelViewModel
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun TeamHomeScreen(
    viewModel: TeamViewModel,
    wheelFormViewModel: WheelViewModel,
    onBack: () -> Unit,
    onAddList: () -> Unit,
    onOpenPreparedForm: () -> Unit,
    onEditList: (String) -> Unit,
    onOpenList: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    val showAiModal by wheelFormViewModel.showAiModal.collectAsState()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }

    SpinScreen(
        title = "Đội",
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            TeamAiButton(
                text = "Trình tạo AI",
                onClick = { wheelFormViewModel.showAiModal(true) },
            )

            Spacer(modifier = Modifier.height(12.dp))

            TeamCreateButton(
                text = "Tạo Bánh Xe mới",
                onClick = onAddList,
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (wheels.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Text(
                        text = "Không có bánh xe. Thêm mới đi!",
                        modifier = Modifier.padding(top = 48.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = SpinColors.TextMuted,
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(wheels, key = { it.id }) { wheel ->
                        TeamListCard(
                            wheel = wheel,
                            onClick = { onOpenList(wheel.id) },
                            onEdit = { onEditList(wheel.id) },
                            onDuplicate = { viewModel.duplicateList(wheel.id) },
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
            text = {
                Text(
                    text = "Bạn có chắc chắn muốn xóa bánh xe này không?",
                    color = SpinColors.TextMuted,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTargetId?.let { viewModel.deleteList(it) }
                        deleteTargetId = null
                    }
                ) {
                    Text("Xóa", color = Color(0xFFFF3B30))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTargetId = null }) {
                    Text("Hủy", color = SpinColors.TextPrimary)
                }
            },
            containerColor = Color(0xFF2D2845),
        )
    }

    if (showAiModal) {
        WheelAiGenerateDialog(
            topics = wheelFormViewModel.aiTopics,
            onSelectTopic = { topic ->
                wheelFormViewModel.generateAiWheel(topic)
                onOpenPreparedForm()
            },
            onCustomPrompt = { prompt ->
                wheelFormViewModel.generateAiCustom(prompt)
                onOpenPreparedForm()
            },
            onDismiss = { wheelFormViewModel.showAiModal(false) },
        )
    }
}

@Composable
private fun TeamAiButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF18B7FF),
                        Color(0xFF2BF48B),
                    ),
                ),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.Sparkles,
                tint = Color.White,
                modifier = Modifier.size(26.dp),
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Composable
private fun TeamCreateButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF37324A))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.Plus,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun TeamListCard(
    wheel: Wheel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .clip(RoundedCornerShape(SpinRadius.Card))
            .background(Color(0xFF343047))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.06f),
                shape = RoundedCornerShape(SpinRadius.Card),
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = wheel.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "${wheel.items.size} options",
                color = SpinColors.TextMuted,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            SpinIconButton(
                glyph = SpinIconGlyph.More,
                contentDescription = "Tùy chọn",
                onClick = { menuExpanded = true },
                tint = SpinColors.TextMuted,
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(Color(0xFF3B354C)),
            ) {
                DropdownMenuItem(
                    text = { Text("Sửa", color = SpinColors.TextPrimary) },
                    onClick = {
                        menuExpanded = false
                        onEdit()
                    },
                )
                DropdownMenuItem(
                    text = { Text("Nhân bản", color = SpinColors.TextPrimary) },
                    onClick = {
                        menuExpanded = false
                        onDuplicate()
                    },
                )
                DropdownMenuItem(
                    text = { Text("Xóa", color = Color(0xFFFF3B30)) },
                    onClick = {
                        menuExpanded = false
                        onDelete()
                    },
                )
            }
        }
    }
}
