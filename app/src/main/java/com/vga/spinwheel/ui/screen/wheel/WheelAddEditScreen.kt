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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.data.model.WheelItem
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
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

    val titleText = if (formState.id == null) "Thêm bánh xe" else "Sửa bánh xe"

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
                centerTitle = false,
                actions = {
                    TextButton(
                        onClick = { viewModel.validateAndSave(onSuccess = onSaveSuccess) }
                    ) {
                        Text(
                            text = "Lưu",
                            color = Color(0xFFFFA726),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
            )
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
                bottom = 36.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Name Field Section
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tên",
                            style = MaterialTheme.typography.titleMedium,
                            color = SpinColors.TextMuted,
                        )
                        formState.nameError?.let { err ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = err,
                                color = Color(0xFFFF5252),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = viewModel::updateFormName,
                        placeholder = { Text("Tên bánh xe", color = SpinColors.TextMuted) },
                        isError = formState.nameError != null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SpinRadius.Control)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF3B3754),
                            unfocusedContainerColor = Color(0xFF3B3754),
                            focusedTextColor = SpinColors.TextPrimary,
                            unfocusedTextColor = SpinColors.TextPrimary,
                            focusedBorderColor = Color(0xFFEC9213),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.55f),
                            errorBorderColor = Color(0xFFFF5252),
                        ),
                    )
                }
            }

            // Action Buttons Row (Add Item & Add Many)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = viewModel::addSingleItem,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(SpinRadius.Button),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B3754),
                            contentColor = Color.White,
                        ),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SpinIcon(
                                glyph = SpinIconGlyph.AddCircle,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Thêm mục", fontSize = 15.sp, maxLines = 1)
                        }
                    }

                    Button(
                        onClick = { viewModel.showAddManyModal(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(SpinRadius.Button),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B3754),
                            contentColor = Color.White,
                        ),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SpinIcon(
                                glyph = SpinIconGlyph.Layers,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Thêm nhiều", fontSize = 15.sp, maxLines = 1)
                        }
                    }
                }
            }

            // Items Section Header
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Mục",
                        style = MaterialTheme.typography.titleMedium,
                        color = SpinColors.TextMuted,
                    )
                    formState.itemError?.let { err ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = err,
                            color = Color(0xFFFF5252),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Items Rows
            itemsIndexed(formState.items, key = { _, item -> item.id }) { _, item ->
                WheelItemRow(
                    item = item,
                    hasError = formState.itemError != null && item.name.trim().isEmpty(),
                    onNameChange = { newName -> viewModel.updateItemName(item.id, newName) },
                    onPriorityChange = { delta -> viewModel.changeItemPriority(item.id, delta) },
                )
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
    hasError: Boolean,
    onNameChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Option Name Input Box
        OutlinedTextField(
            value = item.name,
            onValueChange = onNameChange,
            placeholder = { Text("Tên mục", color = SpinColors.TextMuted) },
            singleLine = true,
            isError = hasError,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(SpinRadius.Control)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3B3754),
                unfocusedContainerColor = Color(0xFF3B3754),
                focusedTextColor = SpinColors.TextPrimary,
                unfocusedTextColor = SpinColors.TextPrimary,
                focusedBorderColor = Color(0xFFEC9213),
                unfocusedBorderColor = Color.White.copy(alpha = 0.55f),
                errorBorderColor = Color(0xFFFF5252),
            ),
        )

        // Priority Box (Square with 'Ưu tiên' label & +/- round buttons)
        Box(
            modifier = Modifier
                .width(76.dp)
                .clip(RoundedCornerShape(SpinRadius.Control))
                .background(Color(0xFF3B3754))
                .border(1.5.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(SpinRadius.Control))
                .padding(horizontal = 8.dp, vertical = 6.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Ưu tiên",
                    fontSize = 10.sp,
                    color = SpinColors.TextMuted,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Round Minus Button (Red)
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE60000))
                            .clickable { onPriorityChange(-1) },
                        contentAlignment = Alignment.Center,
                    ) {
                        SpinIcon(
                            glyph = SpinIconGlyph.Minus,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp),
                        )
                    }

                    Text(
                        text = item.priority.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )

                    // Round Plus Button (Blue)
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0033CC))
                            .clickable { onPriorityChange(1) },
                        contentAlignment = Alignment.Center,
                    ) {
                        SpinIcon(
                            glyph = SpinIconGlyph.Plus,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                }
            }
        }
    }
}
