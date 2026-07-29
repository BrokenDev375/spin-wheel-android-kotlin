package com.vga.spinwheel.ui.components

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelSelectionList(
    wheels: List<Wheel>,
    onAiGenerate: () -> Unit,
    onCreateWheel: () -> Unit,
    onOpenWheel: (Wheel) -> Unit,
    onEditWheel: (Wheel) -> Unit,
    onDuplicateWheel: (Wheel) -> Unit,
    onDeleteWheel: (Wheel) -> Unit,
    modifier: Modifier = Modifier,
    canEditWheel: (Wheel) -> Boolean = { true },
    canDeleteWheel: (Wheel) -> Boolean = { true },
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = SpinSpacing.ScreenHorizontal),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        WheelSelectionActionButton(
            text = stringResource(R.string.generatorai),
            glyph = SpinIconGlyph.Sparkles,
            onClick = onAiGenerate,
            background = Brush.horizontalGradient(
                colors = listOf(Color(0xFF0DA8E8), Color(0xFF22F198)),
            ),
            iconSize = 24.dp,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
        )

        Spacer(modifier = Modifier.height(14.dp))

        WheelSelectionActionButton(
            text = stringResource(R.string.create),
            glyph = SpinIconGlyph.Plus,
            onClick = onCreateWheel,
            background = Brush.linearGradient(
                colors = listOf(WheelSelectionCardColor, WheelSelectionCardColor),
            ),
            iconSize = 20.dp,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            showBorder = true,
        )

        Spacer(modifier = Modifier.height(18.dp))

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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(wheels, key = { it.id }) { wheel ->
                    WheelSelectionCard(
                        wheel = wheel,
                        canEdit = canEditWheel(wheel),
                        canDelete = canDeleteWheel(wheel),
                        onClick = { onOpenWheel(wheel) },
                        onEdit = { onEditWheel(wheel) },
                        onDuplicate = { onDuplicateWheel(wheel) },
                        onDelete = { onDeleteWheel(wheel) },
                    )
                }
            }
        }
    }
}

@Composable
private fun WheelSelectionActionButton(
    text: String,
    glyph: SpinIconGlyph,
    onClick: () -> Unit,
    background: Brush,
    iconSize: Dp,
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
                    Modifier.border(1.dp, Color.White.copy(alpha = 0.08f), shape)
                } else {
                    Modifier
                },
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SpinIcon(
                glyph = glyph,
                tint = Color.White,
                modifier = Modifier.size(iconSize),
            )
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize,
                fontWeight = fontWeight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun WheelSelectionCard(
    wheel: Wheel,
    canEdit: Boolean,
    canDelete: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)
    val itemLabel = stringResource(R.string.item)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(shape)
            .background(WheelSelectionCardColor)
            .border(1.dp, Color.White.copy(alpha = 0.06f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
    ) {
        Text(
            text = wheel.name,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(end = 42.dp),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = "${wheel.items.size} $itemLabel",
            modifier = Modifier.align(Alignment.BottomStart),
            color = SpinColors.TextMuted,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            SpinIconButton(
                glyph = SpinIconGlyph.More,
                contentDescription = stringResource(R.string.customsize),
                onClick = { menuExpanded = true },
                tint = SpinColors.TextMuted,
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(Color(0xFF2D2845)),
            ) {
                if (canEdit) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.edit), color = SpinColors.TextPrimary) },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        },
                    )
                }
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.duplicate), color = SpinColors.TextPrimary) },
                    onClick = {
                        menuExpanded = false
                        onDuplicate()
                    },
                )
                if (canDelete) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.delete), color = Color(0xFFFF5252)) },
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

private val WheelSelectionCardColor = Color(0xFF393347)
