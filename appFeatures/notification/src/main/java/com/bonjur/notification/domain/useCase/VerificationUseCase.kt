package com.bonjur.notification.domain.useCase

import com.bonjur.notification.data.dataSource.NotificationDataSource
import com.bonjur.notification.domain.models.JoinRequestMapper
import com.bonjur.notification.domain.models.VerificationPageResult
import javax.inject.Inject

interface VerificationUseCase {
    suspend fun fetchPending(page: Int, size: Int): VerificationPageResult
    suspend fun setStatus(clubId: Int, accept: Boolean)
}

class VerificationUseCaseImpl @Inject constructor(
    private val dataSource: NotificationDataSource
) : VerificationUseCase {

    override suspend fun fetchPending(page: Int, size: Int): VerificationPageResult {
        val response = dataSource.fetchPendingClubs(page, size)
        return VerificationPageResult(
            items = response.content.mapNotNull(JoinRequestMapper::verification),
            hasMore = response.hasMore()
        )
    }

    override suspend fun setStatus(clubId: Int, accept: Boolean) {
        dataSource.setClubVerification(clubId, accept)
    }
}
