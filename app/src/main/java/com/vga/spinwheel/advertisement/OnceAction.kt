package com.vga.spinwheel.advertisement

import java.util.concurrent.atomic.AtomicBoolean

class OnceAction(
    private val action: () -> Unit,
) {
    private val consumed = AtomicBoolean(false)

    val hasRun: Boolean
        get() = consumed.get()

    fun run() {
        if (consumed.compareAndSet(false, true)) {
            action()
        }
    }
}
