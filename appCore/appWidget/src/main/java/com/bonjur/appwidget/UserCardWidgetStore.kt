package com.bonjur.appwidget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/**
 * Storage shared by the app (writer) and the widget (reader).
 *
 * iOS needs an App Group container for this because the widget is a separate
 * process with its own sandbox; on Android the widget runs inside the app's own
 * process, so plain `SharedPreferences` plus a file in `filesDir` is enough.
 *
 * Kept free of Hilt / network / Compose: the widget may be built from
 * `provideGlance` on a cold process where nothing else is initialised.
 */
object UserCardWidgetStore {

    private const val PREF_NAME = "user_card_widget"
    private const val SNAPSHOT_KEY = "user_card_widget_snapshot"
    private const val AVATAR_FILE_NAME = "user-card-avatar.jpg"

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private fun avatarFile(context: Context) =
        File(context.applicationContext.filesDir, AVATAR_FILE_NAME)

    // MARK: - Write (app side)

    fun save(context: Context, snapshot: UserCardWidgetSnapshot) {
        prefs(context).edit().putString(SNAPSHOT_KEY, snapshot.toJson()).apply()
    }

    /**
     * `null` leaves the stored avatar untouched — a profile load that failed to
     * fetch the image should not blank the widget's existing one.
     */
    fun saveAvatar(context: Context, bitmap: Bitmap?) {
        val bitmap = bitmap ?: return
        runCatching {
            avatarFile(context).outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }
        }
    }

    fun clear(context: Context) {
        prefs(context).edit().remove(SNAPSHOT_KEY).apply()
        runCatching { avatarFile(context).delete() }
    }

    // MARK: - Read (widget side)

    fun load(context: Context): UserCardWidgetSnapshot? =
        UserCardWidgetSnapshot.fromJson(prefs(context).getString(SNAPSHOT_KEY, null))

    fun loadAvatar(context: Context): Bitmap? {
        val file = avatarFile(context)
        if (!file.exists()) return null
        return runCatching { BitmapFactory.decodeFile(file.absolutePath) }.getOrNull()
    }
}
