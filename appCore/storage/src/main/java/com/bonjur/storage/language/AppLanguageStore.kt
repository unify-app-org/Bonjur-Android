package com.bonjur.storage.language

import android.content.Context

/**
 * The chosen app-language code, readable from any layer.
 *
 * Lives here rather than next to the UI-facing `LanguageManager` because the
 * network layer needs it too: `Accept-Language` must follow the in-app choice.
 * Reading `Locale.getDefault()` there is not enough — Android re-applies the
 * device configuration (and with it the default locale) whenever an Activity is
 * created, so a cold start silently sent the device language instead.
 */
object AppLanguageStore {

    private const val PREFS = "app_language_prefs"
    private const val KEY_LANGUAGE = "app_language"

    private val supported = setOf("en", "az", "ru")

    /** Used until the user picks a language; the device language is not consulted. */
    const val DEFAULT_CODE = "az"

    @Volatile
    var code: String = DEFAULT_CODE
        private set

    /** Restores the stored choice. Call once from the Application. */
    fun init(context: Context) {
        val stored = prefs(context).getString(KEY_LANGUAGE, null)
        code = normalize(stored ?: DEFAULT_CODE)
    }

    fun save(context: Context, newCode: String) {
        code = normalize(newCode)
        prefs(context).edit().putString(KEY_LANGUAGE, code).apply()
    }

    private fun normalize(raw: String?): String {
        val lowered = raw?.lowercase().orEmpty()
        return if (lowered in supported) lowered else DEFAULT_CODE
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
