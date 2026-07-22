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
fun DrawingAddEditScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formState by viewModel.formState.collectAsState()

    val titleText = if (formState.id.isEmpty()) "Thêm danh sách" else "Sửa danh sách"

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
                actions = {
                    TextButton(
                        onClick = { viewModel.saveWheel(onSuccess = onSaveSuccess, onError = {}) }
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
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tên",
                            style = MaterialTheme.typography.titleMedium,
                            color = SpinColors.TextMuted,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = viewModel::updateFormName,
                        placeholder = { Text("Tên danh sách", color = SpinColors.TextMuted) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SpinRadius.Control)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF3B3754),
                            unfocusedContainerColor = Color(0xFF3B3754),
                            focusedTextColor = SpinColors.TextPrimary,
                            unfocusedTextColor = SpinColors.TextPrimary,
                            focusedBorderColor = Color.White.copy(alpha = 0.3f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            errorBorderColor = Color(0xFFFF5252),
                        ),
                    )
                }
            }

            item {
                Button(
                    onClick = viewModel::addItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
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
                        Text("Thêm thẻ", fontSize = 15.sp)
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Các thẻ",
                        style = MaterialTheme.typography.titleMedium,
                        color = SpinColors.TextMuted,
                    )
                }
            }

            itemsIndexed(formState.items, key = { _, item -> item.id }) { _, item ->
                DrawingItemRow(
                    item = item,
                    onNameChange = { newName -> viewModel.updateItemName(item.id, newName) },
                    onDelete = { viewModel.removeItem(item.id) },
                )
            }
        }
    }
}

@Composable
private fun DrawingItemRow(
    item: WheelItem,
    onNameChange: (String) -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = item.name,
            onValueChange = onNameChange,
            placeholder = { Text("Tên thẻ", color = SpinColors.TextMuted) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(SpinRadius.Control)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3B3754),
                unfocusedContainerColor = Color(0xFF3B3754),
                focusedTextColor = SpinColors.TextPrimary,
                unfocusedTextColor = SpinColors.TextPrimary,
                focusedBorderColor = Color.White.copy(alpha = 0.3f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
            ),
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(SpinRadius.Control))
                .background(Color(0xFF3B3754))
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.Close,
                tint = Color(0xFFFF5252),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
