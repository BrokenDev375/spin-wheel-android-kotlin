package com.vga.spinwheel.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

enum class SpinFeatureVisual {
    Wheel,
    Finger,
    Coin,
    Team,
    Number,
    Drawing,
    Bottle,
    Dice,
    Card,
}

data class SpinFeatureCardStyle(
    val visual: SpinFeatureVisual,
    val gradient: List<Color>,
    val titleColor: Color = SpinColors.TextPrimary,
)

@Composable
fun SpinFeatureCard(
    title: String,
    style: SpinFeatureCardStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(SpinRadius.Card))
            .background(Brush.linearGradient(style.gradient))
            .clickable(onClick = onClick),
    ) {
        FeatureArtwork(
            visual = style.visual,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            SpinColors.BackgroundDeep.copy(alpha = 0.25f),
                            SpinColors.BackgroundDeep.copy(alpha = 0.82f),
                        ),
                    )
                )
        )
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(SpinSpacing.CardPadding),
            color = style.titleColor,
            style = MaterialTheme.typography.titleLarge.copy(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.55f),
                    offset = Offset(0f, 3f),
                    blurRadius = 6f,
                )
            ),
        )
    }
}

@Composable
private fun FeatureArtwork(
    visual: SpinFeatureVisual,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val white = Color.White.copy(alpha = 0.22f)
        val strongWhite = Color.White.copy(alpha = 0.34f)

        when (visual) {
            SpinFeatureVisual.Wheel -> {
                val center = Offset(w * 0.35f, h * 0.34f)
                val radius = w * 0.34f
                val arcColors = listOf(
                    Color(0xFFFF5F7E),
                    Color(0xFFFFC857),
                    Color(0xFF8DEE2C),
                    Color(0xFF26D9D0),
                    Color(0xFF8E5CFF),
                )
                arcColors.forEachIndexed { index, color ->
                    drawArc(
                        color = color.copy(alpha = 0.82f),
                        startAngle = index * 72f,
                        sweepAngle = 72f,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                    )
                }
                drawCircle(Color.White.copy(alpha = 0.4f), radius, center, style = Stroke(width = 8.dp.toPx()))
                drawCircle(Color(0xFFFFD21E), radius * 0.18f, center)
                drawCircle(Color.White.copy(alpha = 0.72f), radius * 0.08f, Offset(w * 0.62f, h * 0.36f))
            }

            SpinFeatureVisual.Finger -> {
                val stroke = 15.dp.toPx()
                drawLine(strongWhite, Offset(w * 0.56f, h * 0.76f), Offset(w * 0.56f, h * 0.24f), stroke, StrokeCap.Round)
                drawLine(strongWhite, Offset(w * 0.44f, h * 0.72f), Offset(w * 0.44f, h * 0.36f), stroke, StrokeCap.Round)
                drawLine(strongWhite, Offset(w * 0.68f, h * 0.66f), Offset(w * 0.76f, h * 0.46f), stroke, StrokeCap.Round)
                drawRoundRect(
                    color = Color(0xFFFF73A1).copy(alpha = 0.6f),
                    topLeft = Offset(w * 0.36f, h * 0.54f),
                    size = Size(w * 0.34f, h * 0.34f),
                    cornerRadius = CornerRadius(28.dp.toPx()),
                )
            }

            SpinFeatureVisual.Coin -> {
                repeat(6) { index ->
                    drawCircle(
                        color = Color(0xFFFFD33D).copy(alpha = 0.7f),
                        radius = 10.dp.toPx(),
                        center = Offset(w * (0.18f + index * 0.1f), h * (0.2f + (index % 2) * 0.12f)),
                    )
                }
                drawCircle(
                    color = Color(0xFFFFC12D),
                    radius = w * 0.34f,
                    center = Offset(w * 0.76f, h * 0.62f),
                )
                drawCircle(
                    color = Color(0xFFFFE184),
                    radius = w * 0.26f,
                    center = Offset(w * 0.76f, h * 0.62f),
                    style = Stroke(width = 8.dp.toPx()),
                )
            }

            SpinFeatureVisual.Team -> {
                val block = Size(w * 0.26f, h * 0.24f)
                listOf(
                    Offset(w * 0.22f, h * 0.24f) to Color(0xFFFF5F7E),
                    Offset(w * 0.42f, h * 0.34f) to Color(0xFF3EB9FF),
                    Offset(w * 0.25f, h * 0.52f) to Color(0xFFFFC857),
                    Offset(w * 0.55f, h * 0.55f) to Color(0xFF6CE5B1),
                ).forEach { (offset, color) ->
                    drawRoundRect(
                        color = color.copy(alpha = 0.64f),
                        topLeft = offset,
                        size = block,
                        cornerRadius = CornerRadius(14.dp.toPx()),
                    )
                }
                drawCircle(white, 16.dp.toPx(), Offset(w * 0.22f, h * 0.18f))
                drawCircle(white, 16.dp.toPx(), Offset(w * 0.72f, h * 0.26f))
            }

            SpinFeatureVisual.Number -> {
                drawCircle(Color(0xFFFF5470).copy(alpha = 0.86f), w * 0.22f, Offset(w * 0.82f, h * 0.78f), style = Stroke(8.dp.toPx()))
                drawLine(strongWhite, Offset(w * 0.2f, h * 0.18f), Offset(w * 0.78f, h * 0.18f), 8.dp.toPx(), StrokeCap.Round)
                rotate(32f, Offset(w * 0.62f, h * 0.32f)) {
                    drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.42f, h * 0.18f), Offset(w * 0.8f, h * 0.18f), 8.dp.toPx(), StrokeCap.Round)
                }
            }

            SpinFeatureVisual.Drawing -> {
                repeat(18) { index ->
                    val x = (index % 6) * w / 6f + w * 0.1f
                    val y = (index / 6) * h / 4f + h * 0.18f
                    drawCircle(
                        color = listOf(Color(0xFFFF5F7E), Color(0xFF45E1FF), Color(0xFFFFD33D))[index % 3].copy(alpha = 0.62f),
                        radius = 10.dp.toPx(),
                        center = Offset(x, y),
                    )
                }
            }

            SpinFeatureVisual.Bottle -> {
                withTransform({
                    rotate(18f, Offset(w * 0.68f, h * 0.52f))
                }) {
                    val bottle = Path().apply {
                        moveTo(w * 0.61f, h * 0.18f)
                        lineTo(w * 0.74f, h * 0.18f)
                        lineTo(w * 0.74f, h * 0.36f)
                        lineTo(w * 0.82f, h * 0.5f)
                        lineTo(w * 0.77f, h * 0.88f)
                        lineTo(w * 0.58f, h * 0.88f)
                        lineTo(w * 0.53f, h * 0.5f)
                        lineTo(w * 0.61f, h * 0.36f)
                        close()
                    }
                    drawPath(bottle, Color(0xFF3CB371).copy(alpha = 0.78f))
                    drawLine(Color.White.copy(alpha = 0.48f), Offset(w * 0.57f, h * 0.54f), Offset(w * 0.79f, h * 0.54f), 5.dp.toPx())
                }
            }

            SpinFeatureVisual.Dice -> {
                drawRoundRect(
                    color = Color(0xFFFF2E2E).copy(alpha = 0.78f),
                    topLeft = Offset(w * 0.58f, h * 0.16f),
                    size = Size(w * 0.34f, w * 0.34f),
                    cornerRadius = CornerRadius(14.dp.toPx()),
                )
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.88f),
                    topLeft = Offset(w * 0.34f, h * 0.48f),
                    size = Size(w * 0.34f, w * 0.34f),
                    cornerRadius = CornerRadius(14.dp.toPx()),
                )
                listOf(
                    Offset(w * 0.43f, h * 0.57f),
                    Offset(w * 0.58f, h * 0.72f),
                    Offset(w * 0.76f, h * 0.25f),
                    Offset(w * 0.86f, h * 0.36f),
                ).forEach {
                    drawCircle(Color.Black.copy(alpha = 0.74f), 5.dp.toPx(), it)
                }
            }

            SpinFeatureVisual.Card -> {
                rotate(-12f, Offset(w * 0.62f, h * 0.42f)) {
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.86f),
                        topLeft = Offset(w * 0.48f, h * 0.1f),
                        size = Size(w * 0.28f, h * 0.62f),
                        cornerRadius = CornerRadius(10.dp.toPx()),
                    )
                }
                rotate(10f, Offset(w * 0.7f, h * 0.46f)) {
                    drawRoundRect(
                        color = Color(0xFFFFE14A).copy(alpha = 0.72f),
                        topLeft = Offset(w * 0.58f, h * 0.18f),
                        size = Size(w * 0.28f, h * 0.62f),
                        cornerRadius = CornerRadius(10.dp.toPx()),
                    )
                }
                drawCircle(Color(0xFFEF476F).copy(alpha = 0.7f), w * 0.18f, Offset(w * 0.26f, h * 0.42f))
            }
        }
    }
}
