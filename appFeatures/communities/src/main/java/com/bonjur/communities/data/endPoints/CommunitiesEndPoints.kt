package com.bonjur.communities.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class CommunitiesEndPoints : AppEndpoint {

    data object FetchCommunities : CommunitiesEndPoints() {
        override val path = "api/ds/v1/communities"
        override val method = NetworkMethod.GET
    }

    data class FetchCommunityDetail(val communityId: Int) : CommunitiesEndPoints() {
        override val path = "api/cs/v1/clubs/$communityId"
        override val method = NetworkMethod.GET
    }

    // Returns all members of a community/club, paged
    data class FetchCommunityMembers(
        val communityId: Int,
        val page: Int = 0,
        val size: Int = 100
    ) : CommunitiesEndPoints() {
        override val path = "api/cs/v1/clubs/$communityId/members"
        override val method = NetworkMethod.GET
        override val queryParameters = mapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )
    }
}
