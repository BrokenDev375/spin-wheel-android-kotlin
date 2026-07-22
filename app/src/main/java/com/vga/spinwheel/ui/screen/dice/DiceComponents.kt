package com.vga.spinwheel.ui.screen.dice

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
        dieBorder = Color(0x24FFFFFF), // 0.14
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
    modifier: Modifier = Modifier
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
            .size(80.dp)
            .rotate(rotation)
            .clip(RoundedCornerShape(16.dp))
            .background(style.dieBg)
            .border(2.dp, style.dieBorder, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        val safeValue = value.coerceIn(1, 6)
        when (safeValue) {
            1 -> {
                Dot(style.dot, Alignment.Center)
            }
            2 -> {
                Dot(style.dot, Alignment.TopStart)
                Dot(style.dot, Alignment.BottomEnd)
            }
            3 -> {
                Dot(style.dot, Alignment.TopStart)
                Dot(style.dot, Alignment.Center)
                Dot(style.dot, Alignment.BottomEnd)
            }
            4 -> {
                Dot(style.dot, Alignment.TopStart)
                Dot(style.dot, Alignment.TopEnd)
                Dot(style.dot, Alignment.BottomStart)
                Dot(style.dot, Alignment.BottomEnd)
            }
            5 -> {
                Dot(style.dot, Alignment.TopStart)
                Dot(style.dot, Alignment.TopEnd)
                Dot(style.dot, Alignment.Center)
                Dot(style.dot, Alignment.BottomStart)
                Dot(style.dot, Alignment.BottomEnd)
            }
            6 -> {
                Dot(style.dot, Alignment.TopStart)
                Dot(style.dot, Alignment.TopEnd)
                Dot(style.dot, Alignment.CenterStart)
                Dot(style.dot, Alignment.CenterEnd)
                Dot(style.dot, Alignment.BottomStart)
                Dot(style.dot, Alignment.BottomEnd)
            }
        }
    }
}

@Composable
fun BoxScope.Dot(color: Color, alignment: Alignment) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .align(alignment)
            .clip(CircleShape)
            .background(color)
    )
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
