package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingPaletteScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tempThemeIndex by viewModel.tempThemeIndex.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Chọn màu thẻ",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(5) { index ->
                val color = getThemeColor(index)
                val isSelected = tempThemeIndex == index

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            viewModel.setTempThemeIndex(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        SpinIcon(
                            glyph = SpinIconGlyph.Check,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

fun getThemeColor(index: Int): Color {
    return when (index % 5) {
        0 -> Color(0xFFEC9213)
        1 -> Color(0xFFE91E63)
        2 -> Color(0xFF9C27B0)
        3 -> Color(0xFF2196F3)
        else -> Color(0xFF4CAF50)
    }
}
