package com.bonjur.notification.domain.useCase

import com.bonjur.network.model.PageNationResponse
import com.bonjur.notification.data.dataSource.NotificationDataSource
import com.bonjur.notification.domain.models.JoinRequestMapper
import com.bonjur.notification.domain.models.RequestPageResult
import javax.inject.Inject

interface NeedsActionUseCase {
    suspend fun fetchClubRequests(page: Int, size: Int): RequestPageResult
    suspend fun fetchHangoutRequests(page: Int, size: Int): RequestPageResult
    suspend fun fetchEventRequests(page: Int, size: Int): RequestPageResult
    suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean)
    suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean)
    suspend fun setEventStatus(eventId: String, userId: String, accept: Boolean)
    /** Admin-only pending-verification total. Throws (e.g. 403) when not an admin. */
    suspend fun fetchVerificationCount(): Int

    /**
     * Total pending join requests across clubs + hangouts + events, for the feed banner's
     * badge. Each source is fetched at page 0 / size 1 and read off `totalElements` (same
     * trick as [fetchVerificationCount]) and guarded on its own, so one failing source
     * doesn't zero the other two. Verification is deliberately excluded — it is admin-only
     * (403 for everyone else) and already has its own banner inside Needs Action.
     */
    suspend fun fetchPendingActionCount(): Int
}

class NeedsActionUseCaseImpl @Inject constructor(
    private val dataSource: NotificationDataSource
) : NeedsActionUseCase {

    override suspend fun fetchClubRequests(page: Int, size: Int): RequestPageResult {
        val response = dataSource.fetchClubRequests(page, size)
        return RequestPageResult(
            items = response.content.mapNotNull(JoinRequestMapper::item),
            hasMore = response.hasMore()
        )
    }

    override suspend fun fetchHangoutRequests(page: Int, size: Int): RequestPageResult {
        val response = dataSource.fetchHangoutRequests(page, size)
        return RequestPageResult(
            items = response.content.mapNotNull(JoinRequestMapper::item),
            hasMore = response.hasMore()
        )
    }

    override suspend fun fetchEventRequests(page: Int, size: Int): RequestPageResult {
        val response = dataSource.fetchEventRequests(page, size)
        return RequestPageResult(
            items = response.content.mapNotNull(JoinRequestMapper::item),
            hasMore = response.hasMore()
        )
    }

    override suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean) {
        dataSource.setClubStatus(clubId, userId, accept)
    }

    override suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean) {
        dataSource.setHangoutStatus(hangoutId, userId, accept)
    }

    override suspend fun setEventStatus(eventId: String, userId: String, accept: Boolean) {
        dataSource.setEventStatus(eventId, userId, accept)
    }

    override suspend fun fetchVerificationCount(): Int =
        dataSource.fetchPendingClubs(page = 0, size = 1).totalElements ?: 0

    override suspend fun fetchPendingActionCount(): Int {
        suspend fun total(fetch: suspend () -> PageNationResponse<*>): Int =
            runCatching { fetch().totalElements ?: 0 }.getOrDefault(0)

        return total { dataSource.fetchClubRequests(page = 0, size = 1) } +
            total { dataSource.fetchHangoutRequests(page = 0, size = 1) } +
            total { dataSource.fetchEventRequests(page = 0, size = 1) }
    }
}

/** True when there are pages after `page`. */
internal fun PageNationResponse<*>.hasMore(): Boolean {
    val p = page ?: return false
    val tp = totalPages ?: return false
    return p + 1 < tp
}
