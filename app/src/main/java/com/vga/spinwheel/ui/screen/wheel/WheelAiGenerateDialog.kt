package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun WheelAiGenerateDialog(
    topics: List<AiTopic>,
    onSelectTopic: (AiTopic) -> Unit,
    onCustomPrompt: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var prompt by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = SpinColors.Background),
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
            ) {
                SpinTopBar(
                    title = "Trình tạo AI",
                    navigationIcon = SpinIconGlyph.Back,
                    navigationDescription = "Quay lại",
                    onNavigationClick = onDismiss,
                    centerTitle = false,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B3754)),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                        ) {
                            Text(
                                text = "Enter a topic for your wheel:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = prompt,
                                onValueChange = { prompt = it },
                                placeholder = {
                                    Text(
                                        "e.g. Dinner decisions, Truth or dare",
                                        color = SpinColors.TextMuted,
                                        fontSize = 15.sp,
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                                ),
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Remaining quota f111878b: 4/4",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpinColors.TextMuted,
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (prompt.isNotBlank()) {
                                onCustomPrompt(prompt)
                            } else {
                                topics.firstOrNull()?.let(onSelectTopic)
                            }
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC9213),
                            contentColor = Color.White,
                        ),
                    ) {
                        Text(
                            text = "Generate options",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
