package com.vga.spinwheel.core.di

import com.vga.spinwheel.audio.AndroidGameSoundPlayer
import com.vga.spinwheel.audio.GameSoundPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AudioModule {

    @Binds
    @Singleton
    abstract fun bindGameSoundPlayer(
        player: AndroidGameSoundPlayer,
    ): GameSoundPlayer
}
