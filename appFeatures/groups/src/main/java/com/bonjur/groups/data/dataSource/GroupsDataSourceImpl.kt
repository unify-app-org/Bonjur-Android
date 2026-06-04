package com.bonjur.groups.data.dataSource

import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.groups.data.endPoints.GroupsEndPoints
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class GroupsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), GroupsDataSource {

    override suspend fun fetchJoinedClubs(): List<ClubListResponse> =
        fetch(GroupsEndPoints.JoinedClubs)

    override suspend fun fetchJoinedHangouts(): List<HangoutListResponse> =
        fetch(GroupsEndPoints.JoinedHangouts)
}
