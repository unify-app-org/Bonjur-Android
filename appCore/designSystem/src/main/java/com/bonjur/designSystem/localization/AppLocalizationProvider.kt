package com.bonjur.designSystem.localization

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Wraps the app root so every `stringResource` below it resolves against the
 * language picked in [LanguageManager] instead of the device language.
 *
 * Live switching without a restart: [LanguageManager.language] is Compose state,
 * so changing it rebuilds the localized `Context` and recomposes the whole tree —
 * the Android counterpart of iOS's `.localized()` + `.id(languageCode)` rebuild.
 */
@Composable
fun AppLocalizationProvider(content: @Composable () -> Unit) {
    val language = LanguageManager.language
    val context = LocalContext.current

    val localizedContext = remember(language, context) {
        LocalizedContextWrapper(context, Locale(language.code))
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedContext.resources.configuration
    ) {
        content()
    }
}

/**
 * Swaps only the resource lookup, keeping the original context (the Activity) as
 * the base. `createConfigurationContext` alone returns a bare `ContextImpl`, which
 * hides the Activity from anything that unwraps `LocalContext` to find it — Hilt's
 * `hiltViewModel()` crashes with "Expected an activity context".
 */
private class LocalizedContextWrapper(
    base: Context,
    locale: Locale
) : ContextWrapper(base) {

    private val localized: Context = base.createConfigurationContext(
        Configuration(base.resources.configuration).apply {
            setLocales(LocaleList(locale))
            setLayoutDirection(locale)
        }
    )

    override fun getResources(): Resources = localized.resources

    override fun getAssets(): AssetManager = localized.assets

    override fun getTheme(): Resources.Theme = localized.theme
}
