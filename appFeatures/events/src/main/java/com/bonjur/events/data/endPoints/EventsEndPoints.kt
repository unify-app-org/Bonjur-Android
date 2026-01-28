package com.bonjur.events.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class EventsEndPoints : AppEndpoint {

    class Register : EventsEndPoints() {
        override val path = "auth/discover"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
    }
}