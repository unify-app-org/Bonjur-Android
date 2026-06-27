package com.bonjur.communities.data.endPoints

import com.bonjur.communities.data.DTOs.RoleAssignRequest
import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class CommunitiesEndPoints : AppEndpoint {

    // Top-level community list. Same route Discover uses.
    data object FetchCommunities : CommunitiesEndPoints() {
        override val path = "api/ds/v1/clubs/communities"
        override val method = NetworkMethod.GET
    }

    data class FetchCommunityDetail(val communityId: Int) : CommunitiesEndPoints() {
        override val path = "api/cs/v1/clubs/$communityId"
        override val method = NetworkMethod.GET
    }

    // Returns all members of a community/club, paged.
    data class FetchCommunityMembers(
        val communityId: Int,
        val page: Int = 0,
        val size: Int = 100,
        val keyword: String? = null
    ) : CommunitiesEndPoints() {
        override val path = "api/cs/v1/clubs/$communityId/members"
        override val method = NetworkMethod.GET
        override val queryParameters = mutableMapOf(
            "page" to page.toString(),
            "size" to size.toString()
        ).apply { keyword?.takeIf { it.isNotBlank() }?.let { put("keyword", it) } }
    }

    // Sub-clubs within a community. Mirrors iOS `getClubs` (parentId carried in query).
    data class GetClubs(val query: Map<String, String>) : CommunitiesEndPoints() {
        override val path = "api/ds/v1/clubs"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    // Assign a role to a member. Mirrors iOS `assignRole`. Community id is sent as the club id.
    data class AssignRole(
        val communityId: Int,
        val request: RoleAssignRequest
    ) : CommunitiesEndPoints() {
        override val path = "api/cs/v1/clubs/$communityId/role"
        override val method = NetworkMethod.POST
        override val body = request
    }
}
