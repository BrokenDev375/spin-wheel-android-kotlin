package com.vga.spinwheel.ui.screen.dice

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
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
        val dotR = scale * 0.125f
        val p0 = pr[vi[0]]; val p1 = pr[vi[1]]; val p2 = pr[vi[2]]; val p3 = pr[vi[3]]
        val dotColor = style.dot

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
 * Hình lập phương QUAY — hiển thị khi đang lắc.
 * Xoay theo yaw/pitch với số vòng ngẫu nhiên để nhìn như xúc xắc lật nhanh trên không.
 */
@Composable
fun DiceRolling3D(
    style: DiceStyle,
    value: Int,
    diceIndex: Int,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val rotationSpec = remember(diceIndex) { diceRollingRotationSpec(diceIndex) }
    val safeProgress = progress.coerceIn(0f, 1f)

    Canvas(modifier = modifier) {
        drawPerspectiveDiceCube(
            style = style,
            yawDeg = rotationSpec.yawDegrees * safeProgress,
            pitchDeg = rotationSpec.pitchDegrees * safeProgress,
            frontValue = value,
        )
    }
}

@Composable
fun DiceResting3D(
    style: DiceStyle,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawPerspectiveDiceCube(
            style = style,
            yawDeg = 0f,
            pitchDeg = 0f,
            frontValue = value,
        )
    }
}

private data class DiceVec3(val x: Float, val y: Float, val z: Float) {
    operator fun plus(other: DiceVec3) = DiceVec3(x + other.x, y + other.y, z + other.z)
    operator fun times(scale: Float) = DiceVec3(x * scale, y * scale, z * scale)
}

private data class DiceRollingRotationSpec(
    val yawDegrees: Float,
    val pitchDegrees: Float,
)

private fun diceRollingRotationSpec(diceIndex: Int): DiceRollingRotationSpec {
    val yawDirection = if (diceIndex % 2 == 0) 1f else -1f
    val pitchDirection = if ((diceIndex / 2) % 2 == 0) -1f else 1f
    return DiceRollingRotationSpec(
        yawDegrees = yawDirection * (DiceYawBaseTurns + Random.nextInt(DiceYawExtraTurnsExclusive)) * 360f,
        pitchDegrees = pitchDirection * (DicePitchBaseTurns + Random.nextInt(DicePitchExtraTurnsExclusive)) * 360f,
    )
}

private data class PerspectiveDiceFace(
    val value: Int,
    val center: DiceVec3,
    val right: DiceVec3,
    val up: DiceVec3,
)

private data class VisiblePerspectiveFace(
    val face: PerspectiveDiceFace,
    val rotatedCenter: DiceVec3,
    val screenPoints: List<Offset>,
)

private fun DrawScope.drawPerspectiveDiceCube(
    style: DiceStyle,
    yawDeg: Float,
    pitchDeg: Float,
    frontValue: Int,
) {
    val yawRad = (yawDeg * PI / 180.0).toFloat()
    val pitchRad = (pitchDeg * PI / 180.0).toFloat()
    drawPerspectiveDiceCubeTransformed(
        style = style,
        frontValue = frontValue,
        rotatePoint = { rotatePerspectiveDice(it, yawRad, pitchRad) },
    )
}

