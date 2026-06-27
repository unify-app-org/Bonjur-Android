package com.bonjur.communities.data.dataSource

import com.bonjur.communities.data.DTOs.CommunityClubResponse
import com.bonjur.communities.data.DTOs.CommunityDetailResponse
import com.bonjur.communities.data.DTOs.CommunityListResponse
import com.bonjur.communities.data.DTOs.CommunityMemberResponse
import com.bonjur.communities.data.DTOs.RoleAssignRequest
import com.bonjur.communities.data.endPoints.CommunitiesEndPoints
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class CommunitiesDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), CommunitiesDataSource {

    override suspend fun fetchCommunities(): List<CommunityListResponse> =
        fetch(CommunitiesEndPoints.FetchCommunities)

    override suspend fun fetchCommunityDetail(communityId: Int): CommunityDetailResponse =
        fetch(CommunitiesEndPoints.FetchCommunityDetail(communityId))

    override suspend fun fetchCommunityMembers(
        communityId: Int,
        page: Int,
        size: Int,
        keyword: String?
    ): CommunityMemberResponse =
        fetch(CommunitiesEndPoints.FetchCommunityMembers(communityId, page, size, keyword))

    override suspend fun getClubs(query: Map<String, String>): List<CommunityClubResponse> =
        fetch(CommunitiesEndPoints.GetClubs(query))

    override suspend fun assignRole(communityId: Int, request: RoleAssignRequest): ByteArray =
        fetchRawData(CommunitiesEndPoints.AssignRole(communityId, request))
}
