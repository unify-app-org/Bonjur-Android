package com.bonjur.groups.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class GroupsEndPoints : AppEndpoint {

    data object JoinedClubs : GroupsEndPoints() {
        override val path = "api/cs/v1/clubs/joined"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
    }

    data object JoinedHangouts : GroupsEndPoints() {
        override val path = "api/hs/v1/hangouts/get-joined"
        override val method = NetworkMethod.GET
        override val requiresAuth = true
    }
}
