package com.vga.spinwheel.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.vga.spinwheel.R
import com.vga.spinwheel.data.model.AppSettingKeys
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidGameSoundPlayer @Inject constructor(
    @ApplicationContext context: Context,
    private val settingsRepository: SettingsRepository,
) : GameSoundPlayer {

    private val stateLock = Any()
    private val loadedSoundIds = mutableSetOf<Int>()
    private val pendingOneShots = mutableMapOf<Int, Float>()

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_STREAMS)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private var buttonClickSoundId = 0
    private var resultSoundId = 0
    private var wheelSpinSoundId = 0
    private var wheelSpinStreamId = 0
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
        wheelSpinSoundId = soundPool.load(context, R.raw.wheel_spin_loop, PRIORITY_NORMAL)
    }

    override fun playButtonClick() {
        playOneShot(buttonClickSoundId, BUTTON_VOLUME)
    }

    override fun playResult() {
        playOneShot(resultSoundId, RESULT_VOLUME)
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
        wheelSpinStreamId = soundPool.play(
            wheelSpinSoundId,
            WHEEL_VOLUME,
            WHEEL_VOLUME,
            PRIORITY_NORMAL,
            LOOP_FOREVER,
            PLAYBACK_RATE,
        )
    }

    private fun stopWheelSpinLocked() {
        if (wheelSpinStreamId != NO_STREAM) {
            soundPool.stop(wheelSpinStreamId)
            wheelSpinStreamId = NO_STREAM
        }
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
        const val NO_STREAM = 0
        const val PLAYBACK_RATE = 1f
        const val BUTTON_VOLUME = 0.55f
        const val RESULT_VOLUME = 0.8f
        const val WHEEL_VOLUME = 0.65f
    }
}
