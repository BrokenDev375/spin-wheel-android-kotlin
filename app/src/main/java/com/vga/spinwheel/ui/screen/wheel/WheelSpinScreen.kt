package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.audio.rememberGameSoundPlayer
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.components.WheelItemsEditDialog
import com.vga.spinwheel.ui.components.WheelPickerDialog
import com.vga.spinwheel.ui.components.WheelSelectorChip
import com.vga.spinwheel.ui.theme.SpinColors

import com.vga.spinwheel.ui.components.SpinScreen

@Composable
fun WheelSpinScreen(
    wheelId: String,
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHistory: () -> Unit,
    onSelectWheel: (String) -> Unit,
    onResult: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    val currentWheel by viewModel.currentWheel.collectAsState()
    val activeItems by viewModel.activeItems.collectAsState()
    val spinStatus by viewModel.spinStatus.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val paletteIdx by viewModel.paletteIndex.collectAsState()
    val gameSoundPlayer = rememberGameSoundPlayer()
    var showWheelPicker by remember { mutableStateOf(false) }
    var showEditItems by remember { mutableStateOf(false) }

    val palette = WheelPalettes.getPalette(paletteIdx)
    val controlsEnabled = spinStatus !is SpinStatus.Spinning

    LaunchedEffect(wheelId) {
        viewModel.loadWheelForSpin(wheelId)
    }

    LaunchedEffect(spinStatus) {
        if (spinStatus is SpinStatus.Spinning) {
            gameSoundPlayer.startWheelSpin()
        } else {
            gameSoundPlayer.stopWheelSpin()
        }
    }

    DisposableEffect(gameSoundPlayer) {
        onDispose {
            gameSoundPlayer.stopWheelSpin()
        }
    }

    SpinScreen(
        title = stringResource(R.string.spinwheel),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        confirmExitOnBack = true,
        actions = {
            SpinIconButton(
                glyph = SpinIconGlyph.History,
                contentDescription = stringResource(R.string.history),
                onClick = onOpenHistory,
                tint = Color.White,
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))

                // Wheel Name Pill Tag
                WheelSelectorChip(
                    name = currentWheel?.name ?: stringResource(R.string.spinwheel),
                    enabled = controlsEnabled && currentWheel != null,
                    onClick = { showWheelPicker = true },
                    modifier = Modifier
                        .widthIn(min = 120.dp, max = 300.dp),
                    backgroundColor = Color(0xFF3B3754),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Result Text
                val resultText = when (val status = spinStatus) {
                    is SpinStatus.Finished -> status.winner.name
                    is SpinStatus.Spinning -> stringResource(R.string.spinning)
                    SpinStatus.Idle -> "???"
                }

                Text(
                    text = resultText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Wheel Canvas Component
            WheelCanvas(
                items = activeItems,
                palette = palette,
                spinStatus = spinStatus,
                durationSeconds = duration,
                onSpinFinished = { winner ->
                    gameSoundPlayer.stopWheelSpin()
                    viewModel.onSpinCompleted(winner)
                },
                onClickSpin = {
                    if (spinStatus !is SpinStatus.Spinning && activeItems.size >= 2) {
                        viewModel.startSpin()
                    }
                },
                modifier = Modifier.size(304.dp),
            )

            // When spin is finished, navigate to result screen
            if (spinStatus is SpinStatus.Finished) {
                val finishedStatus = spinStatus as SpinStatus.Finished
                LaunchedEffect(finishedStatus.resultId) {
                    onResult(wheelId, finishedStatus.resultId)
                }
            }

            // Bottom Control Action Row (Sliders, Spin, Shuffle, Reset)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Settings / Sliders Icon Button
                Button(
                    onClick = {
                        gameSoundPlayer.playButtonClick()
                        onOpenSettings()
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B3754)),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    SpinIcon(glyph = SpinIconGlyph.Sliders, tint = Color.White, modifier = Modifier.size(18.dp))
                }

                // Primary Spin Button
                Button(
                    onClick = viewModel::startSpin,
                    enabled = spinStatus !is SpinStatus.Spinning && activeItems.size >= 2,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B3754),
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = if (spinStatus is SpinStatus.Spinning) {
                            stringResource(R.string.spinning).uppercase()
                        } else {
                            stringResource(R.string.taptospin).uppercase()
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                }

                // Shuffle Button
                Button(
                    onClick = {
                        gameSoundPlayer.playButtonClick()
                        viewModel.shuffleActiveItems()
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B3754)),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    SpinIcon(glyph = SpinIconGlyph.Shuffle, tint = Color.White, modifier = Modifier.size(18.dp))
                }

                // Reset Button
                Button(
                    onClick = {
                        gameSoundPlayer.playButtonClick()
                        viewModel.resetSpin()
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B3754)),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    SpinIcon(glyph = SpinIconGlyph.Reset, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    if (showWheelPicker) {
        WheelPickerDialog(
            title = stringResource(R.string.select_wheel),
            wheels = wheels,
            selectedWheelId = currentWheel?.id ?: wheelId,
            canEditSelected = controlsEnabled && currentWheel != null,
            onSelectWheel = { selectedWheelId ->
                showWheelPicker = false
                onSelectWheel(selectedWheelId)
            },
            onEditSelectedItems = {
                showWheelPicker = false
                showEditItems = true
            },
            onDismiss = { showWheelPicker = false },
        )
    }

    val editingWheel = currentWheel
    if (showEditItems && editingWheel != null) {
        WheelItemsEditDialog(
            title = stringResource(R.string.edit_wheel),
            wheelName = editingWheel.name,
            items = editingWheel.items,
            showPriorityControls = true,
            onSave = { updatedName, updatedItems ->
                viewModel.saveCurrentWheelItems(updatedName, updatedItems)
                showEditItems = false
            },
            onDismiss = { showEditItems = false },
        )
    }
}
