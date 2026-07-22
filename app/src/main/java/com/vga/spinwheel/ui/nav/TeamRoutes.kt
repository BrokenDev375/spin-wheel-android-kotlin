package com.vga.spinwheel.ui.nav

object TeamRoutes {
    const val ARG_LIST_ID = "listId"

    const val HOME = "team"
    const val ADD = "team/add"
    const val EDIT = "team/{$ARG_LIST_ID}/edit"
    const val DETAIL = "team/{$ARG_LIST_ID}"
    const val SETTINGS = "team/{$ARG_LIST_ID}/settings"
    const val PREVIEW = "team/{$ARG_LIST_ID}/preview"

    fun edit(listId: String): String = "team/$listId/edit"

    fun detail(listId: String): String = "team/$listId"

    fun settings(listId: String): String = "team/$listId/settings"

    fun preview(listId: String): String = "team/$listId/preview"
}
