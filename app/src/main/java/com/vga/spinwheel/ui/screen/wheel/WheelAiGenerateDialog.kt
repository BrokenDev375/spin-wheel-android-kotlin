package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius

@Composable
fun WheelAiGenerateDialog(
    topics: List<AiTopic>,
    onSelectTopic: (AiTopic) -> Unit,
    onCustomPrompt: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var prompt by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Trình Tạo AI Bánh Xe",
                style = MaterialTheme.typography.titleLarge,
                color = SpinColors.TextPrimary,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Chọn chủ đề mẫu hoặc nhập chủ đề của bạn:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SpinColors.TextMuted,
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(topics) { topic ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(SpinRadius.Control))
                                .background(Color.White.copy(alpha = 0.08f))
                                .clickable { onSelectTopic(topic) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = SpinColors.TextPrimary,
                                )
                                Text(
                                    text = "${topic.options.size} mục",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SpinColors.TextMuted,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Ví dụ: Món ăn tối", color = SpinColors.TextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SpinColors.TextPrimary,
                        unfocusedTextColor = SpinColors.TextPrimary,
                        focusedBorderColor = SpinColors.Action,
                        unfocusedBorderColor = SpinColors.CardBorder,
                    ),
                )

                if (prompt.isNotBlank()) {
                    SpinPrimaryButton(
                        text = "Tạo Bánh Xe Với AI",
                        onClick = { onCustomPrompt(prompt) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = SpinColors.TextMuted)
            }
        },
        containerColor = SpinColors.Card,
    )
}
