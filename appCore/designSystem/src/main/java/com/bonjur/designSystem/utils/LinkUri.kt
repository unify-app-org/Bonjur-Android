package com.bonjur.designSystem.utils

import android.net.Uri

private val SCHEME_PREFIX = Regex("^[a-zA-Z][a-zA-Z0-9+.\\-]*:")

/**
 * Turns a user-entered link into something `ACTION_VIEW` can open.
 *
 * The add-link form no longer validates the URL shape — members paste bare handles
 * (`instagram.com/ufaz`), intranet hosts and deep links. A schemeless string has no
 * `ACTION_VIEW` handler, so assume `https://` when no scheme is present.
 * Mirrors iOS `String.browsableURL`.
 */
fun String.asBrowsableUri(): Uri {
    val value = trim()
    return Uri.parse(if (SCHEME_PREFIX.containsMatchIn(value)) value else "https://$value")
}
