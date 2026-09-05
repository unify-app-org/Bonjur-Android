package com.bonjur.designSystem.utils

/**
 * Sign-in email format check.
 *
 * Pattern is iOS's `String.isValidEmail()`
 * (`AppCore/AppUtils/Extensions/Extension+String.swift`) verbatim, so the two apps
 * agree on what counts as well-formed. iOS never calls it — the check is deliberately
 * Android-only for now (decided 2026-09-06), which is why the pattern is copied rather
 * than the behaviour.
 *
 * `Regex.matches` anchors the whole string, matching iOS's `NSPredicate SELF MATCHES`.
 */
private val EMAIL_PATTERN = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")

fun String.isValidEmail(): Boolean = EMAIL_PATTERN.matches(trim())
