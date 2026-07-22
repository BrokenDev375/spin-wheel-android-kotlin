package com.vga.spinwheel.ui.nav

object WheelRoutes {
    const val ARG_WHEEL_ID = "wheelId"
    const val ARG_RESULT_ID = "resultId"

    const val HOME = "wheel"
    const val ADD = "wheel/add"
    const val EDIT = "wheel/{$ARG_WHEEL_ID}/edit"
    const val SPIN = "wheel/{$ARG_WHEEL_ID}/spin"
    const val SETTINGS = "wheel/{$ARG_WHEEL_ID}/settings"
    const val RESULT = "wheel/{$ARG_WHEEL_ID}/result/{$ARG_RESULT_ID}"
    const val HISTORY = "wheel/{$ARG_WHEEL_ID}/history"
    const val PALETTE = "wheel/{$ARG_WHEEL_ID}/palette"

    fun edit(wheelId: String): String = "wheel/$wheelId/edit"

    fun spin(wheelId: String): String = "wheel/$wheelId/spin"

    fun settings(wheelId: String): String = "wheel/$wheelId/settings"

    fun result(wheelId: String, resultId: String): String = "wheel/$wheelId/result/$resultId"

    fun history(wheelId: String): String = "wheel/$wheelId/history"

    fun palette(wheelId: String): String = "wheel/$wheelId/palette"
}
