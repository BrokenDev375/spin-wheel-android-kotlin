package com.vga.spinwheel.ui.screen.wheel

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.data.model.WheelItem
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WheelCanvas(
    items: List<WheelItem>,
    palette: WheelPalette,
    spinStatus: SpinStatus,
    durationSeconds: Int,
    onSpinFinished: (WheelItem) -> Unit,
    onClickSpin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotationAnim = remember { Animatable(0f) }

    LaunchedEffect(spinStatus) {
        if (spinStatus is SpinStatus.Spinning) {
            val startAngle = rotationAnim.value % 360f
            val totalTarget = startAngle + spinStatus.targetAngle
            rotationAnim.animateTo(
                targetValue = totalTarget,
                animationSpec = tween(
                    durationMillis = durationSeconds * 1000,
                    easing = CubicBezierEasing(0.15f, 0.85f, 0.35f, 1.0f),
                ),
            )
            onSpinFinished(spinStatus.winner)
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .clickable(enabled = spinStatus !is SpinStatus.Spinning, onClick = onClickSpin),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (items.isEmpty()) return@Canvas

            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.width.coerceAtMost(size.height) / 2f - 20f
            val sectorAngle = 360f / items.size
            val currentRotation = rotationAnim.value

            // 1. Draw Sectors
            for (i in items.indices) {
                val startAngle = currentRotation + (i * sectorAngle)
                val color = palette.colors[i % palette.colors.size]

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sectorAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                )
            }

            // 2. Draw Sector Borders
            for (i in items.indices) {
                val startAngle = currentRotation + (i * sectorAngle)
                val rad = Math.toRadians(startAngle.toDouble())
                val endX = center.x + (radius * cos(rad)).toFloat()
                val endY = center.y + (radius * sin(rad)).toFloat()

                drawLine(
                    color = Color.White.copy(alpha = 0.4f),
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 2.dp.toPx(),
                )
            }

            // 3. Draw Outer Border Circle
            drawCircle(
                color = Color.White,
                radius = radius,
                center = center,
                style = Stroke(width = 6.dp.toPx()),
            )

            // 4. Draw Item Text Labels
            val textPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = (radius * 0.12f).coerceIn(24f, 42f)
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
                setShadowLayer(4f, 2f, 2f, android.graphics.Color.BLACK)
            }

            for (i in items.indices) {
                val midAngle = currentRotation + (i * sectorAngle) + (sectorAngle / 2f)
                val midRad = Math.toRadians(midAngle.toDouble())
                val textRadius = radius * 0.62f

                val textX = center.x + (textRadius * cos(midRad)).toFloat()
                val textY = center.y + (textRadius * sin(midRad)).toFloat()

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.save()
                    canvas.nativeCanvas.rotate(midAngle + 90f, textX, textY)
                    val label = items[i].name.let {
                        if (it.length > 12) it.take(10) + "…" else it
                    }
                    canvas.nativeCanvas.drawText(label, textX, textY, textPaint)
                    canvas.nativeCanvas.restore()
                }
            }

            // 5. Draw Center Cap
            drawCircle(
                color = Color(0xFF1D1B2E),
                radius = radius * 0.22f,
                center = center,
            )
            drawCircle(
                color = Color(0xFFFFD21E),
                radius = radius * 0.16f,
                center = center,
            )

            // 6. Draw Top Pointer/Indicator
            val pointerWidth = 36.dp.toPx()
            val pointerHeight = 44.dp.toPx()
            val pointerTop = center.y - radius - 10.dp.toPx()

            val pointerPath = Path().apply {
                moveTo(center.x, pointerTop + pointerHeight)
                lineTo(center.x - pointerWidth / 2f, pointerTop)
                lineTo(center.x + pointerWidth / 2f, pointerTop)
                close()
            }

            drawPath(
                path = pointerPath,
                color = Color(0xFFFFD21E),
            )
            drawPath(
                path = pointerPath,
                color = Color.White,
                style = Stroke(width = 3.dp.toPx()),
            )
        }
    }
}
