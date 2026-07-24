package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingPaletteScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tempThemeIndex by viewModel.tempThemeIndex.collectAsState()

    SpinScreen(
        title = stringResource(R.string.drawn),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        actions = {
            TextButton(onClick = onSave) {
                Text(
                    text = stringResource(R.string.save),
                    color = SpinColors.Action,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                )
            }
        },
        modifier = modifier,
    ) { contentModifier ->
        LazyColumn(
            modifier = contentModifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 32.dp,
                bottom = 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            itemsIndexed(DrawingThemes) { index, theme ->
                DrawingThemeOption(
                    theme = theme,
                    selected = tempThemeIndex.mod(DrawingThemes.size) == index,
                    onClick = { viewModel.setTempThemeIndex(index) },
                )
            }
        }
    }
}

@Composable
private fun DrawingThemeOption(
    theme: DrawingTheme,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .clip(shape)
            .background(Color(0xFF393347))
            .border(
                width = 1.5.dp,
                color = if (selected) SpinColors.Action else Color.White.copy(alpha = 0.06f),
                shape = shape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DrawingThemeSwatch(color = theme.colors[0])
        Spacer(modifier = Modifier.width(24.dp))
        DrawingThemeSwatch(color = theme.colors[1])
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = theme.name,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

internal fun getThemeColor(index: Int): Color = drawingTheme(index).colors.first()
