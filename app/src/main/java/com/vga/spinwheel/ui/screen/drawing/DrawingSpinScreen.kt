package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DrawingSpinScreen(
    wheelId: String,
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onResult: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheel by viewModel.currentWheel.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val themeIndex by viewModel.themeIndex.collectAsState()
    
    val scope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var showTempResult by remember { mutableStateOf(false) }

    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(wheelId) {
        viewModel.loadWheelForDrawing(wheelId)
    }

    val cardColor = getThemeColor(themeIndex)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = wheel?.name ?: "Vẽ",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Settings,
                        contentDescription = "Cài đặt",
                        onClick = onOpenSettings,
                    )
                },
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tùy chỉnh (Settings) button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickable { onOpenSettings() },
                    contentAlignment = Alignment.Center
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Settings,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Nhấn để ghép nối (Main action) button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (!isSpinning && !showTempResult) Color(0xFFEC9213) else Color(0xFFEC9213).copy(alpha = 0.5f))
                        .clickable(enabled = !isSpinning && !showTempResult) {
                            isSpinning = true
                            scope.launch {
                                // Horizontal alternating shake
                                val shakeDuration = duration * 1000L
                                val endTime = System.currentTimeMillis() + shakeDuration

                                while (System.currentTimeMillis() < endTime) {
                                    shakeOffset.animateTo(
                                        targetValue = 25f,
                                        animationSpec = tween(50, easing = FastOutLinearInEasing)
                                    )
                                    shakeOffset.animateTo(
                                        targetValue = -25f,
                                        animationSpec = tween(50, easing = FastOutLinearInEasing)
                                    )
                                }
                                shakeOffset.animateTo(0f)

                                viewModel.drawItem()
                                isSpinning = false
                                showTempResult = true

                                delay(1000)
                                onResult(wheelId)
                                showTempResult = false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nhấn để ghép nối",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold, // 900
                        color = Color(0xFF111111)
                    )
                }

                // Quay lại từ đầu (Reset) button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickable(enabled = !isSpinning) {
                            showTempResult = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Reset,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            val maxCards = (wheel?.items?.size ?: 0).coerceAtMost(3)
            val topIndex = (maxCards - 1).coerceAtLeast(0)
            
            Box(
                contentAlignment = Alignment.Center
            ) {
                for (i in topIndex downTo 0) {
                    val scaleFactor = 1f - (i * 0.05f)
                    val yOffset = (i * 12).dp
                    // Alternate shake direction based on index
                    val currentShake = if (i % 2 == 0) shakeOffset.value else -shakeOffset.value
                    
                    Box(
                        modifier = Modifier
                            .offset(x = currentShake.dp, y = -yOffset)
                            .scale(scaleFactor)
                            .size(width = 220.dp, height = 300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (i == 0) cardColor else cardColor.copy(alpha = 0.5f))
                            .border(2.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (i == 0) {
                            SpinIcon(
                                glyph = SpinIconGlyph.Sparkles,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
            
            if (showTempResult) {
                Text(
                    text = "Đang mở thẻ...",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = 200.dp)
                )
            }
        }
    }
}

fun getThemeColor(index: Int): Color {
    return when(index) {
        0 -> Color(0xFFEF6C00) // Orange
        1 -> Color(0xFF1E88E5) // Blue
        2 -> Color(0xFF43A047) // Green
        3 -> Color(0xFFE53935) // Red
        4 -> Color(0xFF8E24AA) // Purple
        else -> Color(0xFFEF6C00)
    }
}
