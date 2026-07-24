package com.vga.spinwheel.ui.screen.drawing

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingHomeScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onAddWheel: () -> Unit,
    onAiGenerate: () -> Unit,
    onEditWheel: (String) -> Unit,
    onSpinWheel: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }
    val displayWheels = wheels.ifEmpty { listOf(DrawingViewModel.DRAWING_FALLBACK_WHEEL) }

    SpinScreen(
        title = "Vẽ",
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        modifier = modifier,
    ) { contentModifier ->
        Column(
            modifier = contentModifier.padding(horizontal = 18.dp),
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            DrawingHomeAction(
                text = "Trình tạo AI",
                icon = SpinIconGlyph.Sparkles,
                onClick = onAiGenerate,
                background = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF0DA8E8), Color(0xFF22F198)),
                ),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
            )

            Spacer(modifier = Modifier.height(14.dp))

            DrawingHomeAction(
                text = "Tạo Bánh xe mới",
                icon = SpinIconGlyph.Plus,
                onClick = onAddWheel,
                background = Brush.linearGradient(
                    colors = listOf(Color(0xFF393347), Color(0xFF393347)),
                ),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                showBorder = true,
            )

            Spacer(modifier = Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(displayWheels, key = { it.id }) { wheel ->
                    DrawingItemCard(
                        wheel = wheel,
                        onClick = { onSpinWheel(wheel.id) },
                        onEdit = { onEditWheel(wheel.id) },
                        onDuplicate = { viewModel.cloneWheel(wheel.id) },
                        onDelete = { deleteTargetId = wheel.id },
                    )
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
                    "Bạn có chắc chắn muốn xóa danh sách này không?",
                    color = SpinColors.TextMuted,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTargetId?.let(viewModel::deleteWheel)
                        deleteTargetId = null
                    },
                ) {
                    Text("Xoá", color = Color(0xFFFF5252))
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
}

@Composable
private fun DrawingHomeAction(
    text: String,
    icon: SpinIconGlyph,
    onClick: () -> Unit,
    background: Brush,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    showBorder: Boolean = false,
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(shape)
            .background(background)
            .then(
                if (showBorder) {
                    Modifier.border(1.dp, Color.White.copy(alpha = 0.06f), shape)
                } else {
                    Modifier
                },
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            SpinIcon(
                glyph = icon,
                tint = Color.White,
                modifier = Modifier.size(if (icon == SpinIconGlyph.Sparkles) 24.dp else 20.dp),
            )
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize,
                fontWeight = fontWeight,
            )
        }
    }
}

@Composable
private fun DrawingItemCard(
    wheel: Wheel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(shape)
            .background(Color(0xFF393347))
            .border(1.5.dp, Color.White.copy(alpha = 0.05f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
    ) {
        Text(
            text = wheel.name,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(0.72f),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = "${wheel.items.size} options",
            modifier = Modifier.align(Alignment.BottomStart),
            color = Color.White.copy(alpha = 0.52f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
        )

        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            SpinIconButton(
                glyph = SpinIconGlyph.More,
                contentDescription = "Tùy chọn",
                onClick = { menuExpanded = true },
                modifier = Modifier.size(30.dp),
                tint = Color.White.copy(alpha = 0.72f),
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(Color(0xFF2D2845)),
            ) {
                if (wheel.id != DrawingViewModel.DRAWING_FALLBACK_WHEEL.id) {
                    DropdownMenuItem(
                        text = { Text("Sửa", color = SpinColors.TextPrimary) },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        },
                    )
                }
                DropdownMenuItem(
                    text = { Text("Nhân bản", color = SpinColors.TextPrimary) },
                    onClick = {
                        menuExpanded = false
                        onDuplicate()
                    },
                )
                if (wheel.id != DrawingViewModel.DRAWING_FALLBACK_WHEEL.id) {
                    DropdownMenuItem(
                        text = { Text("Xoá", color = Color(0xFFFF5252)) },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        },
                    )
                }
            }
        }
    }
}
