package com.vga.spinwheel.data.db

import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.model.RandomResult
import com.vga.spinwheel.data.model.Wheel
import com.vga.spinwheel.data.model.WheelItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelMappersTest {

    @Test
    fun wheelWithItemsToModelKeepsStoredItemOrder() {
        val model = WheelWithItems(
            wheel = WheelEntity(
                id = "wheel-1",
                name = "Lunch",
                createdAt = 10L,
                updatedAt = 20L,
            ),
            items = listOf(
                WheelItemEntity(
                    id = "item-2",
                    wheelId = "wheel-1",
                    name = "Soup",
                    priority = 1,
                    sortOrder = 1,
                ),
                WheelItemEntity(
                    id = "item-1",
                    wheelId = "wheel-1",
                    name = "Rice",
                    priority = 2,
                    sortOrder = 0,
                ),
            ),
        ).toModel()

        assertEquals("Lunch", model.name)
        assertEquals(listOf("Rice", "Soup"), model.items.map { it.name })
        assertEquals(listOf(2, 1), model.items.map { it.priority })
    }

    @Test
    fun wheelToEntitiesKeepsIdsAndSortOrder() {
        val wheel = Wheel(
            id = "wheel-1",
            name = "Names",
            items = listOf(
                WheelItem(id = "a", name = "A", priority = 1),
                WheelItem(id = "b", name = "B", priority = 3),
            ),
            createdAt = 1L,
            updatedAt = 2L,
        )

        val entities = wheel.toItemEntities()

        assertEquals(listOf("a", "b"), entities.map { it.id })
        assertEquals(listOf(0, 1), entities.map { it.sortOrder })
        assertEquals(listOf(1, 3), entities.map { it.priority })
    }

    @Test
    fun randomHistoryEntityMapsFeatureKey() {
        val result = RandomResult(
            id = "history-1",
            feature = RandomFeature.NUMBER,
            title = "Number",
            value = "42",
            sourceId = "number-setting",
            payload = "payload",
            createdAt = 99L,
        )

        val entity = result.toEntity()
        val mapped = entity.toModel()

        assertEquals("number", entity.feature)
        assertEquals(RandomFeature.NUMBER, mapped.feature)
        assertEquals("42", mapped.value)
    }
}
