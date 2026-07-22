package com.vga.spinwheel.advertisement

import com.vga.spinwheel.firebase.Remote
import kotlin.random.Random
import org.json.JSONArray

data class IntroAdPositions(
    val inlineSlides: Set<Int>,
    val modalAfterSlides: Set<Int>,
) {
    fun hasInline(slideNumber: Int): Boolean = slideNumber in inlineSlides
    fun hasModalAfter(slideNumber: Int): Boolean = slideNumber in modalAfterSlides
}

object AdPositions {

    fun current(random: Random = Random.Default): IntroAdPositions {
        val raw = Remote.instance.getString(Remote.KEY_POSITION_INTRO)
        return parsePositions(raw, random)
    }

    fun parsePositions(
        raw: String,
        random: Random = Random.Default,
    ): IntroAdPositions {
        if (raw.isBlank()) return IntroAdPositions(emptySet(), emptySet())

        val variants = runCatching { variantsFromJson(raw) }.getOrDefault(emptyList())
        if (variants.isEmpty()) return IntroAdPositions(emptySet(), emptySet())

        val values = variants[random.nextInt(variants.size)]
        val inlineSlides = values
            .filter { it in 1..9 }
            .toSet()
        val modalAfterSlides = values
            .filter { it >= 11 && it % 11 == 0 }
            .map { it / 11 }
            .filter { it in 1..9 }
            .toSet()
        return IntroAdPositions(inlineSlides, modalAfterSlides)
    }

    private fun variantsFromJson(raw: String): List<List<Int>> {
        val root = JSONArray(raw)
        if (root.length() == 0) return emptyList()

        return if (root.opt(0) is JSONArray) {
            (0 until root.length()).map { index ->
                root.getJSONArray(index).toIntList()
            }
        } else {
            listOf(root.toIntList())
        }
    }

    private fun JSONArray.toIntList(): List<Int> =
        (0 until length()).mapNotNull { index ->
            runCatching { getInt(index) }.getOrNull()
        }
}
