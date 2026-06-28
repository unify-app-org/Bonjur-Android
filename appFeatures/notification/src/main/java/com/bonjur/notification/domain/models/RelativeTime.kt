package com.bonjur.notification.domain.models

import java.time.Instant
import java.time.format.DateTimeParseException

/** Compact relative-time formatter for join-request `createdAt` stamps. */
object RelativeTime {

    /** Parses an ISO-8601 instant to epoch millis, or null. */
    fun parse(value: String?): Long? {
        if (value.isNullOrEmpty()) return null
        return try {
            Instant.parse(value).toEpochMilli()
        } catch (e: DateTimeParseException) {
            null
        }
    }

    /** Compact relative stamp ("now", "5m", "2h", "3d", "2w"); empty if null. */
    fun short(millis: Long?, now: Long = System.currentTimeMillis()): String {
        if (millis == null) return ""
        val seconds = ((now - millis) / 1000).coerceAtLeast(0)
        return when {
            seconds < 60 -> "now"
            seconds < 3_600 -> "${seconds / 60}m"
            seconds < 86_400 -> "${seconds / 3_600}h"
            seconds < 604_800 -> "${seconds / 86_400}d"
            else -> "${seconds / 604_800}w"
        }
    }
}
