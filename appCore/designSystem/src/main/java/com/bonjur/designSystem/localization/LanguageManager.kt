package com.bonjur.designSystem.localization

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bonjur.storage.language.AppLanguageStore
import java.util.Locale

/**
 * App language chosen by the user, independent of the device language.
 *
 * Mirrors iOS `AppLocalization.LanguageManager`: the selection is observable, so
 * changing it re-renders the tree in place — no activity restart, no relaunch.
 * The actual string lookup happens in [AppLocalizationProvider], which swaps the
 * resource `Context` every composable under the app root reads from.
 */
enum class AppLanguage(val code: String, val title: String, val flag: String) {
    // Endonyms on purpose: each language is listed in its own language, so these
    // are NOT resource lookups (and must not be — this enum initializes before
    // LanguageManager has a Context).
    EN("en", "English", "🇬🇧"),
    AZ("az", "Azərbaycan", "🇦🇿"),
    RU("ru", "Русский", "🇷🇺");

    companion object {
        fun from(code: String?): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: EN
    }
}

object LanguageManager {

    private var appContext: Context? = null

    /**
     * Current language. Backed by Compose state, so every `stringResource` under
     * [AppLocalizationProvider] re-reads on change.
     */
    var language by mutableStateOf(AppLanguage.EN)
        private set

    val languageCode: String get() = language.code

    /**
     * Locale for **display** formatting (month names, relative dates), mirroring iOS's
     * `Locale.current` in `Extension+Date`. Parsing keeps a fixed locale: wire formats
     * like `dd-MM-yyyy HH:mm:ss` must not be localized.
     */
    val locale: Locale get() = Locale(language.code)

    /** Called once from the Application; restores the stored choice. */
    fun init(context: Context) {
        appContext = context.applicationContext
        AppLanguageStore.init(context)
        language = AppLanguage.from(AppLanguageStore.code)
        applyDefaultLocale()
    }

    fun select(newLanguage: AppLanguage) {
        if (newLanguage == language) return
        language = newLanguage
        appContext?.let { AppLanguageStore.save(it, newLanguage.code) }
        applyDefaultLocale()
    }

    /**
     * Keeps non-Compose formatting (dates, numbers) on the chosen language too —
     * `stringResource` goes through the provider, but `SimpleDateFormat` and
     * friends read the JVM default.
     */
    private fun applyDefaultLocale() {
        Locale.setDefault(Locale(language.code))
    }

    /**
     * Resource lookup for code that runs outside composition (view models,
     * use cases, snackbars). Composables should use `stringResource`, which goes
     * through [AppLocalizationProvider] and re-reads on a language switch; this
     * resolves once, against the language current at call time.
     */
    fun string(@StringRes id: Int, vararg formatArgs: Any): String {
        val context = appContext ?: return ""
        val localized = localizedContext(context)
        return if (formatArgs.isEmpty()) {
            localized.getString(id)
        } else {
            localized.getString(id, *formatArgs)
        }
    }

    /** Quantity-aware lookup (Russian has three plural forms; Azerbaijani none). */
    fun plural(@PluralsRes id: Int, count: Int): String {
        val context = appContext ?: return ""
        return localizedContext(context).resources.getQuantityString(id, count, count)
    }

    /** [base] re-based on the chosen app language. */
    fun localizedContext(base: Context): Context {
        val locale = Locale(language.code)
        val configuration = Configuration(base.resources.configuration).apply {
            setLocales(LocaleList(locale))
        }
        return base.createConfigurationContext(configuration)
    }

}
