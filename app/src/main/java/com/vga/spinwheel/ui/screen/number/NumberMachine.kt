package com.vga.spinwheel.ui.screen.number

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.vga.spinwheel.R

private data class BallPosition(
    val xFraction: Float,
    val yFraction: Float,
)

private val ClusteredBallPositions = listOf(
    BallPosition(0.43f, 0.37f),
    BallPosition(0.51f, 0.36f),
    BallPosition(0.59f, 0.38f),
    BallPosition(0.39f, 0.42f),
    BallPosition(0.47f, 0.42f),
    BallPosition(0.55f, 0.43f),
    BallPosition(0.63f, 0.43f),
    BallPosition(0.42f, 0.47f),
    BallPosition(0.50f, 0.47f),
    BallPosition(0.58f, 0.48f),
    BallPosition(0.46f, 0.52f),
    BallPosition(0.54f, 0.52f),
)

private val SpreadBallPositions = listOf(
    BallPosition(0.30f, 0.35f),
    BallPosition(0.42f, 0.33f),
    BallPosition(0.55f, 0.34f),
    BallPosition(0.68f, 0.36f),
    BallPosition(0.35f, 0.42f),
    BallPosition(0.48f, 0.40f),
    BallPosition(0.61f, 0.43f),
    BallPosition(0.72f, 0.47f),
    BallPosition(0.28f, 0.49f),
    BallPosition(0.42f, 0.48f),
    BallPosition(0.53f, 0.51f),
    BallPosition(0.65f, 0.53f),
    BallPosition(0.38f, 0.56f),
    BallPosition(0.50f, 0.57f),
)

@Composable
internal fun NumberMachine(
    modifier: Modifier = Modifier,
    spreadBalls: Boolean = false,
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(NumberMachineAspectRatio),
    ) {
        Image(
            painter = painterResource(R.drawable.number_machine),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )

        val positions = if (spreadBalls) SpreadBallPositions else ClusteredBallPositions
        val ballSize = maxWidth * 0.082f
        positions.forEach { position ->
            Image(
                painter = painterResource(R.drawable.number_ball),
                contentDescription = null,
                modifier = Modifier
                    .offset(
                        x = maxWidth * position.xFraction - ballSize / 2,
                        y = maxHeight * position.yFraction - ballSize / 2,
                    )
                    .size(ballSize),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

private const val NumberMachineAspectRatio = 964f / 1318f
