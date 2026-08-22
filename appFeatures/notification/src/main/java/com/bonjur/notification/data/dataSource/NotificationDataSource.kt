package com.bonjur.notification.data.dataSource

import com.bonjur.network.model.PageNationResponse
import com.bonjur.notification.data.DTOs.ClubJoinRequestDTO
import com.bonjur.notification.data.DTOs.EventJoinRequestDTO
import com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO
import com.bonjur.notification.data.DTOs.NotificationDTO

interface NotificationDataSource {
    suspend fun fetchFeed(page: Int, size: Int): PageNationResponse<List<NotificationDTO>>
    suspend fun fetchUnreadCount(): Int
    suspend fun markAllRead()

    /** POST api/ns/v1/notifications/read/{id} — single-row read. */
    suspend fun markRead(id: String)
    suspend fun fetchClubRequests(page: Int, size: Int): PageNationResponse<List<ClubJoinRequestDTO>>
    suspend fun fetchHangoutRequests(page: Int, size: Int): PageNationResponse<List<HangoutJoinRequestDTO>>
    suspend fun fetchEventRequests(page: Int, size: Int): PageNationResponse<List<EventJoinRequestDTO>>
    suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean): ByteArray
    suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean): ByteArray
    suspend fun setEventStatus(eventId: String, userId: String, accept: Boolean): ByteArray
    suspend fun fetchPendingClubs(page: Int, size: Int): PageNationResponse<List<ClubJoinRequestDTO>>
    suspend fun setClubVerification(clubId: Int, accept: Boolean, rejectionReason: String?): ByteArray
}