private fun DrawScope.drawPerspectiveDiceCubeTransformed(
    style: DiceStyle,
    frontValue: Int,
    rotatePoint: (DiceVec3) -> DiceVec3,
) {
    val scalePx = size.minDimension * DiceCubeScale
    val centerPx = Offset(size.width / 2f, size.height / 2f)

    val visibleFaces = perspectiveDiceFacesForFront(frontValue).mapNotNull { face ->
        val rotatedCenter = rotatePoint(face.center)
        if (rotatedCenter.z <= DiceCubeVisibleFaceEpsilon) return@mapNotNull null

        val corners = listOf(
            face.center + face.right * -1f + face.up * -1f,
            face.center + face.right * 1f + face.up * -1f,
            face.center + face.right * 1f + face.up * 1f,
            face.center + face.right * -1f + face.up * 1f,
        )

        VisiblePerspectiveFace(
            face = face,
            rotatedCenter = rotatedCenter,
            screenPoints = corners.map {
                projectPerspectiveDice(rotatePoint(it), scalePx, centerPx)
            },
        )
    }.sortedBy { it.rotatedCenter.z }

    visibleFaces.forEach { visibleFace ->
        val brightness = 0.58f + 0.42f * visibleFace.rotatedCenter.z.coerceIn(0f, 1f)
        val path = roundedPolygonPath(
            points = visibleFace.screenPoints,
            radiusPx = scalePx * DiceCubeCornerRadiusScale,
        )

        drawPath(path, color = tintColor(style.dieBg, brightness), style = Fill)
        drawPath(
            path = path,
            color = style.dieBorder.copy(alpha = (style.dieBorder.alpha + 0.26f).coerceAtMost(0.76f)),
            style = Stroke(width = 1.7.dp.toPx()),
        )

        val activePips = DiceCubePipLayout[visibleFace.face.value].orEmpty()
        activePips.forEach { pipIndex ->
            val uv = DiceCubePipUv[pipIndex - 1]
            drawPath(
                path = projectedPipPath(
                    face = visibleFace.face,
                    uv = uv,
                    rotatePoint = rotatePoint,
                    scalePx = scalePx,
                    centerPx = centerPx,
                ),
                color = style.dot,
                style = Fill,
            )
        }
    }
}

private fun roundedPolygonPath(points: List<Offset>, radiusPx: Float): Path {
    if (points.size < 3) {
        return Path()
    }

    fun pointToward(from: Offset, to: Offset, distancePx: Float): Offset {
        val dx = to.x - from.x
        val dy = to.y - from.y
        val length = sqrt(dx * dx + dy * dy)
        if (length <= 0.001f) return from
        val scale = distancePx / length
        return Offset(from.x + dx * scale, from.y + dy * scale)
    }

    val path = Path()
    points.forEachIndexed { index, current ->
        val previous = points[(index - 1 + points.size) % points.size]
        val next = points[(index + 1) % points.size]
        val previousDistance = distance(current, previous)
        val nextDistance = distance(current, next)
        val cornerRadius = radiusPx
            .coerceAtMost(previousDistance * 0.42f)
            .coerceAtMost(nextDistance * 0.42f)
        val start = pointToward(current, previous, cornerRadius)
        val end = pointToward(current, next, cornerRadius)

        if (index == 0) {
            path.moveTo(start.x, start.y)
        } else {
            path.lineTo(start.x, start.y)
        }
        path.quadraticTo(current.x, current.y, end.x, end.y)
    }
    path.close()
    return path
}

private fun projectedPipPath(
    face: PerspectiveDiceFace,
    uv: Offset,
    rotatePoint: (DiceVec3) -> DiceVec3,
    scalePx: Float,
    centerPx: Offset,
): Path {
    val pipCenter = face.center +
        face.right * ((uv.x - 0.5f) * DiceCubePipSpread) +
        face.up * ((0.5f - uv.y) * DiceCubePipSpread)
    val path = Path()

    for (pointIndex in 0 until DiceCubePipPathSegments) {
        val angle = (2.0 * PI * pointIndex / DiceCubePipPathSegments).toFloat()
        val localPoint = pipCenter +
            face.right * (cos(angle) * DiceCubePipRadius) +
            face.up * (sin(angle) * DiceCubePipRadius)
        val screenPoint = projectPerspectiveDice(
            rotatePoint(localPoint),
            scalePx,
            centerPx,
        )
        if (pointIndex == 0) {
            path.moveTo(screenPoint.x, screenPoint.y)
        } else {
            path.lineTo(screenPoint.x, screenPoint.y)
        }
    }
    path.close()
    return path
}

private fun distance(first: Offset, second: Offset): Float {
    val dx = first.x - second.x
    val dy = first.y - second.y
    return sqrt(dx * dx + dy * dy)
}

