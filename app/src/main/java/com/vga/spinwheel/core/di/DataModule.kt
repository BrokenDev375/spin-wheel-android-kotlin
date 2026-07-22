package com.vga.spinwheel.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.vga.spinwheel.data.db.RandomHistoryDao
import com.vga.spinwheel.data.db.SpinWheelDatabase
import com.vga.spinwheel.data.db.WheelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSpinWheelDatabase(
        @ApplicationContext context: Context,
    ): SpinWheelDatabase = Room.databaseBuilder(
        context,
        SpinWheelDatabase::class.java,
        SpinWheelDatabase.DATABASE_NAME,
    ).build()

    @Provides
    fun provideWheelDao(database: SpinWheelDatabase): WheelDao = database.wheelDao()

    @Provides
    fun provideRandomHistoryDao(database: SpinWheelDatabase): RandomHistoryDao =
        database.randomHistoryDao()

    @Provides
    @Singleton
    fun provideSettingsPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(
        SETTINGS_PREFERENCES_NAME,
        Context.MODE_PRIVATE,
    )

    private const val SETTINGS_PREFERENCES_NAME = "spin_wheel_settings"
}
