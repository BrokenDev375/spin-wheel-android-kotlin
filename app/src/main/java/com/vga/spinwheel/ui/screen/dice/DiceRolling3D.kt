package com.vga.spinwheel.ui.screen.dice

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

// --- Vị trí chấm trên mỗi mặt (u,v): u=0 trái → 1 phải, v=0 trên → 1 dưới ---
private val diceDots = mapOf(
    1 to listOf(0.5f to 0.5f),
    2 to listOf(0.3f to 0.3f, 0.7f to 0.7f),
    3 to listOf(0.3f to 0.3f, 0.5f to 0.5f, 0.7f to 0.7f),
    4 to listOf(0.3f to 0.3f, 0.7f to 0.3f, 0.3f to 0.7f, 0.7f to 0.7f),
    5 to listOf(0.3f to 0.3f, 0.7f to 0.3f, 0.5f to 0.5f, 0.3f to 0.7f, 0.7f to 0.7f),
    6 to listOf(0.3f to 0.22f, 0.7f to 0.22f, 0.3f to 0.5f, 0.7f to 0.5f, 0.3f to 0.78f, 0.7f to 0.78f),
)

// --- Toán học 3D ---
private fun rotX(v: FloatArray, s: Float, c: Float) = floatArrayOf(v[0], v[1] * c - v[2] * s, v[1] * s + v[2] * c)
private fun rotY(v: FloatArray, s: Float, c: Float) = floatArrayOf(v[0] * c + v[2] * s, v[1], -v[0] * s + v[2] * c)
private fun vsub(a: FloatArray, b: FloatArray) = floatArrayOf(a[0] - b[0], a[1] - b[1], a[2] - b[2])
private fun vcross(a: FloatArray, b: FloatArray) = floatArrayOf(
    a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]
)
private fun vnorm(v: FloatArray): FloatArray {
    val m = sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2])
    return if (m > 0f) floatArrayOf(v[0] / m, v[1] / m, v[2] / m) else v
}
private fun vdot(a: FloatArray, b: FloatArray) = a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
private fun tintColor(c: Color, b: Float) = Color(
    (c.red * b).coerceIn(0f, 1f), (c.green * b).coerceIn(0f, 1f),
    (c.blue * b).coerceIn(0f, 1f), c.alpha
)

// Góc nghiêng isometric cố định (X tilt ~30°)
private val ISOMETRIC_TILT_X = -(PI / 6.0).toFloat()
// Góc Y ban đầu → 3 mặt hiện đẹp như ảnh tham khảo
private val ISOMETRIC_START_Y = (PI / 4.0).toFloat()
// Hướng ánh sáng: từ trên-trái-trước
private val LIGHT_DIR = vnorm(floatArrayOf(-0.3f, -0.95f, 0.3f))

/**
 * Hàm vẽ hình lập phương isometric bằng chiếu SONG SONG (orthographic).
 * Không bóp méo phối cảnh → trông giống đúng ảnh tham khảo.
 */
