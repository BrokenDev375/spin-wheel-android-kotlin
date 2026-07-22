package com.vga.spinwheel.data.model

enum class RandomFeature(
    val storageKey: String,
) {
    APP("app"),
    WHEEL("wheel"),
    FINGER("finger"),
    COIN("coin"),
    TEAM("team"),
    NUMBER("number"),
    DRAWING("drawing"),
    BOTTLE("bottle"),
    DICE("dice"),
    CARD("card");

    companion object {
        fun fromStorageKey(value: String): RandomFeature =
            entries.firstOrNull { it.storageKey == value } ?: APP
    }
}
