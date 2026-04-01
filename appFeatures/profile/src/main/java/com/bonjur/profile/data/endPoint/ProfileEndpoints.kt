package com.bonjur.profile.data.endPoint

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class ProfileEndpoints: AppEndpoint {

    class Register : ProfileEndpoints() {
        override val path = "auth/profile"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
    }
}