package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.ui.components.SpinBottomActionBar
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.components.SpinSecondaryButton
import com.vga.spinwheel.ui.components.SpinStepper
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelAddEditScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formState by viewModel.formState.collectAsState()
    val showAddManyModal by viewModel.showAddManyModal.collectAsState()

    val titleText = if (formState.id == null) "Thêm Bánh Xe" else "Sửa Bánh Xe"

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = titleText,
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
            )
        },
        bottomBar = {
            SpinBottomActionBar {
                SpinPrimaryButton(
                    text = "Lưu Bánh Xe",
                    onClick = {
                        viewModel.validateAndSave(onSuccess = onSaveSuccess)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = SpinSpacing.ScreenHorizontal,
                end = SpinSpacing.ScreenHorizontal,
                top = 12.dp,
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Name Field
            item {
                Column {
                    Text(
                        text = "Tên bánh xe",
                        style = MaterialTheme.typography.titleMedium,
                        color = SpinColors.TextPrimary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = viewModel::updateFormName,
                        placeholder = { Text("Nhập tên bánh xe...", color = SpinColors.TextMuted) },
                        isError = formState.nameError != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SpinColors.TextPrimary,
                            unfocusedTextColor = SpinColors.TextPrimary,
                            focusedBorderColor = SpinColors.Action,
                            unfocusedBorderColor = SpinColors.CardBorder,
                            errorBorderColor = Color(0xFFFF5252),
                        ),
                    )
                    formState.nameError?.let { err ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = err, color = Color(0xFFFF5252), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Items Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Danh sách tùy chọn",
                        style = MaterialTheme.typography.titleMedium,
                        color = SpinColors.TextPrimary,
                    )
                    Text(
                        text = "Ưu tiên",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SpinColors.TextMuted,
                    )
                }
                formState.itemError?.let { err ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = err, color = Color(0xFFFF5252), style = MaterialTheme.typography.bodySmall)
                }
            }

            // Items Rows
            itemsIndexed(formState.items, key = { _, item -> item.id }) { _, item ->
                WheelItemRow(
                    item = item,
                    onNameChange = { newName -> viewModel.updateItemName(item.id, newName) },
                    onPriorityChange = { delta -> viewModel.changeItemPriority(item.id, delta) },
                    onRemove = { viewModel.removeItem(item.id) },
                )
            }

            // Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    SpinSecondaryButton(
                        text = "+ Thêm mục",
                        onClick = viewModel::addSingleItem,
                        modifier = Modifier.weight(1f),
                    )
                    SpinSecondaryButton(
                        text = "Thêm nhiều",
                        onClick = { viewModel.showAddManyModal(true) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }

    if (showAddManyModal) {
        WheelAddManyModal(
            onAdd = viewModel::addManyItems,
            onDismiss = { viewModel.showAddManyModal(false) },
        )
    }
}

@Composable
private fun WheelItemRow(
    item: WheelItem,
    onNameChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SpinRadius.Control))
            .background(SpinColors.Card)
            .border(1.dp, SpinColors.CardBorder, RoundedCornerShape(SpinRadius.Control))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SpinIconButton(
            glyph = SpinIconGlyph.Trash,
            contentDescription = "Xóa tùy chọn",
            onClick = onRemove,
            tint = Color(0xFFFF5252),
            modifier = Modifier.size(36.dp),
        )

        OutlinedTextField(
            value = item.name,
            onValueChange = onNameChange,
            placeholder = { Text("Tên tùy chọn", color = SpinColors.TextMuted) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = SpinColors.TextPrimary,
                unfocusedTextColor = SpinColors.TextPrimary,
                focusedBorderColor = SpinColors.Action,
                unfocusedBorderColor = Color.Transparent,
            ),
        )

        SpinStepper(
            value = item.priority.toString(),
            onMinus = { onPriorityChange(-1) },
            onPlus = { onPriorityChange(1) },
        )
    }
}
