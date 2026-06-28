package com.bonjur.notification.data.dataSource

import com.bonjur.network.model.PageNationResponse
import com.bonjur.notification.data.DTOs.ClubJoinRequestDTO
import com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO

interface NotificationDataSource {
    suspend fun fetchClubRequests(page: Int, size: Int): PageNationResponse<List<ClubJoinRequestDTO>>
    suspend fun fetchHangoutRequests(page: Int, size: Int): PageNationResponse<List<HangoutJoinRequestDTO>>
    suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean): ByteArray
    suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean): ByteArray
    suspend fun fetchPendingClubs(page: Int, size: Int): PageNationResponse<List<ClubJoinRequestDTO>>
    suspend fun setClubVerification(clubId: Int, accept: Boolean): ByteArray
}
