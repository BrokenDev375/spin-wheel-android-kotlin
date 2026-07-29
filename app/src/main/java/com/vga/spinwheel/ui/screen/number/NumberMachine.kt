package com.vga.spinwheel.ui.screen.number

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import kotlin.math.sqrt
import kotlin.random.Random
import kotlinx.coroutines.delay

private class BallPhysicsState(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var rotation: Float,
    var vRotation: Float,
    var number: Int,
    var isDropping: Boolean = false,
    var isDroppedOut: Boolean = false
)

@Composable
internal fun NumberMachine(
    modifier: Modifier = Modifier,
    isSpinning: Boolean = false,
    spreadBalls: Boolean = false,
    isDropped: Boolean = false,
    minNumber: Int = 1,
    maxNumber: Int = 10,
    droppedNumbers: List<Int> = emptyList(),
) {
    val ballStates = remember(minNumber, maxNumber) {
        val safeMin = minOf(minNumber, maxNumber)
        val safeMax = maxOf(minNumber, maxNumber)
        val states = List(14) {
            BallPhysicsState(
                x = 0.5f + (Random.nextFloat() - 0.5f) * 0.2f,
                y = 0.5f + Random.nextFloat() * 0.1f, // start near bottom
                vx = 0f,
                vy = 0f,
                rotation = Random.nextFloat() * 360f,
                vRotation = 0f,
                number = if (safeMin == safeMax) safeMin else Random.nextInt(safeMin, safeMax + 1)
            )
        }
        
        // Pre-warm physics so balls are already stacked at the bottom
        val dt = 0.016f
        val cx = 0.5f
        val cy = 0.45f
        val boundaryRadius = 0.19f
        for (step in 0..100) {
            // Ball-to-ball repulsion
            for (i in 0 until states.size) {
                val b1 = states[i]
                for (j in i + 1 until states.size) {
                    val b2 = states[j]
                    val dx = b1.x - b2.x
                    val dy = b1.y - b2.y
                    val dist = sqrt(dx*dx + dy*dy)
                    val minDist = 0.082f
                    if (dist < minDist && dist > 0.001f) {
                        val overlap = minDist - dist
                        val nx = dx / dist
                        val ny = dy / dist
                        b1.vx += nx * overlap * 10f * dt
                        b1.vy += ny * overlap * 10f * dt
                        b2.vx -= nx * overlap * 10f * dt
                        b2.vy -= ny * overlap * 10f * dt
                    }
                }
            }
            // Update
            states.forEach { ball ->
                ball.vy += 4f * dt // gravity
                ball.vx *= 0.85f
                ball.vy *= 0.85f
                ball.x += ball.vx * dt
                ball.y += ball.vy * dt
                
                // Boundary
                val dx = ball.x - cx
                val dy = ball.y - cy
                val dist = sqrt(dx*dx + dy*dy)
                if (dist > boundaryRadius) {
                    val nx = dx / dist
                    val ny = dy / dist
                    ball.x = cx + nx * boundaryRadius
                    ball.y = cy + ny * boundaryRadius
                }
            }
        }
        states
    }

    var tick by remember { mutableStateOf(0) }

    LaunchedEffect(droppedNumbers) {
        if (droppedNumbers.isNotEmpty()) {
            val available = ballStates.filter { !it.isDropping && !it.isDroppedOut }.shuffled()
            val toDrop = available.take(droppedNumbers.size)
            toDrop.forEachIndexed { i, ball ->
                if (i > 0) {
                    delay(500L) // Giãn cách 500ms cho các quả bóng tiếp theo
                }
                ball.number = droppedNumbers[i]
                ball.isDropping = true
            }
        } else {
            ballStates.forEach { 
                it.isDropping = false
                it.isDroppedOut = false
            }
        }
    }

    LaunchedEffect(isSpinning, droppedNumbers) {
        val cx = 0.5f
        val cy = 0.45f
        val boundaryRadius = 0.19f
        var lastFrameTime = withFrameNanos { it }

        while (true) {
            val frameTime = withFrameNanos { it }
            val dt = ((frameTime - lastFrameTime) / 1_000_000_000f).coerceAtMost(0.05f)
            lastFrameTime = frameTime

            var moved = false
            
            // Ball-to-ball repulsion
            for (i in 0 until ballStates.size) {
                val b1 = ballStates[i]
                if (b1.isDroppedOut) continue
                for (j in i + 1 until ballStates.size) {
                    val b2 = ballStates[j]
                    if (b2.isDroppedOut) continue
                    val dx = b1.x - b2.x
                    val dy = b1.y - b2.y
                    val dist = sqrt(dx*dx + dy*dy)
                    val minDist = 0.082f
                    if (dist < minDist && dist > 0.001f) {
                        val overlap = minDist - dist
                        val nx = dx / dist
                        val ny = dy / dist
                        val pushX = nx * overlap * 30f * dt
                        val pushY = ny * overlap * 30f * dt
                        b1.vx += pushX
                        b1.vy += pushY
                        b2.vx -= pushX
                        b2.vy -= pushY
                    }
                }
            }

            ballStates.forEach { ball ->
                if (ball.isDroppedOut) return@forEach
                moved = true

                if (isSpinning) {
                    ball.vx += (Random.nextFloat() - 0.5f) * 10f * dt
                    ball.vy += (Random.nextFloat() - 0.5f) * 10f * dt
                    
                    val dx = cx - ball.x
                    val dy = cy - ball.y
                    ball.vx += dx * 5f * dt
                    ball.vy += dy * 5f * dt
                    
                    val speed = sqrt(ball.vx * ball.vx + ball.vy * ball.vy)
                    if (speed < 0.6f) {
                        ball.vx *= 1.2f
                        ball.vy *= 1.2f
                    } else if (speed > 1.8f) {
                        ball.vx *= 0.9f
                        ball.vy *= 0.9f
                    }
                    ball.vRotation = (ball.vx + ball.vy) * 200f
                } else if (ball.isDropping) {
                    // Drop out of the pipe at the bottom (y = 1.0f)
                    val hx = 0.5f
                    val dx = hx - ball.x
                    
                    ball.vx += dx * 15f * dt // strong pull to center
                    ball.vx *= 0.6f // horizontal friction
                    
                    ball.vy = 1.8f // Tốc độ trượt rơi ống CỐ ĐỊNH (nhanh hơn để rớt ra trước khi bảng kết quả hiện xong)
                    
                    if (ball.y > 0.96f) { // bottom of the machine image
                        ball.isDroppedOut = true
                    }
                } else {
                    ball.vy += 4f * dt // gravity
                    ball.vx *= 0.95f
                    ball.vy *= 0.95f
                    ball.vRotation *= 0.95f
                }

                ball.x += ball.vx * dt
                ball.y += ball.vy * dt
                ball.rotation += ball.vRotation * dt

                // Boundary collision
                if (!ball.isDropping) {
                    val dx = ball.x - cx
                    val dy = ball.y - cy
                    val dist = sqrt(dx*dx + dy*dy)
                    if (dist > boundaryRadius) {
                        val nx = dx / dist
                        val ny = dy / dist
                        val dot = ball.vx * nx + ball.vy * ny
                        if (dot > 0) {
                            ball.vx -= 2 * dot * nx
                            ball.vy -= 2 * dot * ny
                            ball.vx *= 0.8f
                            ball.vy *= 0.8f
                        }
                        ball.x = cx + nx * boundaryRadius
                        ball.y = cy + ny * boundaryRadius
                    }
                }
            }
            if (moved || isSpinning) {
                tick++
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier.aspectRatio(NumberMachineAspectRatio).clipToBounds(),
    ) {
        val _trigger = tick 
        Image(
            painter = painterResource(R.drawable.number_machine),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )

        val ballSize = maxWidth * 0.082f

        ballStates.forEach { ball ->
            if (!ball.isDroppedOut) {
                NumberBall(
                    number = ball.number.toString(),
                    size = ballSize,
                    modifier = Modifier
                        .offset(
                            x = maxWidth * ball.x - ballSize / 2,
                            y = maxHeight * ball.y - ballSize / 2,
                        )
                        .graphicsLayer {
                            rotationZ = ball.rotation
                        }
                )
            }
        }
    }
}

private const val NumberMachineAspectRatio = 964f / 1318f
