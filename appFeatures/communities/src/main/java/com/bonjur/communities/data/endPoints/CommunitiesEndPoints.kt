package com.bonjur.communities.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class CommunitiesEndPoints : AppEndpoint {

    class FetchCommunities : CommunitiesEndPoints() {
        override val path = "communities"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
    }

    class FetchCommunityDetail(private val id: Int) : CommunitiesEndPoints() {
        override val path = "communities/$id"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
    }
}
