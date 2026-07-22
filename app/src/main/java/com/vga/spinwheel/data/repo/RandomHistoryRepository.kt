package com.vga.spinwheel.data.repo

import com.vga.spinwheel.core.di.AppDispatchers
import com.vga.spinwheel.data.db.RandomHistoryDao
import com.vga.spinwheel.data.db.toEntity
import com.vga.spinwheel.data.db.toModel
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.RandomResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class RandomHistoryRepository @Inject constructor(
    private val historyDao: RandomHistoryDao,
    private val dispatchers: AppDispatchers,
) {

    fun observeHistory(
        feature: RandomFeature,
        limit: Int = DEFAULT_LIMIT,
    ): Flow<List<RandomResult>> = historyDao.observeHistory(feature.storageKey, limit)
        .map { rows -> rows.map { it.toModel() } }
        .flowOn(dispatchers.io)

    suspend fun getHistory(
        feature: RandomFeature,
        limit: Int = DEFAULT_LIMIT,
    ): List<RandomResult> = withContext(dispatchers.io) {
        historyDao.getHistory(feature.storageKey, limit).map { it.toModel() }
    }

    suspend fun addResult(
        feature: RandomFeature,
        title: String,
        value: String,
        sourceId: String? = null,
        payload: String? = null,
    ): RandomResult = withContext(dispatchers.io) {
        val result = RandomResult(
            id = UUID.randomUUID().toString(),
            feature = feature,
            sourceId = sourceId,
            title = title.trim(),
            value = value.trim(),
            payload = payload,
            createdAt = System.currentTimeMillis(),
        ).requireValid()

        historyDao.upsert(result.toEntity())
        result
    }

    suspend fun upsertResult(result: RandomResult): RandomResult = withContext(dispatchers.io) {
        val normalized = result.copy(
            title = result.title.trim(),
            value = result.value.trim(),
        ).requireValid()

        historyDao.upsert(normalized.toEntity())
        normalized
    }

    suspend fun deleteResult(id: String) = withContext(dispatchers.io) {
        historyDao.delete(id)
    }

    suspend fun clearFeature(feature: RandomFeature) = withContext(dispatchers.io) {
        historyDao.clearFeature(feature.storageKey)
    }

    suspend fun clearAll() = withContext(dispatchers.io) {
        historyDao.clearAll()
    }

    private fun RandomResult.requireValid(): RandomResult {
        require(title.isNotBlank()) { "History title must not be blank." }
        require(value.isNotBlank()) { "History value must not be blank." }
        return this
    }

    private companion object {
        const val DEFAULT_LIMIT = 50
    }
}
