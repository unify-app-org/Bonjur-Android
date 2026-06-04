package com.bonjur.hangouts.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class HangoutsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), HangoutsDataSource {

    override suspend fun getHangouts(query: Map<String, String>): List<com.bonjur.hangouts.data.DTOs.HangoutListResponse> =
        fetch(com.bonjur.hangouts.data.endPoints.HangoutsEndPoints.GetHangouts(query))

    override suspend fun getHangoutById(hangoutId: String): com.bonjur.hangouts.data.DTOs.HangoutDetailResponse =
        fetch(com.bonjur.hangouts.data.endPoints.HangoutsEndPoints.GetHangoutById(hangoutId))

    override suspend fun getHangoutMembers(hangoutId: String): List<com.bonjur.hangouts.data.DTOs.HangoutMemberResponse> =
        fetch(com.bonjur.hangouts.data.endPoints.HangoutsEndPoints.GetHangoutMembers(hangoutId))

    override suspend fun createHangout(request: com.bonjur.hangouts.data.DTOs.HangoutCreateRequest): com.bonjur.hangouts.data.DTOs.HangoutDetailResponse =
        fetch(com.bonjur.hangouts.data.endPoints.HangoutsEndPoints.CreateHangout(request))

    override suspend fun editHangout(hangoutId: String, request: com.bonjur.hangouts.data.DTOs.HangoutCreateRequest): com.bonjur.hangouts.data.DTOs.HangoutDetailResponse =
        fetch(com.bonjur.hangouts.data.endPoints.HangoutsEndPoints.EditHangout(hangoutId, request))
}