package com.vga.spinwheel.ui.screen.dice

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen

@Composable
fun DiceHomeScreen(
    viewModel: DiceViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onPreview: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isRolling, uiState.currentResults) {
        if (!uiState.isRolling && uiState.currentResults.isNotEmpty()) {
            onPreview()
        }
    }

    SpinScreen(
        title = "Xúc Xắc",
        navigationIcon = SpinIconGlyph.Back,
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        navigationTint = Color.White,
        confirmExitOnBack = true,
        actions = {
            if (!uiState.isRolling) {
                SpinIconButton(
                    glyph = SpinIconGlyph.Settings,
                    contentDescription = "Cài đặt",
                    onClick = onOpenSettings,
                    tint = Color.White,
                )
            }
        },
    ) { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            DiceCountSelector(
                selectedCount = uiState.diceCount,
                enabled = !uiState.isRolling,
                onSelect = viewModel::setDiceCount,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                DiceStage(
                    uiState = uiState,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            DiceBottomBar(
                isRolling = uiState.isRolling,
                onSettings = onOpenSettings,
                onRoll = { viewModel.startRoll {} },
                onReset = viewModel::resetResults,
                modifier = Modifier.padding(bottom = 58.dp),
            )
        }
    }
}

@Composable
private fun DiceCountSelector(
    selectedCount: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DicePanelColor)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Dice Count:",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            for (count in 1..6) {
                val selected = selectedCount == count
                Box(
                    modifier = Modifier
                        .size(width = 28.dp, height = 30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) DiceAccentColor else Color.Transparent)
                        .border(
                            width = if (selected) 0.dp else 1.2.dp,
                            color = DiceStrokeColor,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .clickable(enabled = enabled) { onSelect(count) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = count.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
        }
    }
}

@Composable
private fun DiceStage(
    uiState: DiceUiState,
    modifier: Modifier = Modifier,
) {
    val displayResults = if (uiState.currentResults.isEmpty()) {
        List(uiState.diceCount) { 1 }
    } else {
        uiState.currentResults
    }
    val total = if (uiState.currentResults.isEmpty()) 0 else displayResults.sum()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        DiceTotalChip(total = total)
        DiceGrid(
            values = displayResults,
            styleIndex = uiState.styleIndex,
            isShaking = uiState.isRolling,
        )
    }
}

@Composable
private fun DiceTotalChip(total: Int) {
    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = total.toString(),
            color = DiceAccentColor,
            fontSize = 27.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun DiceBottomBar(
    isRolling: Boolean,
    onSettings: () -> Unit,
    onRoll: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DiceToolButton(
            glyph = SpinIconGlyph.Sliders,
            enabled = !isRolling,
            onClick = onSettings,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DicePanelColor)
                .clickable(enabled = !isRolling, onClick = onRoll),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "CHƠI",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
            )
        }

        DiceToolButton(
            glyph = SpinIconGlyph.Reset,
            enabled = !isRolling,
            onClick = onReset,
        )
    }
}

@Composable
private fun DiceToolButton(
    glyph: SpinIconGlyph,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DicePanelColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color.White,
            modifier = Modifier.size(28.dp),
        )
    }
}

private val DicePanelColor = Color(0xFF393347)
private val DiceAccentColor = Color(0xFFEC9213)
private val DiceStrokeColor = Color(0xFF8C8893)
