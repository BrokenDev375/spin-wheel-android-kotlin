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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF201B2D))
    ) {
        SpinTopBar(
            title = "Xúc Xắc",
            navigationIcon = SpinIconGlyph.Back,
            onNavigationClick = onBack,
            actions = {
                if (!uiState.isRolling) {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Settings,
                        contentDescription = "Cài đặt",
                        onClick = onOpenSettings
                    )
                }
            }
        )

        // Dice count selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "DiceCount:",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in 1..6) {
                    val isSelected = uiState.diceCount == i
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFFE4F0FF) else Color.Transparent)
                            .border(
                                1.dp,
                                if (isSelected) Color.Transparent else Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                            .clickable(enabled = !uiState.isRolling) {
                                viewModel.setDiceCount(i)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = i.toString(),
                            color = if (isSelected) Color(0xFF2E63D8) else Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Dice stage
        val displayResults = if (uiState.currentResults.isEmpty()) {
            List(uiState.diceCount) { 1 } // default face 1
        } else {
            uiState.currentResults
        }
        val total = displayResults.sum()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Total chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (uiState.currentResults.isNotEmpty() && !uiState.isRolling) Color(0xFFF12966) else Color.White.copy(alpha = 0.1f))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (uiState.currentResults.isEmpty() || uiState.isRolling) "--" else total.toString(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Grid of dice
                val columns = when (uiState.diceCount) {
                    1 -> 1
                    2 -> 2
                    3 -> 2
                    4 -> 2
                    else -> 3
                }
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(displayResults) { value ->
                        DiceTile(
                            styleIndex = uiState.styleIndex,
                            modifier = Modifier.size(100.dp)
                        ) {
                            DiceFace(
                                value = value,
                                styleIndex = uiState.styleIndex,
                                isShaking = uiState.isRolling
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C263A))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF393347))
                    .clickable(enabled = !uiState.isRolling) { onOpenSettings() },
                contentAlignment = Alignment.Center
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.Settings,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF769EFD))
                    .clickable(enabled = !uiState.isRolling) {
                        viewModel.startRoll {}
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CHƠI",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF393347))
                    .clickable(enabled = !uiState.isRolling) { viewModel.resetResults() },
                contentAlignment = Alignment.Center
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.Reset,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
