package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun WheelAddManyModal(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var textInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Thêm Nhiều Tùy Chọn",
                style = MaterialTheme.typography.titleLarge,
                color = SpinColors.TextPrimary,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nhập mỗi tùy chọn trên một dòng riêng biệt:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SpinColors.TextMuted,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text(
                            text = "Tùy chọn 1\nTùy chọn 2\nTùy chọn 3",
                            color = SpinColors.TextMuted,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SpinColors.TextPrimary,
                        unfocusedTextColor = SpinColors.TextPrimary,
                        focusedBorderColor = SpinColors.Action,
                        unfocusedBorderColor = SpinColors.CardBorder,
                    ),
                )
            }
        },
        confirmButton = {
            SpinPrimaryButton(
                text = "Xong",
                onClick = {
                    onAdd(textInput)
                    onDismiss()
                },
                enabled = textInput.isNotBlank(),
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = SpinColors.TextMuted)
            }
        },
        containerColor = SpinColors.Card,
    )
}
