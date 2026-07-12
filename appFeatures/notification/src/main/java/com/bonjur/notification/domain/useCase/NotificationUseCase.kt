package com.bonjur.notification.domain.useCase

import com.bonjur.notification.data.dataSource.NotificationDataSource
import com.bonjur.notification.domain.models.NotificationFeedMapper
import com.bonjur.notification.domain.models.NotificationFeedPage
import javax.inject.Inject

interface NotificationUseCase {
    /** One page of the live notification feed (`api/ns`). */
    suspend fun fetchFeedPage(page: Int, size: Int): NotificationFeedPage
    suspend fun markAllRead()
    /** Live pending-request total for the banner (club + hangout + event). */
    suspend fun fetchRequestCounts(): Int
    /** Admin-only pending-verification total; throws (403) when not an admin. */
    suspend fun fetchVerificationCount(): Int
    /** Unread notification total for the Discover bell badge. */
    suspend fun fetchUnreadCount(): Int
}

class NotificationUseCaseImpl @Inject constructor(
    private val dataSource: NotificationDataSource
) : NotificationUseCase {

    override suspend fun fetchFeedPage(page: Int, size: Int): NotificationFeedPage {
        val response = dataSource.fetchFeed(page, size)
        return NotificationFeedPage(
            items = response.content.mapNotNull(NotificationFeedMapper::item),
            hasMore = response.hasMore()
        )
    }

    override suspend fun markAllRead() {
        dataSource.markAllRead()
    }

    /** Cheap `size=1` probes — only `totalElements` is read from each source. */
    override suspend fun fetchRequestCounts(): Int {
        val clubs = dataSource.fetchClubRequests(page = 0, size = 1).totalElements ?: 0
        val hangouts = dataSource.fetchHangoutRequests(page = 0, size = 1).totalElements ?: 0
        val events = dataSource.fetchEventRequests(page = 0, size = 1).totalElements ?: 0
        return clubs + hangouts + events
    }

    override suspend fun fetchVerificationCount(): Int =
        dataSource.fetchPendingClubs(page = 0, size = 1).totalElements ?: 0

    override suspend fun fetchUnreadCount(): Int = dataSource.fetchUnreadCount()
}
