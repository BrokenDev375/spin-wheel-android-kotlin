package com.vga.spinwheel.ui.screen.dice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen

@Composable
fun DiceLabelScreen(
    viewModel: DiceViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    SpinScreen(
        title = stringResource(R.string.Templatedice),
        navigationIcon = SpinIconGlyph.Back,
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        navigationTint = Color.White,
        actions = {
            Text(
                text = stringResource(R.string.save),
                color = DiceLabelAccent,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .clickable {
                        viewModel.saveStyleIndex()
                        onBack()
                    }
                    .padding(horizontal = 8.dp),
            )
        },
    ) { contentModifier ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 34.dp,
                end = 12.dp,
                bottom = 24.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = contentModifier.fillMaxWidth(),
        ) {
            items(diceStyles.size) { index ->
                val isSelected = uiState.tempStyleIndex == index

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.02f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 2.dp,
                            color = if (isSelected) DiceLabelAccent else Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable { viewModel.setTempStyleIndex(index) }
                        .padding(2.dp),
                ) {
                    DiceTile(
                        styleIndex = index,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1.02f),
                    ) {
                        DiceFace(
                            value = 1,
                            styleIndex = index,
                            dotSize = 22.dp,
                            contentPadding = 22.dp,
                            modifier = Modifier.size(112.dp),
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(9.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF35F282)),
                            contentAlignment = Alignment.Center,
                        ) {
                            SpinIcon(
                                glyph = SpinIconGlyph.Check,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

private val DiceLabelAccent = Color(0xFFEC9213)
