package com.bonjur.groups.data.dataSource

import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.groups.data.DTOs.GroupsClubResponse
import com.bonjur.groups.data.DTOs.GroupsHangoutResponse
import com.bonjur.groups.data.endPoints.GroupsEndPoints
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import com.bonjur.network.model.PageNationResponse
import javax.inject.Inject

class GroupsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), GroupsDataSource {

    // The /joined endpoints return a paginated wrapper ({ "content": [...] }), so decode
    // PageNationResponse<List<T>> and hand back the content (matches ProfileDataSourceImpl).
    override suspend fun fetchJoinedClubs(query: Map<String, String>): List<GroupsClubResponse> =
        fetch<PageNationResponse<List<GroupsClubResponse>>>(GroupsEndPoints.JoinedClubs(query)).content

    override suspend fun fetchJoinedHangouts(query: Map<String, String>): List<GroupsHangoutResponse> =
        fetch<PageNationResponse<List<GroupsHangoutResponse>>>(GroupsEndPoints.JoinedHangouts(query)).content

    override suspend fun fetchJoinedEvents(query: Map<String, String>): List<EventListResponse> =
        fetch<PageNationResponse<List<EventListResponse>>>(GroupsEndPoints.JoinedEvents(query)).content
}
