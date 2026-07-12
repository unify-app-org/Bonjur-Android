package com.bonjur.notification.domain.models

import com.bonjur.notification.data.DTOs.NotificationDTO
import java.time.Instant
import java.time.ZoneId

/**
 * Maps notification-service rows into feed items and groups a flat page stream
 * into the date buckets the feed renders (Today / Yesterday / This week /
 * Earlier). Mirrors iOS `NotificationFeedMapper`.
 */
object NotificationFeedMapper {

    fun item(dto: NotificationDTO): NotificationFeedItem? {
        val id = dto.id ?: return null
        val createdAtMillis = RelativeTime.parse(dto.createdAt)
        return NotificationFeedItem(
            id = id.toString(),
            type = NotificationType.from(dto.type ?: ""),
            title = dto.title ?: "",
            subtitle = dto.body ?: "",
            note = dto.note,
            imageUrl = dto.imageUrl,
            timeAgo = RelativeTime.short(createdAtMillis),
            isRead = dto.isRead ?: true,
            targetType = NotificationTargetType.from(dto.targetType),
            targetId = dto.targetId,
            createdAtMillis = createdAtMillis
        )
    }

    /**
     * Buckets keep the incoming (server, newest-first) order within each
     * section; items without a parseable createdAt sink to "Earlier".
     */
    fun sections(
        items: List<NotificationFeedItem>,
        now: Long = System.currentTimeMillis(),
        zone: ZoneId = ZoneId.systemDefault()
    ): List<NotificationSection> {
        val today = Instant.ofEpochMilli(now).atZone(zone).toLocalDate()
        val startOfToday = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val startOfYesterday = today.minusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val startOfWeekWindow = today.minusDays(7).atStartOfDay(zone).toInstant().toEpochMilli()

        val todayItems = mutableListOf<NotificationFeedItem>()
        val yesterdayItems = mutableListOf<NotificationFeedItem>()
        val thisWeekItems = mutableListOf<NotificationFeedItem>()
        val earlierItems = mutableListOf<NotificationFeedItem>()

        for (item in items) {
            val millis = item.createdAtMillis
            when {
                millis == null -> earlierItems.add(item)
                millis >= startOfToday -> todayItems.add(item)
                millis >= startOfYesterday -> yesterdayItems.add(item)
                millis >= startOfWeekWindow -> thisWeekItems.add(item)
                else -> earlierItems.add(item)
            }
        }

        return listOf(
            "Today" to todayItems,
            "Yesterday" to yesterdayItems,
            "This week" to thisWeekItems,
            "Earlier" to earlierItems
        )
            .filter { it.second.isNotEmpty() }
            .map { NotificationSection(it.first, it.second) }
    }
}
