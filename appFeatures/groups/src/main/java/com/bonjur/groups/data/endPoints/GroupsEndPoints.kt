package com.bonjur.groups.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class GroupsEndPoints : AppEndpoint {

    data class JoinedClubs(val query: Map<String, String>) : GroupsEndPoints() {
        override val path = "api/cs/v1/clubs/joined"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
        override val queryParameters = query
    }

    data class JoinedHangouts(val query: Map<String, String>) : GroupsEndPoints() {
        override val path = "api/hs/v1/hangouts/get-joined"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
        override val queryParameters = query
    }

    data class JoinedEvents(val query: Map<String, String>) : GroupsEndPoints() {
        override val path = "api/es/v1/events/joined"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
        override val queryParameters = query
    }
}
