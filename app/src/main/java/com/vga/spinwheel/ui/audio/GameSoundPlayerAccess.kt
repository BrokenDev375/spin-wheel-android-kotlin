package com.vga.spinwheel.ui.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.vga.spinwheel.audio.GameSoundPlayer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
private interface GameSoundPlayerEntryPoint {
    fun gameSoundPlayer(): GameSoundPlayer
}

private object SilentGameSoundPlayer : GameSoundPlayer {
    override fun playButtonClick() = Unit

    override fun playResult() = Unit

    override fun startDiceRoll() = Unit

    override fun stopDiceRoll() = Unit

    override fun startNumberRoll() = Unit

    override fun stopNumberRoll() = Unit

    override fun startBottleSpin() = Unit

    override fun stopBottleSpin() = Unit

    override fun startCoinFlip() = Unit

    override fun stopCoinFlip() = Unit

    override fun startCardShuffle() = Unit

    override fun stopCardShuffle() = Unit

    override fun playCardFlip() = Unit

    override fun startWheelSpin() = Unit

    override fun stopWheelSpin() = Unit
}

@Composable
fun rememberGameSoundPlayer(): GameSoundPlayer {
    if (LocalInspectionMode.current) return SilentGameSoundPlayer

    val applicationContext = LocalContext.current.applicationContext
    return remember(applicationContext) {
        EntryPointAccessors.fromApplication(
            applicationContext,
            GameSoundPlayerEntryPoint::class.java,
        ).gameSoundPlayer()
    }
}
