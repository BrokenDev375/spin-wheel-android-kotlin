package com.vga.spinwheel.ui.screen.dice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import kotlinx.coroutines.delay
import kotlin.random.Random

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
    if (isShaking) {
        // Mỗi xúc xắc có 1 kiểu quay riêng: liên tục CW / liên tục CCW / lắc qua lại
        // → nhìn lộn xộn, không đều, không có 2 cái giống nhau
        data class SpinConfig(
            val target: Float,
            val durationMs: Int,
            val mode: RepeatMode,
            val bounceDurationMs: Int = 360,
            val bounceHeight: Dp = 10.dp,
            val bounceDelayMs: Int = 0,
        )
        val configs = listOf(
            SpinConfig( 360f, 260, RepeatMode.Restart, 340, 22.dp,   0),  // 0: nhanh CW liên tục
            SpinConfig(-180f, 170, RepeatMode.Reverse,  410, 18.dp,  95),  // 1: lắc nhanh CCW
            SpinConfig(-360f, 320, RepeatMode.Restart, 365, 24.dp, 170),  // 2: vừa CCW liên tục
            SpinConfig( 200f, 210, RepeatMode.Reverse,  455, 20.dp,  55),  // 3: lắc CW nhanh
            SpinConfig( 360f, 280, RepeatMode.Restart, 390, 26.dp, 130),  // 4: vừa CW liên tục
            SpinConfig(-360f, 400, RepeatMode.Restart, 500, 21.dp, 215),  // 5: chậm CCW liên tục
        )
        val cfg = configs[diceIndex % configs.size]
        val rotationDurationMs = (cfg.durationMs * 1.65f).toInt()
        val rotationTarget = (cfg.target * DiceRollingRotationScale).coerceIn(-DiceRollingMaxRotation, DiceRollingMaxRotation)
        val bounceDurationMs = (cfg.bounceDurationMs + (diceIndex % 3) * 35).coerceAtMost(560)
        val bounceDelayMs = cfg.bounceDelayMs + (diceIndex % 2) * 25
        val bounceHeight = cfg.bounceHeight + if (diceIndex % 2 == 0) 3.dp else 0.dp
        val bounceHeightPx = with(LocalDensity.current) { bounceHeight.toPx() }
        val startsLifted = diceIndex % 2 == 1

        val transition = rememberInfiniteTransition("dice_spin_$diceIndex")

        val rotZ by transition.animateFloat(
            initialValue = 0f,
            targetValue = rotationTarget,
            animationSpec = infiniteRepeatable(
                animation = tween(rotationDurationMs, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "rotZ_$diceIndex"
        )

        val bounceY by transition.animateFloat(
            initialValue = if (startsLifted) -bounceHeightPx else 0f,
            targetValue = if (startsLifted) 0f else -bounceHeightPx,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = bounceDurationMs,
                    delayMillis = bounceDelayMs,
                    easing = FastOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "bounceY_$diceIndex"
        )

        val bounceScale by transition.animateFloat(
            initialValue = if (startsLifted) 0.57f else 0.67f,
            targetValue = if (startsLifted) 0.67f else 0.57f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = bounceDurationMs,
                    delayMillis = bounceDelayMs,
                    easing = FastOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "bounceScale_$diceIndex"
        )

        // Refresh rolling face at a calmer pace.
        val spinValue by produceState(Random.nextInt(1, 7)) {
            while (true) {
                delay(170L)
                this.value = Random.nextInt(1, 7)
            }
        }

        DiceIsometricStatic(
            style = style,
            value = spinValue,
            modifier = modifier.graphicsLayer {
                rotationZ = rotZ
                translationY = bounceY
                // Keep the max scale below the 45-degree diagonal so rotation does not clip.
                scaleX = bounceScale
                scaleY = bounceScale
            }
        )
    } else {
        // --- KẾT QUẢ / ĐỨNG YÊN: mặt phẳng 2D như cũ ---
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(cornerRadius))
                .background(style.dieBg)
                .border(borderWidth, style.dieBorder, RoundedCornerShape(cornerRadius))
                .padding(contentPadding)
        ) {
            val safeValue = value.coerceIn(1, 6)
            when (safeValue) {
                1 -> Dot(style.dot, Alignment.Center, dotSize)
                2 -> { Dot(style.dot, Alignment.TopStart, dotSize); Dot(style.dot, Alignment.BottomEnd, dotSize) }
                3 -> { Dot(style.dot, Alignment.TopStart, dotSize); Dot(style.dot, Alignment.Center, dotSize); Dot(style.dot, Alignment.BottomEnd, dotSize) }
                4 -> { Dot(style.dot, Alignment.TopStart, dotSize); Dot(style.dot, Alignment.TopEnd, dotSize); Dot(style.dot, Alignment.BottomStart, dotSize); Dot(style.dot, Alignment.BottomEnd, dotSize) }
                5 -> { Dot(style.dot, Alignment.TopStart, dotSize); Dot(style.dot, Alignment.TopEnd, dotSize); Dot(style.dot, Alignment.Center, dotSize); Dot(style.dot, Alignment.BottomStart, dotSize); Dot(style.dot, Alignment.BottomEnd, dotSize) }
                6 -> { Dot(style.dot, Alignment.TopStart, dotSize); Dot(style.dot, Alignment.TopEnd, dotSize); Dot(style.dot, Alignment.CenterStart, dotSize); Dot(style.dot, Alignment.CenterEnd, dotSize); Dot(style.dot, Alignment.BottomStart, dotSize); Dot(style.dot, Alignment.BottomEnd, dotSize) }
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
    val renderedDieSize = if (isShaking) dieSize * DiceRollingSizeScale else dieSize
    val edgePadding = if (isShaking) DiceRollingGridEdgePadding else 0.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(edgePadding),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        userScrollEnabled = false,
        modifier = modifier
            .width(dieSize * columns + spacing * (columns - 1) + edgePadding * 2)
            .height(dieSize * rows + spacing * (rows - 1) + edgePadding * 2),
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

private val DicePanelColor = Color(0xFF393347)
private val DiceAccentColor = Color(0xFFEC9213)
private val DiceStrokeColor = Color(0xFF8C8893)
private const val DiceRollingRotationScale = 0.58f
private const val DiceRollingMaxRotation = 190f
private const val DiceRollingSizeScale = 0.86f
private val DiceRollingGridEdgePadding = 24.dp
