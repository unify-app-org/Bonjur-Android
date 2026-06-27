package com.bonjur.communities.data.dataSource

import com.bonjur.communities.data.DTOs.CommunityClubResponse
import com.bonjur.communities.data.DTOs.CommunityDetailResponse
import com.bonjur.communities.data.DTOs.CommunityListResponse
import com.bonjur.communities.data.DTOs.CommunityMemberResponse
import com.bonjur.communities.data.DTOs.RoleAssignRequest

interface CommunitiesDataSource {
    suspend fun fetchCommunities(): List<CommunityListResponse>
    suspend fun fetchCommunityDetail(communityId: Int): CommunityDetailResponse
    suspend fun fetchCommunityMembers(communityId: Int, page: Int = 0, size: Int = 100, keyword: String? = null): CommunityMemberResponse
    suspend fun getClubs(query: Map<String, String>): List<CommunityClubResponse>
    suspend fun assignRole(communityId: Int, request: RoleAssignRequest): ByteArray
}
