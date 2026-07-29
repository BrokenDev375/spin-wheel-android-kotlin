package com.vga.spinwheel.ui.screen.wheel

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.clickableWithSound
import com.vga.spinwheel.data.model.WheelItem
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Cắt [label] sao cho chiều rộng pixel (đo bằng [paint]) không vượt quá [maxWidthPx].
 *
 * Dùng Paint.measureText() thay vì đếm ký tự vì mỗi ký tự có độ rộng khác nhau
 * (ví dụ 'W' rộng hơn 'i', ký tự tiếng Việt có dấu rộng hơn chữ Latin thường).
 *
 * Thuật toán binary search để tránh vòng lặp O(n) với chuỗi dài.
 */
private fun fitLabelToWidth(label: String, paint: Paint, maxWidthPx: Float): String {
    if (paint.measureText(label) <= maxWidthPx) return label

    // Binary search tìm số ký tự tối đa vừa với maxWidthPx
    var lo = 1
    var hi = label.length - 1
    while (lo < hi) {
        val mid = (lo + hi + 1) / 2
        if (paint.measureText(label.take(mid) + "…") <= maxWidthPx) lo = mid else hi = mid - 1
    }
    return label.take(lo) + "…"
}

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
            .padding(2.dp)
            .clickableWithSound(enabled = spinStatus !is SpinStatus.Spinning, onClick = onClickSpin),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (items.isEmpty()) return@Canvas

            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.width.coerceAtMost(size.height) / 2f
            val sectorAngle = 360f / items.size
            val currentRotation = if (spinStatus is SpinStatus.Finished && rotationAnim.value == 0f) {
                val winnerIndex = items.indexOfFirst { it.id == spinStatus.winner.id }.coerceAtLeast(0)
                270f - (winnerIndex * sectorAngle + sectorAngle / 2f)
            } else {
                rotationAnim.value
            }

            // 1. Draw Sectors
            for (i in items.indices) {
                val startAngle = currentRotation + (i * sectorAngle)
                val color = palette.colors[(i + 1) % palette.colors.size]

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sectorAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                )
            }

            // Keep same-color adjacent slices distinguishable without outlining the wheel.
            for (i in items.indices) {
                val startAngle = currentRotation + (i * sectorAngle)
                val rad = Math.toRadians(startAngle.toDouble())
                val endX = center.x + (radius * cos(rad)).toFloat()
                val endY = center.y + (radius * sin(rad)).toFloat()

                drawLine(
                    color = Color.Black.copy(alpha = 0.08f),
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 1.dp.toPx(),
                )
            }

            // Draw labels — chữ radial (dọc theo tia từ tâm ra rim).
            // Hướng radial phù hợp hơn tangential khi nhiều item vì:
            //   • Chiều dài tia = radius (cố định, không đổi theo số item)
            //   • Chiều rộng arc = 2r·sin(θ/2) → hẹp dần khi nhiều item
            val baseTextSize = (radius * 0.085f).coerceIn(16f, 32f)
            val scaledTextSize = when {
                items.size > 10 -> baseTextSize * 0.70f
                items.size > 6  -> baseTextSize * 0.82f
                else            -> baseTextSize
            }

            val textPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = scaledTextSize
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }

            // Vị trí giữa tia: từ mép cục tâm (cap = 0.17r) đến gần rim.
            // midTextRadius = giữa đoạn [capEdge, rim] = (0.17 + 1.0) / 2 = 0.585r
            val capEdge = 0.17f       // bán kính cục trắng ở tâm
            val rimEdge = 0.92f       // dừng trước rim để có lề
            val textRadius = radius * (capEdge + rimEdge) / 2f   // ~0.545r

            // Chiều dài tại tâm tia khả dụng cho text = (rimEdge - capEdge) * radius
            // Nhân 0.85 để có padding 2 đầu, text không chạm cỡp tâm hay rim.
            val availableRadialPx = (rimEdge - capEdge) * radius * 0.85f

            for (i in items.indices) {
                val midAngle = currentRotation + (i * sectorAngle) + (sectorAngle / 2f)
                val midRad = Math.toRadians(midAngle.toDouble())

                val textX = center.x + (textRadius * cos(midRad)).toFloat()
                val textY = center.y + (textRadius * sin(midRad)).toFloat()

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.save()
                    // midAngle (không + 90) → text dọc theo tia, đọc từ tâm ra ngoài.
                    canvas.nativeCanvas.rotate(midAngle, textX, textY)
                    // fitLabelToWidth dùng chiều dài tia — chứa được nhiều text hơn arc
                    val label = fitLabelToWidth(items[i].name, textPaint, availableRadialPx)
                    canvas.nativeCanvas.drawText(label, textX, textY, textPaint)
                    canvas.nativeCanvas.restore()
                }
            }

            // The original uses a white center cap with an upward pointer.
            val pointerPath = Path().apply {
                moveTo(center.x, center.y - radius * 0.28f)
                lineTo(center.x - radius * 0.11f, center.y - radius * 0.08f)
                lineTo(center.x + radius * 0.11f, center.y - radius * 0.08f)
                close()
            }
            drawPath(pointerPath, color = Color.White)
            drawCircle(
                color = Color.White,
                radius = radius * 0.17f,
                center = center,
            )
        }
    }
}
