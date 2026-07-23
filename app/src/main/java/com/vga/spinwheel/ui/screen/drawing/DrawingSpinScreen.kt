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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.vga.spinwheel.ui.components.SpinScreen
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

    SpinScreen(
        title = wheel?.name ?: "Vẽ",
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        confirmExitOnBack = true,
        actions = {
            SpinIconButton(
                glyph = SpinIconGlyph.Settings,
                contentDescription = "Cài đặt",
                onClick = onOpenSettings,
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
                        val rotation = if (i == 0) 0f else (if (i % 2 == 1) 3f else -3f)

                        Box(
                            modifier = Modifier
                                .offset(y = yOffset, x = if (i == 0) shakeOffset.value.dp else 0.dp)
                                .scale(scaleFactor)
                                .rotate(rotation)
                                .size(width = 240.dp, height = 340.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (i == 0) cardColor else cardColor.copy(alpha = 0.8f))
                                .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (i == 0 && showTempResult) {
                                val winnerText = viewModel.lastResult.collectAsState().value?.name ?: ""
                                Text(
                                    text = winnerText,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom bar controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Settings button
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

                // Main action button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (!isSpinning && !showTempResult) Color(0xFFEC9213) else Color(0xFFEC9213).copy(alpha = 0.5f))
                        .clickable(enabled = !isSpinning && !showTempResult) {
                            isSpinning = true
                            scope.launch {
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
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF111111)
                    )
                }

                // Reset button
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
    }
}
