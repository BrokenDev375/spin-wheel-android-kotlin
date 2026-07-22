package com.vga.spinwheel.ui.nav

object DrawingRoutes {
    const val HOME = "drawing_home"
    const val ADD = "drawing_add"
    const val EDIT = "drawing_edit/{wheelId}"
    const val AI_FORM = "drawing_ai_form"
    const val SPIN = "drawing_spin/{wheelId}"
    const val RESULT = "drawing_result/{wheelId}"
    const val SETTINGS = "drawing_settings/{wheelId}"
    const val PALETTE = "drawing_palette/{wheelId}"

    const val ARG_WHEEL_ID = "wheelId"

    fun edit(wheelId: String) = "drawing_edit/$wheelId"
    fun spin(wheelId: String) = "drawing_spin/$wheelId"
    fun result(wheelId: String) = "drawing_result/$wheelId"
    fun settings(wheelId: String) = "drawing_settings/$wheelId"
    fun palette(wheelId: String) = "drawing_palette/$wheelId"
}
