package com.vga.spinwheel.audio

interface GameSoundPlayer {
    fun playButtonClick()

    fun playResult()

    fun startDiceRoll()

    fun stopDiceRoll()

    fun startNumberRoll()

    fun stopNumberRoll()

    fun startBottleSpin()

    fun stopBottleSpin()

    fun startCoinFlip()

    fun stopCoinFlip()

    fun startCardShuffle()

    fun stopCardShuffle()

    fun playCardFlip()

    fun startWheelSpin()

    fun stopWheelSpin()
}
