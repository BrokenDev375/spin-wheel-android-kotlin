package com.vga.spinwheel.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.vga.spinwheel.R
import com.vga.spinwheel.data.model.AppSettingKeys
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidGameSoundPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) : GameSoundPlayer {

    private val stateLock = Any()
    private val loadedSoundIds = mutableSetOf<Int>()
    private val pendingOneShots = mutableMapOf<Int, Float>()

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_STREAMS)
        .setAudioAttributes(audioAttributes)
        .build()

    private var buttonClickSoundId = 0
    private var resultSoundId = 0
    private var cardFlipSoundId = 0
    private var wheelSpinSoundId = 0
    private var wheelSpinStreamId = 0
    private var diceRollPlayer: MediaPlayer? = null
    private var numberRollPlayer: MediaPlayer? = null
    private var bottleSpinPlayer: MediaPlayer? = null
    private var coinFlipPlayer: MediaPlayer? = null
    private var cardShufflePlayer: MediaPlayer? = null
    private var wheelSpinRequested = false

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status != LOAD_SUCCESS) return@setOnLoadCompleteListener

            synchronized(stateLock) {
                loadedSoundIds += sampleId

                pendingOneShots.remove(sampleId)?.let { volume ->
                    if (isGameSoundEnabled()) {
                        soundPool.play(sampleId, volume, volume, PRIORITY_NORMAL, NO_LOOP, PLAYBACK_RATE)
                    }
                }

                if (
                    sampleId == wheelSpinSoundId &&
                    wheelSpinRequested &&
                    isGameSoundEnabled()
                ) {
                    playWheelSpinLocked()
                }
            }
        }

        buttonClickSoundId = soundPool.load(context, R.raw.button_click, PRIORITY_NORMAL)
        resultSoundId = soundPool.load(context, R.raw.result_win, PRIORITY_NORMAL)
        cardFlipSoundId = soundPool.load(context, R.raw.card_flip, PRIORITY_NORMAL)
        wheelSpinSoundId = soundPool.load(context, R.raw.wheel_spin_loop, PRIORITY_NORMAL)
    }

    override fun playButtonClick() {
        playOneShot(buttonClickSoundId, BUTTON_VOLUME)
    }

    override fun playResult() {
        playOneShot(resultSoundId, RESULT_VOLUME)
    }

    override fun startDiceRoll() {
        synchronized(stateLock) {
            diceRollPlayer = startActionPlayerLocked(
                currentPlayer = diceRollPlayer,
                rawResId = R.raw.dice,
                volume = DICE_VOLUME,
            )
        }
    }

    override fun stopDiceRoll() {
        synchronized(stateLock) {
            diceRollPlayer = stopActionPlayerLocked(diceRollPlayer)
        }
    }

    override fun startNumberRoll() {
        synchronized(stateLock) {
            numberRollPlayer = startActionPlayerLocked(
                currentPlayer = numberRollPlayer,
                rawResId = resolveNumberRollRawResId(),
                volume = NUMBER_VOLUME,
            )
        }
    }

    override fun stopNumberRoll() {
        synchronized(stateLock) {
            numberRollPlayer = stopActionPlayerLocked(numberRollPlayer)
        }
    }

    override fun startBottleSpin() {
        synchronized(stateLock) {
            bottleSpinPlayer = startActionPlayerLocked(
                currentPlayer = bottleSpinPlayer,
                rawResId = R.raw.bottle_spin,
                volume = BOTTLE_VOLUME,
            )
        }
    }

    override fun stopBottleSpin() {
        synchronized(stateLock) {
            bottleSpinPlayer = stopActionPlayerLocked(bottleSpinPlayer)
        }
    }

    override fun startCoinFlip() {
        synchronized(stateLock) {
            coinFlipPlayer = startActionPlayerLocked(
                currentPlayer = coinFlipPlayer,
                rawResId = R.raw.coin_flip,
                volume = COIN_VOLUME,
            )
        }
    }

    override fun stopCoinFlip() {
        synchronized(stateLock) {
            coinFlipPlayer = stopActionPlayerLocked(coinFlipPlayer)
        }
    }

    override fun startCardShuffle() {
        synchronized(stateLock) {
            cardShufflePlayer = startActionPlayerLocked(
                currentPlayer = cardShufflePlayer,
                rawResId = R.raw.card_shuffle,
                volume = CARD_SHUFFLE_VOLUME,
            )
        }
    }

    override fun stopCardShuffle() {
        synchronized(stateLock) {
            cardShufflePlayer = stopActionPlayerLocked(cardShufflePlayer)
        }
    }

    override fun playCardFlip() {
        playOneShot(cardFlipSoundId, CARD_FLIP_VOLUME)
    }

    override fun startWheelSpin() {
        synchronized(stateLock) {
            stopWheelSpinLocked()
            wheelSpinRequested = isGameSoundEnabled()
            if (wheelSpinRequested && wheelSpinSoundId in loadedSoundIds) {
                playWheelSpinLocked()
            }
        }
    }

    override fun stopWheelSpin() {
        synchronized(stateLock) {
            wheelSpinRequested = false
            stopWheelSpinLocked()
        }
    }

    private fun playOneShot(soundId: Int, volume: Float) {
        if (!isGameSoundEnabled()) return

        synchronized(stateLock) {
            if (soundId in loadedSoundIds) {
                soundPool.play(soundId, volume, volume, PRIORITY_NORMAL, NO_LOOP, PLAYBACK_RATE)
            } else {
                pendingOneShots[soundId] = volume
            }
        }
    }

    private fun playWheelSpinLocked() {
        stopWheelSpinLocked()
        wheelSpinStreamId = playSoundLocked(wheelSpinSoundId, WHEEL_VOLUME, LOOP_FOREVER)
    }

    private fun stopWheelSpinLocked() {
        wheelSpinStreamId = stopStreamLocked(wheelSpinStreamId)
    }

    private fun playSoundLocked(soundId: Int, volume: Float, loop: Int): Int =
        soundPool.play(
            soundId,
            volume,
            volume,
            PRIORITY_NORMAL,
            loop,
            PLAYBACK_RATE,
        )

    private fun startActionPlayerLocked(
        currentPlayer: MediaPlayer?,
        rawResId: Int,
        volume: Float,
    ): MediaPlayer? {
        stopActionPlayerLocked(currentPlayer)
        if (!isGameSoundEnabled()) return null

        var player: MediaPlayer? = null
        return try {
            val assetFileDescriptor = context.resources.openRawResourceFd(rawResId)
            assetFileDescriptor.use { descriptor ->
                player = MediaPlayer().apply {
                    setAudioAttributes(audioAttributes)
                    setDataSource(
                        descriptor.fileDescriptor,
                        descriptor.startOffset,
                        descriptor.length,
                    )
                    setVolume(volume, volume)
                    setOnCompletionListener { completedPlayer ->
                        synchronized(stateLock) {
                            clearCompletedActionPlayerLocked(completedPlayer)
                        }
                        completedPlayer.release()
                    }
                    prepare()
                    start()
                }
            }
            player
        } catch (_: IOException) {
            player?.release()
            null
        } catch (_: RuntimeException) {
            player?.release()
            null
        }
    }

    private fun stopActionPlayerLocked(player: MediaPlayer?): MediaPlayer? {
        player ?: return null
        runCatching {
            if (player.isPlaying) {
                player.stop()
            }
        }
        player.release()
        return null
    }

    private fun clearCompletedActionPlayerLocked(player: MediaPlayer) {
        when (player) {
            diceRollPlayer -> diceRollPlayer = null
            numberRollPlayer -> numberRollPlayer = null
            bottleSpinPlayer -> bottleSpinPlayer = null
            coinFlipPlayer -> coinFlipPlayer = null
            cardShufflePlayer -> cardShufflePlayer = null
        }
    }

    private fun resolveNumberRollRawResId(): Int =
        context.resources.getIdentifier(
            NUMBER_ROLL_RESOURCE_NAME,
            RAW_RESOURCE_TYPE,
            context.packageName,
        ).takeIf { it != NO_RESOURCE } ?: R.raw.dice

    private fun stopStreamLocked(streamId: Int): Int {
        if (streamId != NO_STREAM) {
            soundPool.stop(streamId)
        }
        return NO_STREAM
    }

    private fun isGameSoundEnabled(): Boolean = settingsRepository.getBoolean(
        RandomFeature.APP,
        AppSettingKeys.GAME_SOUND_ENABLED,
        true,
    )

    private companion object {
        const val MAX_STREAMS = 3
        const val LOAD_SUCCESS = 0
        const val PRIORITY_NORMAL = 1
        const val NO_LOOP = 0
        const val LOOP_FOREVER = -1
        const val NO_RESOURCE = 0
        const val NO_STREAM = 0
        const val PLAYBACK_RATE = 1f
        const val BUTTON_VOLUME = 0.55f
        const val RESULT_VOLUME = 0.8f
        const val DICE_VOLUME = 1f
        const val NUMBER_VOLUME = 0.9f
        const val BOTTLE_VOLUME = 1f
        const val COIN_VOLUME = 0.95f
        const val CARD_SHUFFLE_VOLUME = 0.9f
        const val CARD_FLIP_VOLUME = 0.8f
        const val WHEEL_VOLUME = 0.65f
        const val NUMBER_ROLL_RESOURCE_NAME = "number_roll"
        const val RAW_RESOURCE_TYPE = "raw"
    }
}
