package com.vga.spinwheel.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WheelDao {

    @Transaction
    @Query("SELECT * FROM wheels ORDER BY updatedAt DESC")
    abstract fun observeWheels(): Flow<List<WheelWithItems>>

    @Transaction
    @Query("SELECT * FROM wheels ORDER BY updatedAt DESC")
    abstract suspend fun getWheels(): List<WheelWithItems>

    @Transaction
    @Query("SELECT * FROM wheels WHERE id = :id LIMIT 1")
    abstract suspend fun getWheel(id: String): WheelWithItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertWheel(wheel: WheelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItems(items: List<WheelItemEntity>)

    @Query("DELETE FROM wheel_items WHERE wheelId = :wheelId")
    abstract suspend fun deleteItemsForWheel(wheelId: String)

    @Query("DELETE FROM wheels WHERE id = :id")
    abstract suspend fun deleteWheel(id: String)

    @Transaction
    open suspend fun replaceWheel(wheel: WheelEntity, items: List<WheelItemEntity>) {
        upsertWheel(wheel)
        deleteItemsForWheel(wheel.id)
        if (items.isNotEmpty()) {
            insertItems(items)
        }
    }
}
