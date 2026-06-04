package com.bonjur.communities.data.dataSource

import com.bonjur.communities.data.DTOs.CommunityDetailResponse
import com.bonjur.communities.data.DTOs.CommunityListResponse
import com.bonjur.communities.data.DTOs.CommunityMemberResponse

interface CommunitiesDataSource {
    suspend fun fetchCommunities(): List<CommunityListResponse>
    suspend fun fetchCommunityDetail(communityId: Int): CommunityDetailResponse
    suspend fun fetchCommunityMembers(communityId: Int, page: Int = 0, size: Int = 100): CommunityMemberResponse
}
