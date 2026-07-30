package com.vga.spinwheel.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.vga.spinwheel.ui.audio.rememberGameSoundPlayer

@Composable
fun rememberClickWithSound(onClick: () -> Unit): () -> Unit {
    val gameSoundPlayer = rememberGameSoundPlayer()
    return remember(gameSoundPlayer, onClick) {
        {
            gameSoundPlayer.playButtonClick()
            onClick()
        }
    }
}

@Composable
fun <T> rememberValueChangeWithSound(onValueChange: (T) -> Unit): (T) -> Unit {
    val gameSoundPlayer = rememberGameSoundPlayer()
    return remember(gameSoundPlayer, onValueChange) {
        { value: T ->
            gameSoundPlayer.playButtonClick()
            onValueChange(value)
        }
    }
}

@Composable
fun Modifier.clickableWithSound(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val clickWithSound = rememberClickWithSound(onClick)
    return clickable(enabled = enabled, onClick = clickWithSound)
}

@Composable
fun Modifier.softClickableWithSound(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (enabled && pressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "soft-click-scale",
    )
    val clickWithSound = rememberClickWithSound(onClick)

    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        onClick = clickWithSound,
    )
}
