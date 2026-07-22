package com.vga.spinwheel.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomHistoryDao {

    @Query(
        """
        SELECT * FROM random_history
        WHERE feature = :feature
        ORDER BY createdAt DESC
        LIMIT :limit
        """
    )
    fun observeHistory(feature: String, limit: Int): Flow<List<RandomHistoryEntity>>

    @Query(
        """
        SELECT * FROM random_history
        WHERE feature = :feature
        ORDER BY createdAt DESC
        LIMIT :limit
        """
    )
    suspend fun getHistory(feature: String, limit: Int): List<RandomHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(result: RandomHistoryEntity)

    @Query("DELETE FROM random_history WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM random_history WHERE feature = :feature")
    suspend fun clearFeature(feature: String)

    @Query("DELETE FROM random_history")
    suspend fun clearAll()
}
