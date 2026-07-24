package com.vga.spinwheel.ui.screen.dice

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DiceStyle(
    val dieBg: Color,
    val dot: Color,
    val dieBorder: Color,
    val tileBgColors: List<Color>,
)

val diceStyles = listOf(
    DiceStyle(
        dieBg = Color(0xFF111116),
        dot = Color(0xFFFFFFFF),
        dieBorder = Color(0xFF8D8B8F),
        tileBgColors = listOf(Color(0xFF56535E), Color(0xFFDFE0E4))
    ),
    DiceStyle(
        dieBg = Color(0xFFDF3438),
        dot = Color(0xFFFFFFFF),
        dieBorder = Color(0x42FFFFFF), // 0.26
        tileBgColors = listOf(Color(0xFFFFF4CC), Color(0xFFF8D88A))
    ),
    DiceStyle(
        dieBg = Color(0xFFD93343),
        dot = Color(0xFFFFE7EC),
        dieBorder = Color(0x42FFFFFF), // 0.26
        tileBgColors = listOf(Color(0xFFCFFFE9), Color(0xFFE9FFF7))
    ),
    DiceStyle(
        dieBg = Color(0xFFFFFFFF),
        dot = Color(0xFF17131D),
        dieBorder = Color(0x1F000000), // 0.12
        tileBgColors = listOf(Color(0xFFDCE8FF), Color(0xFF92B8FF))
    )
)

@Composable
fun DiceFace(
    value: Int,
    styleIndex: Int,
    isShaking: Boolean = false,
    modifier: Modifier = Modifier.size(80.dp),
    dotSize: Dp = 16.dp,
    contentPadding: Dp = 18.dp,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 2.dp,
) {
    val style = diceStyles.getOrNull(styleIndex) ?: diceStyles[0]

    val infiniteTransition = rememberInfiniteTransition(label = "dice_shake")
    val rotation by infiniteTransition.animateFloat(
        initialValue = if (isShaking) -10f else 0f,
        targetValue = if (isShaking) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dice_rotation"
    )

    Box(
        modifier = modifier
            .rotate(rotation)
            .clip(RoundedCornerShape(cornerRadius))
            .background(style.dieBg)
            .border(borderWidth, style.dieBorder, RoundedCornerShape(cornerRadius))
            .padding(contentPadding)
    ) {
        val safeValue = value.coerceIn(1, 6)
        when (safeValue) {
            1 -> {
                Dot(style.dot, Alignment.Center, dotSize)
            }
            2 -> {
                Dot(style.dot, Alignment.TopStart, dotSize)
                Dot(style.dot, Alignment.BottomEnd, dotSize)
            }
            3 -> {
                Dot(style.dot, Alignment.TopStart, dotSize)
                Dot(style.dot, Alignment.Center, dotSize)
                Dot(style.dot, Alignment.BottomEnd, dotSize)
            }
            4 -> {
                Dot(style.dot, Alignment.TopStart, dotSize)
                Dot(style.dot, Alignment.TopEnd, dotSize)
                Dot(style.dot, Alignment.BottomStart, dotSize)
                Dot(style.dot, Alignment.BottomEnd, dotSize)
            }
            5 -> {
                Dot(style.dot, Alignment.TopStart, dotSize)
                Dot(style.dot, Alignment.TopEnd, dotSize)
                Dot(style.dot, Alignment.Center, dotSize)
                Dot(style.dot, Alignment.BottomStart, dotSize)
                Dot(style.dot, Alignment.BottomEnd, dotSize)
            }
            6 -> {
                Dot(style.dot, Alignment.TopStart, dotSize)
                Dot(style.dot, Alignment.TopEnd, dotSize)
                Dot(style.dot, Alignment.CenterStart, dotSize)
                Dot(style.dot, Alignment.CenterEnd, dotSize)
                Dot(style.dot, Alignment.BottomStart, dotSize)
                Dot(style.dot, Alignment.BottomEnd, dotSize)
            }
        }
    }
}

@Composable
fun BoxScope.Dot(color: Color, alignment: Alignment, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .align(alignment)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun DiceGrid(
    values: List<Int>,
    styleIndex: Int,
    modifier: Modifier = Modifier,
    isShaking: Boolean = false,
    singleDieSize: Dp = 160.dp,
    gridDieSize: Dp = 104.dp,
    spacing: Dp = 16.dp,
) {
    val columns = if (values.size <= 1) 1 else 2
    val rows = (values.size.coerceAtLeast(1) + columns - 1) / columns
    val dieSize = if (values.size <= 1) singleDieSize else gridDieSize
    val dotSize = if (values.size <= 1) 28.dp else 16.dp
    val contentPadding = if (values.size <= 1) 32.dp else 18.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        userScrollEnabled = false,
        modifier = modifier
            .width(dieSize * columns + spacing * (columns - 1))
            .height(dieSize * rows + spacing * (rows - 1)),
    ) {
        items(values) { value ->
            DiceFace(
                value = value,
                styleIndex = styleIndex,
                isShaking = isShaking,
                dotSize = dotSize,
                contentPadding = contentPadding,
                modifier = Modifier.size(dieSize),
            )
        }
    }
}

@Composable
fun DiceTile(
    styleIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val style = diceStyles.getOrNull(styleIndex) ?: diceStyles[0]
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(style.tileBgColors)),
        contentAlignment = Alignment.Center,
        content = content
    )
}
