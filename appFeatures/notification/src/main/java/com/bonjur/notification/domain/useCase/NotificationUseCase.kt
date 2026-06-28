package com.bonjur.notification.domain.useCase

import com.bonjur.notification.data.dataSource.NotificationDataSource
import com.bonjur.notification.domain.models.NeedsActionSummary
import com.bonjur.notification.domain.models.NotificationFeedItem
import com.bonjur.notification.domain.models.NotificationInbox
import com.bonjur.notification.domain.models.NotificationSection
import com.bonjur.notification.domain.models.NotificationType
import javax.inject.Inject

interface NotificationUseCase {
    /** Mock feed until the notification service ships (mirrors iOS mock). */
    suspend fun fetchInbox(): NotificationInbox
    suspend fun markAllRead()
    /** Live pending-request total for the banner (club + hangout totalElements). */
    suspend fun fetchRequestCount(): Int
}

class NotificationUseCaseImpl @Inject constructor(
    private val dataSource: NotificationDataSource
) : NotificationUseCase {

    override suspend fun fetchInbox(): NotificationInbox = NotificationInbox(
        action = NeedsActionSummary(requests = 3, verifications = 3),
        sections = listOf(
            NotificationSection(
                title = "Today",
                items = listOf(
                    NotificationFeedItem(
                        id = "1",
                        type = NotificationType.BIRTHDAY,
                        title = "It's Durdana's birthday today! 🎉",
                        subtitle = "Be the first to wish her a happy birthday and make her day special.",
                        note = null,
                        imageUrl = null,
                        timeAgo = "2h",
                        isRead = false
                    ),
                    NotificationFeedItem(
                        id = "2",
                        type = NotificationType.EVENT_REMINDER,
                        title = "The Grandmasters events for 1 hours.",
                        subtitle = "Checkmate society club",
                        note = null,
                        imageUrl = "https://cdn.unify.az/clubs/8/logo.png",
                        timeAgo = "4h",
                        isRead = false
                    ),
                    NotificationFeedItem(
                        id = "3",
                        type = NotificationType.VERIFICATION_OUTCOME,
                        title = "Verification rejected",
                        subtitle = "Football club 4s couldn't be verified.",
                        note = "Logo doesn't match the registration document.",
                        imageUrl = "https://cdn.unify.az/clubs/12/logo.png",
                        timeAgo = "7h",
                        isRead = true
                    ),
                    NotificationFeedItem(
                        id = "4",
                        type = NotificationType.HOLIDAY,
                        title = "Happy new year! 🥂",
                        subtitle = "Wishing you a great year ahead.",
                        note = null,
                        imageUrl = null,
                        timeAgo = "8h",
                        isRead = true
                    )
                )
            )
        )
    )

    override suspend fun markAllRead() {
        // no-op for the mock
    }

    override suspend fun fetchRequestCount(): Int {
        val clubs = dataSource.fetchClubRequests(page = 0, size = 1).totalElements ?: 0
        val hangouts = dataSource.fetchHangoutRequests(page = 0, size = 1).totalElements ?: 0
        return clubs + hangouts
    }
}
