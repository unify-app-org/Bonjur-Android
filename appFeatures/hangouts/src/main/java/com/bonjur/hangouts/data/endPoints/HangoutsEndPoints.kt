package com.bonjur.hangouts.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class HangoutsEndPoints : AppEndpoint {

    class Register : HangoutsEndPoints() {
        override val path = "auth/discover"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
    }
}