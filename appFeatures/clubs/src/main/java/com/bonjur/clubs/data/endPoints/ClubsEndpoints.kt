package com.bonjur.clubs.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class ClubsEndpoints: AppEndpoint {

    class Register : ClubsEndpoints() {
        override val path = "auth/discover"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
    }
}