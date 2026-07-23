package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelPaletteScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentPaletteIdx by viewModel.paletteIndex.collectAsState()
    var selectedIdx by remember { mutableStateOf(currentPaletteIdx) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Bảng màu",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
                centerTitle = false,
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.updatePalette(selectedIdx)
                            onBack()
                        }
                    ) {
                        Text(
                            text = "Lưu",
                            color = Color(0xFFFFA726),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            itemsIndexed(WheelPalettes.all) { index, palette ->
                PaletteCircleItem(
                    palette = palette,
                    isSelected = index == selectedIdx,
                    onClick = { selectedIdx = index },
                )
            }
        }
    }
}

@Composable
private fun PaletteCircleItem(
    palette: WheelPalette,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .then(
                    if (isSelected) Modifier.border(4.dp, Color(0xFFFFA726), CircleShape)
                    else Modifier.border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                )
                .padding(6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.width.coerceAtMost(size.height) / 2f
                val sectorAngle = 360f / palette.colors.size

                for (i in palette.colors.indices) {
                    val startAngle = i * sectorAngle - 90f
                    drawArc(
                        color = palette.colors[i],
                        startAngle = startAngle,
                        sweepAngle = sectorAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                    )
                }

                // Inner white border circle
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx()),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = palette.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}
