package com.vga.spinwheel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vga.spinwheel.R
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun WheelSelectorChip(
    name: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF3B3754),
    contentColor: Color = Color.White,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    horizontalPadding: Dp = 24.dp,
    verticalPadding: Dp = 6.dp,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickableWithSound(enabled = enabled, onClick = onClick)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = name,
            color = contentColor.copy(alpha = if (enabled) 1f else 0.72f),
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false),
        )
        SpinIcon(
            glyph = SpinIconGlyph.ChevronDown,
            tint = contentColor.copy(alpha = if (enabled) 0.9f else 0.42f),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
fun WheelPickerDialog(
    title: String,
    wheels: List<Wheel>,
    selectedWheelId: String?,
    canEditSelected: Boolean,
    onSelectWheel: (String) -> Unit,
    onEditSelectedItems: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedId = selectedWheelId.orEmpty()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF2E293E),
            modifier = modifier.fillMaxWidth(0.92f),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = title,
                        color = SpinColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    SpinIconButton(
                        glyph = SpinIconGlyph.Close,
                        contentDescription = stringResource(R.string.close),
                        onClick = onDismiss,
                        tint = Color.White,
                    )
                }

                if (canEditSelected) {
                    Spacer(modifier = Modifier.height(14.dp))
                    WheelPickerEditRow(
                        text = stringResource(R.string.edit_wheel),
                        onClick = onEditSelectedItems,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (wheels.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.no_wheels),
                            color = SpinColors.TextMuted,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 330.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(wheels, key = { it.id }) { wheel ->
                            WheelPickerRow(
                                wheel = wheel,
                                selected = wheel.id == selectedId,
                                onClick = {
                                    if (wheel.id == selectedId) {
                                        onDismiss()
                                    } else {
                                        onSelectWheel(wheel.id)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WheelItemsEditDialog(
    title: String,
    wheelName: String,
    items: List<WheelItem>,
    showPriorityControls: Boolean,
    onSave: (String, List<WheelItem>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheelNameLabel = stringResource(R.string.itemwhell)
    val itemNameLabel = stringResource(R.string.itemsname)
    val needWheelName = stringResource(R.string.pleaseWhell)
    val needTwoItems = stringResource(R.string.need_two_items)
    var draftWheelName by remember(wheelName) { mutableStateOf(wheelName) }
    var draftItems by remember(items) {
        mutableStateOf(items.ifEmpty { listOf(newDraftItem(1), newDraftItem(2)) })
    }
    var errorMessage by remember(wheelName, items) { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF2E293E),
            modifier = modifier.fillMaxWidth(0.94f),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = title,
                        color = SpinColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    SpinIconButton(
                        glyph = SpinIconGlyph.Close,
                        contentDescription = stringResource(R.string.close),
                        onClick = onDismiss,
                        tint = Color.White,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = draftWheelName,
                    onValueChange = { name ->
                        errorMessage = null
                        draftWheelName = name
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp),
                    singleLine = true,
                    label = { Text(wheelNameLabel) },
                    isError = errorMessage == needWheelName,
                    colors = wheelTextFieldColors(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(draftItems, key = { it.id }) { item ->
                        WheelItemEditRow(
                            item = item,
                            itemNameLabel = itemNameLabel,
                            showPriorityControls = showPriorityControls,
                            canDelete = draftItems.size > 2,
                            onNameChange = { name ->
                                errorMessage = null
                                draftItems = draftItems.map {
                                    if (it.id == item.id) it.copy(name = name) else it
                                }
                            },
                            onPriorityChange = { delta ->
                                draftItems = draftItems.map {
                                    if (it.id == item.id) {
                                        it.copy(priority = (it.priority + delta).coerceAtLeast(1))
                                    } else {
                                        it
                                    }
                                }
                            },
                            onDelete = {
                                errorMessage = null
                                draftItems = draftItems.filterNot { it.id == item.id }
                            },
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage.orEmpty(),
                        color = Color(0xFFFF7A7A),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = rememberClickWithSound {
                        errorMessage = null
                        draftItems = draftItems + newDraftItem(draftItems.size + 1)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF393347),
                        contentColor = Color.White,
                    ),
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Plus,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.addItem),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = rememberClickWithSound(onDismiss)) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = SpinColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = rememberClickWithSound {
                            val cleanedName = draftWheelName.trim()
                            val cleaned = draftItems
                                .map {
                                    it.copy(
                                        name = it.name.trim(),
                                        priority = it.priority.coerceAtLeast(1),
                                    )
                                }
                                .filter { it.name.isNotBlank() }

                            when {
                                cleanedName.isBlank() -> errorMessage = needWheelName
                                cleaned.size < 2 -> errorMessage = needTwoItems
                                else -> onSave(cleanedName, cleaned)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC9213),
                            contentColor = Color(0xFF111111),
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            fontWeight = FontWeight.Black,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WheelPickerEditRow(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFEC9213))
            .clickableWithSound(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SpinIcon(
            glyph = SpinIconGlyph.Settings,
            tint = Color(0xFF111111),
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = text,
            color = Color(0xFF111111),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun WheelPickerRow(
    wheel: Wheel,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    val itemLabel = stringResource(R.string.item)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(shape)
            .background(if (selected) Color(0xFF4B445F) else Color(0xFF393347))
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFFEC9213) else Color.White.copy(alpha = 0.06f),
                shape = shape,
            )
            .clickableWithSound(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SpinIcon(
            glyph = SpinIconGlyph.Wheel,
            tint = if (selected) Color(0xFFEC9213) else Color.White.copy(alpha = 0.76f),
            modifier = Modifier.size(24.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = wheel.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${wheel.items.size} $itemLabel",
                color = Color.White.copy(alpha = 0.54f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (selected) {
            SpinIcon(
                glyph = SpinIconGlyph.Check,
                tint = Color(0xFFEC9213),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun WheelItemEditRow(
    item: WheelItem,
    itemNameLabel: String,
    showPriorityControls: Boolean,
    canDelete: Boolean,
    onNameChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.06f),
                shape = RoundedCornerShape(14.dp),
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = item.name,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp),
            singleLine = false,
            minLines = 1,
            maxLines = 2,
            label = { Text(itemNameLabel) },
            colors = wheelTextFieldColors(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            if (showPriorityControls) {
                WheelPriorityStepper(
                    value = item.priority,
                    onPriorityChange = onPriorityChange,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            SpinIconButton(
                glyph = SpinIconGlyph.Trash,
                contentDescription = stringResource(R.string.delete),
                onClick = onDelete,
                enabled = canDelete,
                tint = if (canDelete) Color(0xFFFF7A7A) else Color.White.copy(alpha = 0.28f),
            )
        }
    }
}

@Composable
private fun WheelPriorityStepper(
    value: Int,
    onPriorityChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SpinIconButton(
            glyph = SpinIconGlyph.Minus,
            contentDescription = stringResource(R.string.remove),
            onClick = { onPriorityChange(-1) },
            enabled = value > 1,
            tint = Color.White,
            modifier = Modifier.size(34.dp),
        )
        Text(
            text = value.toString(),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.width(20.dp),
        )
        SpinIconButton(
            glyph = SpinIconGlyph.Plus,
            contentDescription = stringResource(R.string.add),
            onClick = { onPriorityChange(1) },
            tint = Color.White,
            modifier = Modifier.size(34.dp),
        )
    }
}

@Composable
private fun wheelTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFFEC9213),
    unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
    errorTextColor = Color.White,
    errorBorderColor = Color(0xFFFF7A7A),
    focusedLabelColor = Color(0xFFEC9213),
    unfocusedLabelColor = Color.White.copy(alpha = 0.54f),
    errorLabelColor = Color(0xFFFF7A7A),
    cursorColor = Color(0xFFEC9213),
)

private fun newDraftItem(index: Int): WheelItem =
    WheelItem(
        id = "draft-${System.currentTimeMillis()}-$index",
        name = "",
        priority = 1,
    )
