package com.bonjur.notification.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import com.bonjur.network.model.PageNationResponse
import com.bonjur.notification.data.DTOs.ClubJoinRequestDTO
import com.bonjur.notification.data.DTOs.ClubStatusRequest
import com.bonjur.notification.data.DTOs.ClubVerificationRequest
import com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO
import com.bonjur.notification.data.DTOs.HangoutStatusRequest
import com.bonjur.notification.data.endPoints.NotificationEndpoints
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), NotificationDataSource {

    override suspend fun fetchClubRequests(page: Int, size: Int): PageNationResponse<List<ClubJoinRequestDTO>> =
        fetch(NotificationEndpoints.ClubJoinRequests(query(page, size)))

    override suspend fun fetchHangoutRequests(page: Int, size: Int): PageNationResponse<List<HangoutJoinRequestDTO>> =
        fetch(NotificationEndpoints.HangoutJoinRequests(query(page, size)))

    override suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean): ByteArray =
        fetchRawData(
            NotificationEndpoints.SetClubRequestStatus(
                ClubStatusRequest(clubId = clubId, userId = userId, status = if (accept) "ACCEPT" else "REJECT")
            )
        )

    override suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean): ByteArray =
        fetchRawData(
            NotificationEndpoints.SetHangoutRequestStatus(
                hangoutId = hangoutId,
                request = HangoutStatusRequest(userId = userId, status = if (accept) "ACCEPTED" else "REJECTED")
            )
        )

    override suspend fun fetchPendingClubs(page: Int, size: Int): PageNationResponse<List<ClubJoinRequestDTO>> =
        fetch(NotificationEndpoints.ClubPending(query(page, size)))

    override suspend fun setClubVerification(clubId: Int, accept: Boolean): ByteArray =
        fetchRawData(
            NotificationEndpoints.SetClubVerification(
                ClubVerificationRequest(clubId = clubId, status = if (accept) "ACCEPT" else "REJECT")
            )
        )

    private fun query(page: Int, size: Int) = mapOf("page" to page.toString(), "size" to size.toString())
}
