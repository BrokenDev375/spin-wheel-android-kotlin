package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.data.model.WheelItem

internal data class DrawingTheme(
    val name: String,
    val colors: List<Color>,
)

internal val DrawingThemes = listOf(
    DrawingTheme(
        name = "Default",
        colors = listOf(
            Color(0xFFBC705D),
            Color(0xFF609A85),
            Color(0xFF5A8E95),
            Color(0xFF93A07D),
            Color(0xFFC0AD75),
        ),
    ),
    DrawingTheme(
        name = "Pink",
        colors = listOf(
            Color(0xFFD51673),
            Color(0xFFB91C6B),
            Color(0xFFEF4F99),
            Color(0xFFC72D7F),
            Color(0xFFA71462),
        ),
    ),
    DrawingTheme(
        name = "Green",
        colors = listOf(
            Color(0xFF578A99),
            Color(0xFF5D9AA0),
            Color(0xFF4F8290),
            Color(0xFF6BA1A6),
            Color(0xFF467684),
        ),
    ),
    DrawingTheme(
        name = "Brown",
        colors = listOf(
            Color(0xFF31560F),
            Color(0xFF3F6819),
            Color(0xFF526F28),
            Color(0xFF27470B),
            Color(0xFF496923),
        ),
    ),
)

internal fun drawingTheme(index: Int): DrawingTheme =
    DrawingThemes[index.mod(DrawingThemes.size)]

@Composable
internal fun DrawingCardStack(
    items: List<WheelItem>,
    winnerIndex: Int,
    themeIndex: Int,
    modifier: Modifier = Modifier,
    shakeOffset: Float = 0f,
    emphasizeWinner: Boolean = true,
) {
    if (items.isEmpty()) return

    val safeWinnerIndex = winnerIndex.coerceIn(0, items.lastIndex)
    val theme = drawingTheme(themeIndex)
    val backIndexes = items.indices
        .filter { it != safeWinnerIndex }
        .take(4)
        .toMutableList()

    while (backIndexes.size < minOf(4, items.size)) {
        backIndexes += backIndexes.size % items.size
    }

    val offsets = listOf(
        -34.dp to 6.dp,
        -16.dp to 22.dp,
        12.dp to 38.dp,
        40.dp to 54.dp,
    )

    Box(modifier = modifier.size(width = 336.dp, height = 250.dp)) {
        backIndexes.take(4).forEachIndexed { order, itemIndex ->
            val (x, y) = offsets[order]
            DrawingStackCard(
                item = items[itemIndex],
                index = itemIndex,
                color = theme.colors[theme.colors.lastIndex - order],
                modifier = Modifier.offset(
                    x = x + if (order % 2 == 0) shakeOffset.dp else (-shakeOffset).dp,
                    y = y,
                ),
                highlighted = false,
            )
        }

        DrawingStackCard(
            item = items[safeWinnerIndex],
            index = safeWinnerIndex,
            color = theme.colors.first(),
            modifier = Modifier.offset(x = 28.dp + shakeOffset.dp, y = 76.dp),
            highlighted = emphasizeWinner,
        )
    }
}

@Composable
private fun DrawingStackCard(
    item: WheelItem,
    index: Int,
    color: Color,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .size(width = 292.dp, height = 142.dp)
            .clip(shape)
            .background(color)
            .border(
                width = if (highlighted) 2.dp else 1.5.dp,
                color = if (highlighted) Color(0xFFEC9213) else Color.White.copy(alpha = 0.14f),
                shape = shape,
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.56f)
                .fillMaxHeight(0.52f)
                .clip(RoundedCornerShape(topStart = 72.dp))
                .background(Color.White.copy(alpha = 0.06f)),
        )

        Text(
            text = item.name,
            modifier = Modifier.padding(start = 28.dp, top = 23.dp, end = 20.dp),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = (index + 1).toString(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 30.dp, bottom = 16.dp),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
internal fun DrawingThemeSwatch(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = 82.dp, height = 58.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.62f)
                .fillMaxHeight(0.54f)
                .clip(RoundedCornerShape(topStart = 36.dp))
                .background(Color.White.copy(alpha = 0.08f)),
        )
    }
}
