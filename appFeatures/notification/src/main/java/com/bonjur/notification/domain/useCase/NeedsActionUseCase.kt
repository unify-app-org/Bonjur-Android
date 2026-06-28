package com.bonjur.notification.domain.useCase

import com.bonjur.network.model.PageNationResponse
import com.bonjur.notification.data.dataSource.NotificationDataSource
import com.bonjur.notification.domain.models.JoinRequestMapper
import com.bonjur.notification.domain.models.RequestPageResult
import javax.inject.Inject

interface NeedsActionUseCase {
    suspend fun fetchClubRequests(page: Int, size: Int): RequestPageResult
    suspend fun fetchHangoutRequests(page: Int, size: Int): RequestPageResult
    suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean)
    suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean)
    /** Admin-only pending-verification total. Throws (e.g. 403) when not an admin. */
    suspend fun fetchVerificationCount(): Int
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

    override suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean) {
        dataSource.setClubStatus(clubId, userId, accept)
    }

    override suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean) {
        dataSource.setHangoutStatus(hangoutId, userId, accept)
    }

    override suspend fun fetchVerificationCount(): Int =
        dataSource.fetchPendingClubs(page = 0, size = 1).totalElements ?: 0
}

/** True when there are pages after `page`. */
internal fun PageNationResponse<*>.hasMore(): Boolean {
    val p = page ?: return false
    val tp = totalPages ?: return false
    return p + 1 < tp
}
