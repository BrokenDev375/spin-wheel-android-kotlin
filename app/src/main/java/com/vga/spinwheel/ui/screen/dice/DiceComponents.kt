package com.vga.spinwheel.ui.screen.dice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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
        dieBorder = Color(0x42FFFFFF),
        tileBgColors = listOf(Color(0xFFFFF4CC), Color(0xFFF8D88A))
    ),
    DiceStyle(
        dieBg = Color(0xFFD93343),
        dot = Color(0xFFFFE7EC),
        dieBorder = Color(0x42FFFFFF),
        tileBgColors = listOf(Color(0xFFCFFFE9), Color(0xFFE9FFF7))
    ),
    DiceStyle(
        dieBg = Color(0xFFFFFFFF),
        dot = Color(0xFF17131D),
        dieBorder = Color(0x1F000000),
        tileBgColors = listOf(Color(0xFFDCE8FF), Color(0xFF92B8FF))
    )
)

@Composable
fun DiceFace(
    value: Int,
    styleIndex: Int,
    isShaking: Boolean = false,
    diceIndex: Int = 0,
    modifier: Modifier = Modifier.size(80.dp),
    dotSize: Dp = 16.dp,
    contentPadding: Dp = 18.dp,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 2.dp,
) {
    val style = diceStyles.getOrNull(styleIndex) ?: diceStyles[0]
    val faceModifier = if (isShaking) {
        val cycleMs = DiceThrowCycleMs + (diceIndex % 3) * DiceThrowCycleStaggerMs
        val liftHeightPx = with(LocalDensity.current) { DiceThrowLiftHeight.toPx() }
        val transition = rememberInfiniteTransition(label = "dice_throw_$diceIndex")

        val liftProgress by transition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = cycleMs
                    0f at 0 using FastOutSlowInEasing
                    1f at (cycleMs * 0.26f).toInt() using FastOutSlowInEasing
                    0.96f at (cycleMs * 0.62f).toInt() using LinearEasing
                    0f at cycleMs using FastOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "dice_lift_$diceIndex",
        )

        val scale by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = cycleMs
                    1f at 0 using FastOutSlowInEasing
                    1.22f at (cycleMs * 0.22f).toInt() using FastOutSlowInEasing
                    1.1f at (cycleMs * 0.62f).toInt() using LinearEasing
                    0.94f at (cycleMs * 0.88f).toInt() using FastOutSlowInEasing
                    1f at cycleMs using FastOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "dice_scale_$diceIndex",
        )

        modifier.graphicsLayer {
            translationY = -liftHeightPx * liftProgress
            scaleX = scale
            scaleY = scale
        }
    } else {
        modifier
    }

    if (isShaking) {
        DiceRolling3D(
            style = style,
            diceIndex = diceIndex,
            modifier = faceModifier,
        )
    } else {
        DiceResting3D(
            style = style,
            value = value,
            modifier = faceModifier,
        )
    }
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
    val renderedDieSize = if (isShaking) dieSize * DiceRollingSizeScale else dieSize
    val horizontalPadding = if (isShaking) DiceRollingGridHorizontalPadding else 0.dp
    val verticalPadding = if (isShaking) DiceRollingGridVerticalPadding else 0.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(
            horizontal = horizontalPadding,
            vertical = verticalPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        userScrollEnabled = false,
        modifier = modifier
            .width(dieSize * columns + spacing * (columns - 1) + horizontalPadding * 2)
            .height(dieSize * rows + spacing * (rows - 1) + verticalPadding * 2),
    ) {
        itemsIndexed(values) { index, value ->
            Box(
                modifier = Modifier.size(dieSize),
                contentAlignment = Alignment.Center,
            ) {
                DiceFace(
                    value = value,
                    styleIndex = styleIndex,
                    isShaking = isShaking,
                    diceIndex = index,
                    dotSize = dotSize,
                    contentPadding = contentPadding,
                    modifier = Modifier.size(renderedDieSize),
                )
            }
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

private const val DiceRollingSizeScale = 0.86f
private val DiceRollingGridHorizontalPadding = 28.dp
private val DiceRollingGridVerticalPadding = 68.dp
private const val DiceThrowCycleMs = 1100
private const val DiceThrowCycleStaggerMs = 90
private val DiceThrowLiftHeight = 48.dp
