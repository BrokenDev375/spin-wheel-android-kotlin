package com.vga.spinwheel.advertisement

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnceActionTest {

    @Test
    fun runInvokesCallbackExactlyOnce() {
        var calls = 0
        val action = OnceAction { calls++ }

        action.run()
        action.run()
        action.run()

        assertEquals(1, calls)
        assertTrue(action.hasRun)
    }
}
