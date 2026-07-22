package com.vga.spinwheel.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        WheelEntity::class,
        WheelItemEntity::class,
        RandomHistoryEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class SpinWheelDatabase : RoomDatabase() {

    abstract fun wheelDao(): WheelDao

    abstract fun randomHistoryDao(): RandomHistoryDao

    companion object {
        const val DATABASE_NAME = "spin_wheel.db"
    }
}
