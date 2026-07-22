package com.vga.spinwheel.data.repo

import com.vga.spinwheel.core.di.AppDispatchers
import com.vga.spinwheel.data.db.WheelDao
import com.vga.spinwheel.data.db.toEntity
import com.vga.spinwheel.data.db.toItemEntities
import com.vga.spinwheel.data.db.toModel
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class WheelRepository @Inject constructor(
    private val wheelDao: WheelDao,
    private val dispatchers: AppDispatchers,
) {

    fun observeWheels(): Flow<List<Wheel>> = wheelDao.observeWheels()
        .map { wheels -> wheels.map { it.toModel() } }
        .flowOn(dispatchers.io)

    suspend fun getWheels(): List<Wheel> = withContext(dispatchers.io) {
        wheelDao.getWheels().map { it.toModel() }
    }

    suspend fun getWheel(id: String): Wheel? = withContext(dispatchers.io) {
        wheelDao.getWheel(id)?.toModel()
    }

    suspend fun createWheel(
        name: String,
        itemNames: List<String>,
    ): Wheel = withContext(dispatchers.io) {
        val now = System.currentTimeMillis()
        val wheel = Wheel(
            id = newId(),
            name = cleanName(name),
            items = itemNames
                .mapNotNull { itemName -> itemName.toWheelItem() },
            createdAt = now,
            updatedAt = now,
        ).requireValid()

        wheelDao.replaceWheel(wheel.toEntity(), wheel.toItemEntities())
        wheel
    }

    suspend fun upsertWheel(wheel: Wheel): Wheel = withContext(dispatchers.io) {
        val now = System.currentTimeMillis()
        val normalized = wheel.copy(
            name = cleanName(wheel.name),
            items = wheel.items
                .mapNotNull { item -> item.normalized() },
            updatedAt = now,
        ).requireValid()

        wheelDao.replaceWheel(normalized.toEntity(), normalized.toItemEntities())
        normalized
    }

    suspend fun duplicateWheel(
        id: String,
        nameSuffix: String = " Copy",
    ): Wheel? = withContext(dispatchers.io) {
        val source = wheelDao.getWheel(id)?.toModel() ?: return@withContext null
        val now = System.currentTimeMillis()
        val duplicated = Wheel(
            id = newId(),
            name = cleanName(source.name + nameSuffix),
            items = source.items.map { item ->
                item.copy(
                    id = newId(),
                    name = cleanName(item.name),
                    priority = item.priority.coerceAtLeast(MIN_PRIORITY),
                ).normalized() ?: error("Duplicated item must be valid")
            },
            createdAt = now,
            updatedAt = now,
        ).requireValid()

        wheelDao.replaceWheel(duplicated.toEntity(), duplicated.toItemEntities())
        duplicated
    }

    suspend fun deleteWheel(id: String) = withContext(dispatchers.io) {
        wheelDao.deleteWheel(id)
    }

    private fun String.toWheelItem(): WheelItem? {
        val itemName = trim()
        if (itemName.isEmpty()) return null
        return WheelItem(
            id = newId(),
            name = itemName,
            priority = MIN_PRIORITY,
        ).normalized()
    }

    private fun WheelItem.normalized(): WheelItem? {
        val itemName = cleanName(name)
        if (itemName.isEmpty()) return null
        return copy(
            id = id.ifBlank { newId() },
            name = itemName,
            priority = priority.coerceAtLeast(MIN_PRIORITY),
        )
    }

    private fun Wheel.requireValid(): Wheel {
        require(name.isNotBlank()) { "Wheel name must not be blank." }
        require(items.isNotEmpty()) { "Wheel must contain at least one item." }
        return this
    }

    private fun cleanName(value: String): String = value.trim()

    private fun newId(): String = UUID.randomUUID().toString()

    private companion object {
        const val MIN_PRIORITY = 1
    }
}
