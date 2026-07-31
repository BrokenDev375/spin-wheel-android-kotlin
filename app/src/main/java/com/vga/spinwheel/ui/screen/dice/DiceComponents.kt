package com.vga.spinwheel.ui.screen.dice

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    animationMillis: Int = DiceThrowDefaultAnimationMillis,
    modifier: Modifier = Modifier.size(80.dp),
    dotSize: Dp = 16.dp,
    contentPadding: Dp = 18.dp,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 2.dp,
) {
    val style = diceStyles.getOrNull(styleIndex) ?: diceStyles[0]
    val throwProgress = remember(diceIndex) { Animatable(1f) }

    LaunchedEffect(isShaking, animationMillis) {
        if (isShaking) {
            throwProgress.snapTo(0f)
            throwProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = animationMillis.coerceAtLeast(1),
                    easing = LinearEasing,
                ),
            )
        } else {
            throwProgress.snapTo(1f)
        }
    }

    val progress = if (isShaking) throwProgress.value else 1f
    val faceModifier = if (isShaking) {
        val liftHeightPx = with(LocalDensity.current) { DiceThrowLiftHeight.toPx() }
        val liftProgress = diceThrowLiftProgress(progress)
        val scale = diceThrowScale(progress)

        modifier.graphicsLayer {
            translationY = -liftHeightPx * liftProgress
            scaleX = scale
            scaleY = scale
        }
    } else {
        modifier
    }

    if (isShaking) {
        if (progress < DiceThrowResultRevealProgress) {
            DiceRolling3D(
                style = style,
                value = value,
                diceIndex = diceIndex,
                progress = (progress / DiceThrowResultRevealProgress).coerceIn(0f, 1f),
                modifier = faceModifier,
            )
        } else {
            DiceResting3D(
                style = style,
                value = value,
                modifier = faceModifier,
            )
        }
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
    reserveAnimationSpace: Boolean = isShaking,
    animationMillis: Int = DiceThrowDefaultAnimationMillis,
    singleDieSize: Dp = 160.dp,
    gridDieSize: Dp = 104.dp,
    spacing: Dp = 16.dp,
) {
    val columns = if (values.size <= 1) 1 else 2
    val rows = (values.size.coerceAtLeast(1) + columns - 1) / columns
    val dieSize = if (values.size <= 1) singleDieSize else gridDieSize
    val dotSize = if (values.size <= 1) 28.dp else 16.dp
    val contentPadding = if (values.size <= 1) 32.dp else 18.dp
    val horizontalPadding = if (reserveAnimationSpace) DiceRollingGridHorizontalPadding else 0.dp
    val topPadding = if (reserveAnimationSpace) DiceRollingGridTopPadding else 0.dp
    val bottomPadding = if (reserveAnimationSpace) DiceRollingGridBottomPadding else 0.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            top = topPadding,
            end = horizontalPadding,
            bottom = bottomPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        userScrollEnabled = false,
        modifier = modifier
            .width(dieSize * columns + spacing * (columns - 1) + horizontalPadding * 2)
            .height(dieSize * rows + spacing * (rows - 1) + topPadding + bottomPadding),
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
                    animationMillis = animationMillis,
                    dotSize = dotSize,
                    contentPadding = contentPadding,
                    modifier = Modifier.size(dieSize),
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

private fun diceThrowLiftProgress(progress: Float): Float {
    val p = progress.coerceIn(0f, 1f)
    return when {
        p < DiceThrowPeakProgress -> {
            val t = (p / DiceThrowPeakProgress).coerceIn(0f, 1f)
            FastOutSlowInEasing.transform(t)
        }
        p < DiceThrowResultRevealProgress -> {
            val t = ((p - DiceThrowPeakProgress) / (DiceThrowResultRevealProgress - DiceThrowPeakProgress))
                .coerceIn(0f, 1f)
            lerpFloat(1f, 0.96f, t)
        }
        else -> {
            val t = ((p - DiceThrowResultRevealProgress) / (1f - DiceThrowResultRevealProgress))
                .coerceIn(0f, 1f)
            lerpFloat(0.96f, 0f, FastOutSlowInEasing.transform(t))
        }
    }
}

private fun diceThrowScale(progress: Float): Float {
    val p = progress.coerceIn(0f, 1f)
    return when {
        p < DiceThrowScalePeakProgress -> {
            val t = (p / DiceThrowScalePeakProgress).coerceIn(0f, 1f)
            lerpFloat(1f, 1.1f, FastOutSlowInEasing.transform(t))
        }
        p < DiceThrowResultRevealProgress -> {
            val t = ((p - DiceThrowScalePeakProgress) / (DiceThrowResultRevealProgress - DiceThrowScalePeakProgress))
                .coerceIn(0f, 1f)
            lerpFloat(1.1f, 1.04f, t)
        }
        p < DiceThrowSquashProgress -> {
            val t = ((p - DiceThrowResultRevealProgress) / (DiceThrowSquashProgress - DiceThrowResultRevealProgress))
                .coerceIn(0f, 1f)
            lerpFloat(1.04f, 0.98f, FastOutSlowInEasing.transform(t))
        }
        else -> {
            val t = ((p - DiceThrowSquashProgress) / (1f - DiceThrowSquashProgress))
                .coerceIn(0f, 1f)
            lerpFloat(0.98f, 1f, FastOutSlowInEasing.transform(t))
        }
    }
}

private fun lerpFloat(start: Float, stop: Float, fraction: Float): Float =
    start + (stop - start) * fraction.coerceIn(0f, 1f)

private val DiceRollingGridHorizontalPadding = 28.dp
private val DiceRollingGridTopPadding = 56.dp
private val DiceRollingGridBottomPadding = 12.dp
private const val DiceThrowDefaultAnimationMillis = 1_000
internal const val DiceThrowResultRevealProgress = 0.72f
private const val DiceThrowPeakProgress = 0.26f
private const val DiceThrowScalePeakProgress = 0.22f
private const val DiceThrowSquashProgress = 0.88f
private val DiceThrowLiftHeight = 48.dp
