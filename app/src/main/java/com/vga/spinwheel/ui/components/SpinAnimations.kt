package com.vga.spinwheel.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset

object SpinAnimations {
    val ScreenFade = tween<Float>(durationMillis = 220)
    val CardPress = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )
    val SlideIn = tween<IntOffset>(durationMillis = 260)
}