private fun DrawScope.drawIsometricCube(
    style: DiceStyle,
    angY: Float,
    faceTop: Int,
    faceFront: Int,
    faceSide: Int,
) {
    val scale = size.minDimension * 0.42f
    val cx = size.width / 2f
    val cy = size.height / 2f

    val sX = sin(ISOMETRIC_TILT_X); val cX = cos(ISOMETRIC_TILT_X)
    val sY = sin(angY);              val cY = cos(angY)

    val base = arrayOf(
        floatArrayOf(-1f, -1f, -1f), floatArrayOf(1f, -1f, -1f),
        floatArrayOf(1f,  1f, -1f),  floatArrayOf(-1f,  1f, -1f),
        floatArrayOf(-1f, -1f,  1f), floatArrayOf(1f, -1f,  1f),
        floatArrayOf(1f,  1f,  1f),  floatArrayOf(-1f,  1f,  1f),
    )

    val rot = base.map { rotY(rotX(it, sX, cX), sY, cY) }.toTypedArray()

    // ORTHOGRAPHIC: chỉ dùng x, y của điểm đã xoay (không chia cho z)
    val pr = rot.map { Offset(it[0] * scale + cx, it[1] * scale + cy) }.toTypedArray()

    // Mặt và giá trị chấm (opposite faces sum to 7 — chuẩn xúc xắc)
    val faceData = listOf(
        intArrayOf(4, 5, 6, 7) to faceFront,       // front  (+Z)
        intArrayOf(1, 0, 3, 2) to (7 - faceFront), // back   (-Z)
        intArrayOf(0, 4, 7, 3) to faceSide,        // left   (-X)
        intArrayOf(5, 1, 2, 6) to (7 - faceSide),  // right  (+X)
        intArrayOf(3, 7, 6, 2) to faceTop,         // top    (-Y)
        intArrayOf(0, 1, 5, 4) to (7 - faceTop),  // bottom (+Y)
    )

    // Painter's algorithm: vẽ từ xa → gần
    val sorted = faceData.sortedByDescending { (vi, _) ->
        vi.sumOf { rot[it][2].toDouble() } / 4.0
    }

    for ((vi, faceVal) in sorted) {
        val n = vnorm(vcross(vsub(rot[vi[1]], rot[vi[0]]), vsub(rot[vi[3]], rot[vi[0]])))

        // Chỉ vẽ mặt quay về phía người xem
        if (n[2] <= 0f) continue

        // Độ sáng khuếch tán: top face sáng nhất, các mặt bên tối hơn
        val brightness = (0.38f + vdot(n, LIGHT_DIR).coerceIn(0f, 1f) * 0.62f)

        val path = Path().apply {
            moveTo(pr[vi[0]].x, pr[vi[0]].y)
            for (i in 1 until vi.size) lineTo(pr[vi[i]].x, pr[vi[i]].y)
            close()
        }
        drawPath(path, tintColor(style.dieBg, brightness))
        drawPath(path, style.dieBorder.copy(alpha = 0.55f), style = Stroke(2f * density))

        // Vẽ chấm qua nội suy song tuyến trên mặt
        val dots = diceDots[faceVal.coerceIn(1, 6)] ?: continue
        val dotR = scale * 0.105f
        val p0 = pr[vi[0]]; val p1 = pr[vi[1]]; val p2 = pr[vi[2]]; val p3 = pr[vi[3]]
        val dotColor = tintColor(style.dot, brightness * 0.88f)

        for ((u, v) in dots) {
            val px = (1 - v) * ((1 - u) * p0.x + u * p1.x) + v * ((1 - u) * p3.x + u * p2.x)
            val py = (1 - v) * ((1 - u) * p0.y + u * p1.y) + v * ((1 - u) * p3.y + u * p2.y)
            drawCircle(dotColor, dotR, Offset(px, py))
        }
    }
}

/**
 * Hình lập phương isometric TĨNH — hiển thị khi chưa quay.
 * value là mặt trên. Các mặt khác tuân theo quy tắc xúc xắc chuẩn.
 */
@Composable
fun DiceIsometricStatic(
    style: DiceStyle,
    value: Int,
    modifier: Modifier = Modifier,
) {
    val v = value.coerceIn(1, 6)
    // Chọn các mặt xung quanh hợp lệ (không trùng, tổng mặt đối = 7)
    val top   = v
    val front = when (v) { 1 -> 2; 2 -> 1; 3 -> 4; 4 -> 3; 5 -> 2; else -> 1 }
    val side  = when (v) { 1 -> 3; 2 -> 3; 3 -> 2; 4 -> 2; 5 -> 3; else -> 2 }

    Canvas(modifier = modifier) {
        drawIsometricCube(style, ISOMETRIC_START_Y, top, front, side)
    }
}

/**
 * Hình lập phương isometric QUAY — hiển thị khi đang lắc.
 * Xoay liên tục quanh trục Y. Các mặt hiện dots ngẫu nhiên thay đổi liên tục.
 */
@Composable
fun DiceRolling3D(
    style: DiceStyle,
    diceIndex: Int,
    modifier: Modifier = Modifier,
) {
    val phaseMs = (diceIndex * 220).coerceAtMost(880)
    val transition = rememberInfiniteTransition("dice_iso_$diceIndex")

    // Bắt đầu từ góc isometric (PI/4) rồi xoay 360°
    val angY by transition.animateFloat(
        initialValue = ISOMETRIC_START_Y,
        targetValue = ISOMETRIC_START_Y + (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1400 + phaseMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angY_$diceIndex"
    )

    val bounceY by transition.animateFloat(
        initialValue = 0f, targetValue = -42f,
        animationSpec = infiniteRepeatable(
            animation = tween(430 + phaseMs, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bY_$diceIndex"
    )

    // Dots ngẫu nhiên thay đổi liên tục trên từng cặp mặt
    val faceTop by produceState(Random.nextInt(1, 7)) {
        while (true) { delay(190L + phaseMs / 4); value = Random.nextInt(1, 7) }
    }
    val faceFront by produceState(Random.nextInt(1, 7)) {
        while (true) { delay(175L + phaseMs / 4); value = Random.nextInt(1, 7) }
    }
    val faceSide by produceState(Random.nextInt(1, 7)) {
        while (true) { delay(210L + phaseMs / 4); value = Random.nextInt(1, 7) }
    }

    Canvas(modifier = modifier.graphicsLayer { translationY = bounceY }) {
        drawIsometricCube(style, angY, faceTop, faceFront, faceSide)
    }
}
