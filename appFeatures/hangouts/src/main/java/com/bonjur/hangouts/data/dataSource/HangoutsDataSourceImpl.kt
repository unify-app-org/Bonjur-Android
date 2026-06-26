package com.bonjur.hangouts.data.dataSource

import com.bonjur.hangouts.data.DTOs.HangoutCategorySectionResponse
import com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
import com.bonjur.hangouts.data.DTOs.HangoutDetailResponse
import com.bonjur.hangouts.data.DTOs.HangoutJoinRequest
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.hangouts.data.DTOs.HangoutMembersResponse
import com.bonjur.hangouts.data.endPoints.HangoutsEndPoints
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class HangoutsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), HangoutsDataSource {

    override suspend fun getHangouts(query: Map<String, String>): List<HangoutListResponse> =
        fetch(HangoutsEndPoints.GetHangouts(query))

    override suspend fun getHangoutById(hangoutId: String): HangoutDetailResponse =
        fetch(HangoutsEndPoints.GetHangoutById(hangoutId))

    override suspend fun getHangoutMembers(
        hangoutId: String,
        page: Int,
        size: Int
    ): HangoutMembersResponse =
        fetch(HangoutsEndPoints.GetHangoutMembers(hangoutId, page, size))

    override suspend fun getCategories(): List<HangoutCategorySectionResponse> =
        fetch(HangoutsEndPoints.GetCategories())

    // Mirrors iOS (returns Void): don't decode a typed response — create/edit/join/exit
    // may return an empty/201 body, and a parse failure would mask success. fetchRawData
    // also correctly JSON-serializes the request body (the typed `fetch` path does not).
    override suspend fun createHangout(request: HangoutCreateRequest): ByteArray =
        fetchRawData(HangoutsEndPoints.CreateHangout(request))

    override suspend fun editHangout(hangoutId: String, request: HangoutCreateRequest): ByteArray =
        fetchRawData(HangoutsEndPoints.EditHangout(hangoutId, request))

    override suspend fun joinHangout(request: HangoutJoinRequest): ByteArray =
        fetchRawData(HangoutsEndPoints.JoinHangout(request))

    override suspend fun exitHangout(hangoutId: String): ByteArray =
        fetchRawData(HangoutsEndPoints.ExitHangout(hangoutId))
}
