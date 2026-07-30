package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.audio.rememberGameSoundPlayer
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.WheelItemsEditDialog
import com.vga.spinwheel.ui.components.WheelPickerDialog
import com.vga.spinwheel.ui.components.WheelSelectorChip
import com.vga.spinwheel.ui.components.clickableWithSound
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DrawingSpinScreen(
    wheelId: String,
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onSelectWheel: (String) -> Unit,
    onResult: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    val wheel by viewModel.currentWheel.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val themeIndex by viewModel.themeIndex.collectAsState()
    val winner by viewModel.lastResult.collectAsState()
    val showLastResultOnSpin by viewModel.showLastResultOnSpin.collectAsState()

    val scope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var showTempResult by remember { mutableStateOf(false) }
    var showWheelPicker by remember { mutableStateOf(false) }
    var showEditItems by remember { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }
    val gameSoundPlayer = rememberGameSoundPlayer()
    val availableWheels = remember(wheels, wheel) {
        val current = wheel
        if (current != null && wheels.none { it.id == current.id }) {
            listOf(current) + wheels
        } else {
            wheels
        }
    }

    LaunchedEffect(wheelId) {
        viewModel.loadWheelForDrawing(wheelId)
    }

    LaunchedEffect(showLastResultOnSpin, winner?.id, wheel?.id, wheelId) {
        if (showLastResultOnSpin && winner != null && wheel?.id == wheelId) {
            showTempResult = true
            viewModel.consumeShowLastResultOnSpinRequest()
        }
    }

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            gameSoundPlayer.startCardShuffle()
        } else {
            gameSoundPlayer.stopCardShuffle()
        }
    }

    DisposableEffect(gameSoundPlayer) {
        onDispose { gameSoundPlayer.stopCardShuffle() }
    }

    val winnerIndex = wheel?.items
        ?.indexOfFirst { it.id == winner?.id }
        ?.coerceAtLeast(0)
        ?: 0
    val winnerName = winner?.name?.takeIf { it.isNotBlank() } ?: "-"

    SpinScreen(
        title = stringResource(R.string.drawn),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        actions = {
            SpinIconButton(
                glyph = SpinIconGlyph.Settings,
                contentDescription = stringResource(R.string.customsize),
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
            Spacer(modifier = Modifier.height(16.dp))

            WheelSelectorChip(
                name = wheel?.name.orEmpty(),
                enabled = !isSpinning && wheel != null,
                onClick = { showWheelPicker = true },
                modifier = Modifier
                    .widthIn(min = 120.dp, max = 300.dp),
                backgroundColor = Color(0xFF3B3754),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

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
                        DrawingResultSummary(
                            name = winnerName,
                            position = winnerIndex + 1,
                        )
                    }
                }
            }

            DrawingBottomControls(
                enabled = !isSpinning,
                canStart = !isSpinning && !wheel?.items.isNullOrEmpty(),
                onSettings = onOpenSettings,
                onStart = {
                    showTempResult = false
                    viewModel.clearLastResult()
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
                onReset = {
                    showTempResult = false
                    viewModel.clearLastResult()
                },
                modifier = Modifier.padding(bottom = 70.dp),
            )
        }
    }

    if (showWheelPicker) {
        WheelPickerDialog(
            title = stringResource(R.string.select_wheel),
            wheels = availableWheels,
            selectedWheelId = wheel?.id ?: wheelId,
            canEditSelected = !isSpinning && wheel != null,
            onSelectWheel = { selectedWheelId ->
                showWheelPicker = false
                showTempResult = false
                onSelectWheel(selectedWheelId)
            },
            onEditSelectedItems = {
                showWheelPicker = false
                showEditItems = true
            },
            onDismiss = { showWheelPicker = false },
        )
    }

    val editingWheel = wheel
    if (showEditItems && editingWheel != null) {
        WheelItemsEditDialog(
            title = stringResource(R.string.edit_wheel),
            wheelName = editingWheel.name,
            items = editingWheel.items,
            showPriorityControls = false,
            onSave = { updatedName, updatedItems ->
                showTempResult = false
                viewModel.saveCurrentWheelItems(updatedName, updatedItems) { savedWheelId ->
                    if (savedWheelId != wheelId) {
                        onSelectWheel(savedWheelId)
                    }
                }
                showEditItems = false
            },
            onDismiss = { showEditItems = false },
        )
    }
}

@Composable
internal fun DrawingResultSummary(
    name: String,
    position: Int,
    modifier: Modifier = Modifier,
) {
    val safePosition = position.coerceAtLeast(1)
    Column(
        modifier = modifier.widthIn(min = 180.dp, max = 300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = name.ifBlank { "-" },
                color = Color(0xFF111111),
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "#$safePosition",
            color = Color.White.copy(alpha = 0.78f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
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
    val customizeLabel = stringResource(R.string.customsize)
    val startLabel = stringResource(R.string.playtapto).uppercase()
    val restartLabel = stringResource(R.string.restart)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DrawingToolButton(
            glyph = SpinIconGlyph.Sliders,
            contentDescription = customizeLabel,
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
                .clickableWithSound(enabled = canStart, onClick = onStart),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = startLabel,
                color = Color(0xFF111111),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
            )
        }

        DrawingToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = restartLabel,
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
            .clickableWithSound(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color.White.copy(alpha = if (enabled) 1f else 0.5f),
            modifier = Modifier.size(26.dp),
        )
    }
}
