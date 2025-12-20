package com.bonjur.auth.data.endPoint

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class AuthEndpoint : AppEndpoint {

    data class Register(
        val request: RegisterRequest
    ) : AuthEndpoint() {
        override val path = "auth/register"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
        override val body = request
    }
}
