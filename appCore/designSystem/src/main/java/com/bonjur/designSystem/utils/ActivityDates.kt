package com.bonjur.designSystem.utils

import com.bonjur.designSystem.localization.LanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * The two date shapes a club / event / hangout / community detail screen shows.
 *
 * Every module used to carry its own copy of these — clubs had none at all and printed
 * the raw `dd-MM-yyyy HH:mm:ss` audit stamp, and events/hangouts dropped the comma iOS
 * puts before the time. One home so the four detail screens cannot drift again.
 *
 * Patterns mirror iOS `DateFormat` (`AppCore/AppUtils/Extensions/Extension+Date.swift`):
 * `.dMMMMYYYY` for the audit stamp, `.dMMMMyyyyHHmm` for the activity date.
 *
 * Parsing is always [Locale.US]: wire formats are fixed, and a locale with its own
 * numerals or month names cannot read them. Only the *output* takes the app language.
 */
private const val WIRE_AUDIT = "dd-MM-yyyy HH:mm:ss"
private const val DISPLAY_DATE = "d MMMM yyyy"
private const val DISPLAY_DATE_TIME = "d MMMM yyyy, HH:mm"

private val ISO_PATTERNS = listOf(
    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    "yyyy-MM-dd'T'HH:mm:ss'Z'",
    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
    "yyyy-MM-dd'T'HH:mm:ss"
)

/**
 * `dd-MM-yyyy HH:mm:ss` audit stamp → "2 September 2026".
 *
 * Falls back to the raw value rather than to null: an unparseable stamp is still more
 * useful on screen than an empty row, and that is what iOS's `modifiedDate` does.
 */
fun String?.asActivityAuditDate(): String? {
    val value = this?.trim()?.takeIf { it.isNotBlank() && it != "-" } ?: return null
    val parsed = runCatching {
        SimpleDateFormat(WIRE_AUDIT, Locale.US).parse(value)
    }.getOrNull() ?: parseIsoDate(value) ?: return value
    return SimpleDateFormat(DISPLAY_DATE, LanguageManager.locale).format(parsed)
}

/** ISO 8601 activity date → "2 September 2026, 18:30" in the device time zone. */
fun String?.asActivityDateTime(): String? {
    val parsed = parseIsoDate(this) ?: return null
    return SimpleDateFormat(DISPLAY_DATE_TIME, LanguageManager.locale).apply {
        timeZone = TimeZone.getDefault()
    }.format(parsed)
}

/** The backend's ISO 8601 shapes, read as UTC. */
fun parseIsoDate(value: String?): Date? {
    val v = value?.trim().orEmpty()
    if (v.isEmpty()) return null
    for (pattern in ISO_PATTERNS) {
        runCatching {
            SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(v)
        }.getOrNull()?.let { return it }
    }
    return null
}