private fun rotatePerspectiveDice(v: DiceVec3, yawRad: Float, pitchRad: Float): DiceVec3 =
    rotatePerspectiveDiceX(rotatePerspectiveDiceY(v, yawRad), pitchRad)

private fun rotatePerspectiveDiceY(v: DiceVec3, radians: Float): DiceVec3 {
    val c = cos(radians)
    val s = sin(radians)
    return DiceVec3(v.x * c + v.z * s, v.y, -v.x * s + v.z * c)
}

private fun rotatePerspectiveDiceX(v: DiceVec3, radians: Float): DiceVec3 {
    val c = cos(radians)
    val s = sin(radians)
    return DiceVec3(v.x, v.y * c - v.z * s, v.y * s + v.z * c)
}

private fun projectPerspectiveDice(v: DiceVec3, scalePx: Float, centerPx: Offset): Offset {
    val perspective = DiceCubeCameraDistance / (DiceCubeCameraDistance - v.z).coerceAtLeast(0.15f)
    return Offset(
        x = centerPx.x + v.x * scalePx * perspective,
        y = centerPx.y - v.y * scalePx * perspective,
    )
}

private fun perspectiveDiceFacesForFront(frontValue: Int): List<PerspectiveDiceFace> {
    val front = frontValue.coerceIn(1, 6)
    val back = 7 - front
    val top = (1..6).first { it != front && it != back }
    val bottom = 7 - top
    val right = (1..6).first { it != front && it != back && it != top && it != bottom }
    val left = 7 - right

    return listOf(
        PerspectiveDiceFace(front, center = DiceVec3(0f, 0f, 1f), right = DiceVec3(1f, 0f, 0f), up = DiceVec3(0f, 1f, 0f)),
        PerspectiveDiceFace(back, center = DiceVec3(0f, 0f, -1f), right = DiceVec3(-1f, 0f, 0f), up = DiceVec3(0f, 1f, 0f)),
        PerspectiveDiceFace(right, center = DiceVec3(1f, 0f, 0f), right = DiceVec3(0f, 0f, -1f), up = DiceVec3(0f, 1f, 0f)),
        PerspectiveDiceFace(left, center = DiceVec3(-1f, 0f, 0f), right = DiceVec3(0f, 0f, 1f), up = DiceVec3(0f, 1f, 0f)),
        PerspectiveDiceFace(top, center = DiceVec3(0f, 1f, 0f), right = DiceVec3(1f, 0f, 0f), up = DiceVec3(0f, 0f, -1f)),
        PerspectiveDiceFace(bottom, center = DiceVec3(0f, -1f, 0f), right = DiceVec3(1f, 0f, 0f), up = DiceVec3(0f, 0f, 1f)),
    )
}

private val DiceCubePipUv = listOf(
    Offset(1f / 6f, 1f / 6f),
    Offset(0.5f, 1f / 6f),
    Offset(5f / 6f, 1f / 6f),
    Offset(1f / 6f, 0.5f),
    Offset(0.5f, 0.5f),
    Offset(5f / 6f, 0.5f),
    Offset(1f / 6f, 5f / 6f),
    Offset(0.5f, 5f / 6f),
    Offset(5f / 6f, 5f / 6f),
)

private val DiceCubePipLayout = mapOf(
    1 to listOf(5),
    2 to listOf(1, 9),
    3 to listOf(1, 5, 9),
    4 to listOf(1, 3, 7, 9),
    5 to listOf(1, 3, 5, 7, 9),
    6 to listOf(1, 3, 4, 6, 7, 9),
)

private const val DiceCubeCameraDistance = 3.6f
private const val DiceCubeScale = 0.31f
private const val DiceCubePipRadius = 0.11f
private const val DiceCubePipPathSegments = 18
private const val DiceCubePipSpread = 1.34f
private const val DiceCubeCornerRadiusScale = 0.5f
private const val DiceCubeVisibleFaceEpsilon = 0.001f
private const val DiceYawBaseTurns = 3
private const val DiceYawExtraTurnsExclusive = 2
private const val DicePitchBaseTurns = 2
private const val DicePitchExtraTurnsExclusive = 2
