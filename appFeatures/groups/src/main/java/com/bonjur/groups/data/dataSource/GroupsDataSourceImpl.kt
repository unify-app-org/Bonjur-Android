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

    // The /joined endpoints return a paginated wrapper ({ "content": [...] }). Hand the
    // whole envelope back — the use case needs `totalPages` to know when to stop paging.
    override suspend fun fetchJoinedClubs(query: Map<String, String>): PageNationResponse<List<GroupsClubResponse>> =
        fetch(GroupsEndPoints.JoinedClubs(query))

    override suspend fun fetchJoinedHangouts(query: Map<String, String>): PageNationResponse<List<GroupsHangoutResponse>> =
        fetch(GroupsEndPoints.JoinedHangouts(query))

    override suspend fun fetchJoinedEvents(query: Map<String, String>): PageNationResponse<List<EventListResponse>> =
        fetch(GroupsEndPoints.JoinedEvents(query))
}
