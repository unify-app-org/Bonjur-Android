package com.bonjur.discover.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class DiscoverEndPoints : AppEndpoint {

    class Register : DiscoverEndPoints() {
        override val path = "auth/discover"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
    }
}