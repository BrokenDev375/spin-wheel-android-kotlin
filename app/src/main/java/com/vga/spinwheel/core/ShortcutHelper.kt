package com.vga.spinwheel.core

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.vga.spinwheel.R

/**
 * ShortcutManager dynamic shortcut helper.
 * Tạo dynamic shortcut "Uninstall" trên launcher khi vào ứng dụng.
 * MainActivity đọc extra [EXTRA_SHORTCUT_ID] qua [consumeShortcutId] để xử lý.
 */
object ShortcutHelper {

    const val EXTRA_SHORTCUT_ID = "shortcutId"
    const val SHORTCUT_UNINSTALL = "uninstall"

    /** Tạo (hoặc cập nhật) shortcut "Uninstall" trên launcher. Gọi 1 lần khi vào Home. */
    fun addUninstallShortcut(context: Context, label: String = "Uninstall") {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(EXTRA_SHORTCUT_ID, SHORTCUT_UNINSTALL)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val shortcut = ShortcutInfoCompat.Builder(context, SHORTCUT_UNINSTALL)
            .setShortLabel(label)
            .setLongLabel(label)
            .setIcon(IconCompat.createWithResource(context, R.drawable.icon_app))
            .setIntent(intent)
            .build()
        runCatching { ShortcutManagerCompat.pushDynamicShortcut(context, shortcut) }
    }

    fun removeAll(context: Context) {
        runCatching { ShortcutManagerCompat.removeAllDynamicShortcuts(context) }
    }

    /** Đọc shortcutId từ intent khởi chạy (gọi trong MainActivity.onCreate/onNewIntent). */
    fun consumeShortcutId(intent: Intent?): String? = intent?.getStringExtra(EXTRA_SHORTCUT_ID)
}
