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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
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
                title = stringResource(R.string.spinwheel),
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = onBack,
                centerTitle = false,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // AI Generator Button (Full Width, Green/Teal, Sparkles Icon)
            Button(
                onClick = { viewModel.showAiModal(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF00A8FF), Color(0xFF00EA97)),
                        )
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Sparkles,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.generatorai),
                        fontSize = 18.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Create New Wheel Button (Full Width, Dark Purple)
            Button(
                onClick = onAddWheel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B3754),
                    contentColor = Color.White,
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Plus,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = stringResource(R.string.create),
                        fontSize = 17.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (wheels.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Text(
                        text = stringResource(R.string.no_wheels),
                        style = MaterialTheme.typography.titleMedium,
                        color = SpinColors.TextMuted,
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
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
            title = { Text(stringResource(R.string.confirm), color = SpinColors.TextPrimary) },
            text = { Text(stringResource(R.string.confirm_delete), color = SpinColors.TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTargetId?.let { viewModel.deleteWheel(it) }
                        deleteTargetId = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTargetId = null }) {
                    Text(stringResource(R.string.cancel), color = SpinColors.TextPrimary)
                }
            },
            containerColor = Color(0xFF2D2845),
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
private fun WheelItemCard(
    wheel: Wheel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val itemLabel = stringResource(R.string.item)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(SpinRadius.Card))
            .background(Color(0xFF3B3754))
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = wheel.name,
                style = MaterialTheme.typography.titleMedium,
                color = SpinColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 34.dp),
            )
            Text(
                text = "${wheel.items.size} $itemLabel",
                style = MaterialTheme.typography.bodyMedium,
                color = SpinColors.TextMuted,
            )
        }
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            SpinIconButton(
                glyph = SpinIconGlyph.More,
                contentDescription = "Menu",
                onClick = { menuExpanded = true },
                modifier = Modifier.size(32.dp),
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(Color(0xFF2D2845)),
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit), color = SpinColors.TextPrimary) },
                    onClick = { menuExpanded = false; onEdit() },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.duplicate), color = SpinColors.TextPrimary) },
                    onClick = { menuExpanded = false; onDuplicate() },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete), color = Color(0xFFFF5252)) },
                    onClick = { menuExpanded = false; onDelete() },
                )
            }
        }
    }
}
