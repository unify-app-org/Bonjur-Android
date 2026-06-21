package com.bonjur.discover.data.endPoints

import com.bonjur.discover.data.DTOs.JoinHangoutRequest
import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class DiscoverEndPoints : AppEndpoint {

    data class GetHangouts(val query: Map<String, String>) : DiscoverEndPoints() {
        override val path = "api/ds/v1/hangouts"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetCommunities(val query: Map<String, String>) : DiscoverEndPoints() {
        override val path = "api/ds/v1/clubs/communities"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetClubs(val query: Map<String, String>) : DiscoverEndPoints() {
        override val path = "api/ds/v1/clubs"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetEvents(val query: Map<String, String>) : DiscoverEndPoints() {
        override val path = "api/ds/v1/events"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data object GetCategories : DiscoverEndPoints() {
        override val path = "api/sd/v1/categories"
        override val method = NetworkMethod.GET
    }

    data class GetUserById(val userId: String) : DiscoverEndPoints() {
        override val path = "api/us/v1/users/$userId"
        override val method = NetworkMethod.GET
    }

    data class JoinHangout(val request: JoinHangoutRequest) : DiscoverEndPoints() {
        override val path = "api/hs/v1/hangouts/join"
        override val method = NetworkMethod.POST
        override val body = request
    }
}
