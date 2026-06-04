package com.bonjur.communities.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class CommunitiesDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), CommunitiesDataSource {

    override suspend fun fetchCommunities(): List<com.bonjur.communities.data.DTOs.CommunityListResponse> =
        fetch(com.bonjur.communities.data.endPoints.CommunitiesEndPoints.FetchCommunities)

    override suspend fun fetchCommunityDetail(communityId: Int): com.bonjur.communities.data.DTOs.CommunityDetailResponse =
        fetch(com.bonjur.communities.data.endPoints.CommunitiesEndPoints.FetchCommunityDetail(communityId))

    override suspend fun fetchCommunityMembers(
        communityId: Int,
        page: Int,
        size: Int
    ): com.bonjur.communities.data.DTOs.CommunityMemberResponse =
        fetch(com.bonjur.communities.data.endPoints.CommunitiesEndPoints.FetchCommunityMembers(communityId, page, size))
}
