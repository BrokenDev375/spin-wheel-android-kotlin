package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
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
    val winner by viewModel.lastResult.collectAsState()

    val scope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var showTempResult by remember { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(wheelId) {
        viewModel.loadWheelForDrawing(wheelId)
    }

    val winnerIndex = wheel?.items
        ?.indexOfFirst { it.id == winner?.id }
        ?.coerceAtLeast(0)
        ?: 0

    SpinScreen(
        title = "Vẽ",
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        actions = {
            SpinIconButton(
                glyph = SpinIconGlyph.Settings,
                contentDescription = "Tùy chỉnh",
                onClick = onOpenSettings,
                tint = Color.White,
            )
        },
        modifier = modifier,
    ) { contentModifier ->
        Column(
            modifier = contentModifier.padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(34.dp))

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .widthIn(min = 210.dp, max = 330.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF393347))
                    .padding(horizontal = 28.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = wheel?.name.orEmpty(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.offset(y = (-56).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val currentItems = wheel?.items.orEmpty()
                    DrawingCardStack(
                        items = currentItems,
                        winnerIndex = if (showTempResult) winnerIndex else 0,
                        themeIndex = themeIndex,
                        shakeOffset = if (isSpinning) shakeOffset.value else 0f,
                        wheelTitle = wheel?.name.orEmpty(),
                    )

                    if (showTempResult && currentItems.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(26.dp))
                        DrawingResultNumber(value = winnerIndex + 1)
                    }
                }
            }

            DrawingBottomControls(
                enabled = !isSpinning,
                canStart = !isSpinning && !showTempResult && !wheel?.items.isNullOrEmpty(),
                onSettings = onOpenSettings,
                onStart = {
                    isSpinning = true
                    scope.launch {
                        val endTime = System.currentTimeMillis() + duration * 1_000L
                        while (System.currentTimeMillis() < endTime) {
                            shakeOffset.animateTo(
                                targetValue = 12f,
                                animationSpec = tween(120, easing = FastOutSlowInEasing),
                            )
                            shakeOffset.animateTo(
                                targetValue = -12f,
                                animationSpec = tween(120, easing = FastOutSlowInEasing),
                            )
                        }
                        shakeOffset.snapTo(0f)
                        viewModel.drawItem()
                        isSpinning = false
                        showTempResult = true
                        delay(1_000L)
                        onResult(wheelId)
                    }
                },
                onReset = { showTempResult = false },
                modifier = Modifier.padding(bottom = 70.dp),
            )
        }
    }
}

@Composable
internal fun DrawingResultNumber(
    value: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            color = Color(0xFF111111),
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun DrawingBottomControls(
    enabled: Boolean,
    canStart: Boolean,
    onSettings: () -> Unit,
    onStart: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DrawingToolButton(
            glyph = SpinIconGlyph.Sliders,
            contentDescription = "Tùy chỉnh",
            enabled = enabled,
            onClick = onSettings,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (canStart) Color(0xFFEC9213) else Color(0xFFEC9213).copy(alpha = 0.5f),
                )
                .clickable(enabled = canStart, onClick = onStart),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Nhấn để ghép nối",
                color = Color(0xFF111111),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
            )
        }

        DrawingToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = "Quay lại từ đầu",
            enabled = enabled,
            onClick = onReset,
        )
    }
}

@Composable
private fun DrawingToolButton(
    glyph: SpinIconGlyph,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color.White.copy(alpha = if (enabled) 1f else 0.5f),
            modifier = Modifier.size(26.dp),
        )
    }
}
