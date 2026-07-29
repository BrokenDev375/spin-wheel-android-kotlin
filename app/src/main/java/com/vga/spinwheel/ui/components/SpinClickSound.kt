package com.vga.spinwheel.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
