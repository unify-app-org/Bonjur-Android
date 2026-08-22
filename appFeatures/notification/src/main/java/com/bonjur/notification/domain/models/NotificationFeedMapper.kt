package com.bonjur.notification.domain.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.notification.R
import com.bonjur.notification.data.DTOs.NotificationDTO
import java.time.Instant
import java.time.ZoneId

/**
 * Maps notification-service rows into feed items and groups a flat page stream
 * into the date buckets the feed renders (Today / Yesterday / This week /
 * Earlier). Mirrors iOS `NotificationFeedMapper`.
 */
object NotificationFeedMapper {

    /** Rejection outcomes are the only rows whose note box carries the admin reason. */
    private val REJECTION_TYPES = setOf(
        NotificationType.REJECTED_USER_FROM_CLUB,
        NotificationType.REJECTED_CLUB_VERIFICATION,
        NotificationType.REJECTED_USER_FROM_HANGOUT,
        NotificationType.REJECTED_USER_FROM_EVENT
    )

    fun item(dto: NotificationDTO): NotificationFeedItem? {
        val id = dto.id ?: return null
        val createdAtMillis = RelativeTime.parse(dto.createdAt)
        val type = NotificationType.from(dto.type ?: "")
        return NotificationFeedItem(
            id = id.toString(),
            type = type,
            title = dto.title ?: "",
            subtitle = dto.body ?: "",
            // `metadata.rejectionReason` is only meaningful on a rejection — the
            // backend also stamps it on SUCCESS rows, which used to render a
            // stray note box under "Club verified".
            note = dto.note ?: dto.metadata?.rejectionReason?.takeIf { type in REJECTION_TYPES },
            imageUrl = dto.imageUrl,
            timeAgo = RelativeTime.short(createdAtMillis),
            isRead = dto.isRead ?: true,
            targetType = NotificationTargetType.from(dto.targetType),
            targetId = dto.targetId,
            createdAtMillis = createdAtMillis
        )
    }

    /**
     * Newest first; rows missing createdAt keep their relative order and sink
     * to the bottom. The server order is not relied on — pages are
     * concatenated as they load, so the merged list is sorted client-side.
     * Mirrors iOS `NotificationFeedMapper.sorted`.
     */
    fun sorted(items: List<NotificationFeedItem>): List<NotificationFeedItem> =
        items
            // Pages can overlap when rows arrive between requests; a repeated
            // id would render twice (and collide as a LazyColumn key).
            .distinctBy { it.id }
            .sortedWith(compareByDescending { it.createdAtMillis ?: Long.MIN_VALUE })

    /**
     * Buckets are filled newest-first; items without a parseable createdAt
     * sink to "Earlier".
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

        for (item in sorted(items)) {
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
            LanguageManager.string(R.string.notif_today) to todayItems,
            LanguageManager.string(R.string.notif_yesterday) to yesterdayItems,
            LanguageManager.string(R.string.notif_this_week) to thisWeekItems,
            LanguageManager.string(R.string.notif_earlier) to earlierItems
        )
            .filter { it.second.isNotEmpty() }
            .map { NotificationSection(it.first, it.second) }
    }
}
