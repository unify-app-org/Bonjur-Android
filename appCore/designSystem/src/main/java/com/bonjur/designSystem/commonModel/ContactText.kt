package com.bonjur.designSystem.commonModel

/**
 * Detail-screen contact helpers, shared by clubs / events / hangouts / communities so
 * the four screens agree on what counts as a phone number. Mirrors iOS `EventsRepo`'s
 * `cleaned` / `phoneNumber` pair.
 */

/** Trims and drops placeholder values the backend sends for "not set". */
fun String?.cleanedContact(): String? =
    this?.trim()?.takeIf { it.isNotEmpty() && it != "-" && !it.equals("null", ignoreCase = true) }

/**
 * Returns the contact only when it looks dialable, so the row becomes tappable
 * (call / copy). Anything else — an email, a handle — stays plain text.
 */
fun String?.dialablePhone(): String? {
    val value = cleanedContact() ?: return null
    val digits = value.count { it.isDigit() }
    val allowed = value.all { it.isDigit() || it in "+ -()" }
    return if (allowed && digits >= 7) value else null
}
