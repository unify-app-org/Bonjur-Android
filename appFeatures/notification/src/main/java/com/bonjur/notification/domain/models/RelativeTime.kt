package com.bonjur.notification.domain.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.notification.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/** Compact relative-time formatter for join-request `createdAt` stamps. */
object RelativeTime {

    /**
     * The format notification-service actually emits: `19-08-2026 17:55:35`.
     * Same house audit stamp events/communities already parse. No zone on the
     * wire — it is server-local, so it is read in the device zone like the rest
     * of the app. ISO shapes stay as fallbacks (the API spec documents those).
     * An unparseable stamp shows no time on the row and sinks it to "Earlier",
     * so keep this tolerant. Mirrors iOS `RelativeTime.parse`.
     */
    fun parse(value: String?): Long? {
        if (value.isNullOrEmpty()) return null
        return parseHouseStamp(value) ?: parseIso(value.replace(" ", "T"))
    }

    private val houseStamp: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    private fun parseHouseStamp(value: String): Long? = try {
        LocalDateTime.parse(value, houseStamp).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (e: DateTimeParseException) {
        null
    }

    private fun parseIso(value: String): Long? = try {
        Instant.parse(value).toEpochMilli()
    } catch (e: DateTimeParseException) {
        parseOffset(value) ?: parseZoneLess(value)
    }

    private fun parseOffset(value: String): Long? = try {
        OffsetDateTime.parse(value).toInstant().toEpochMilli()
    } catch (e: DateTimeParseException) {
        null
    }

    private fun parseZoneLess(value: String): Long? = try {
        LocalDateTime.parse(value).toInstant(ZoneOffset.UTC).toEpochMilli()
    } catch (e: DateTimeParseException) {
        null
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
