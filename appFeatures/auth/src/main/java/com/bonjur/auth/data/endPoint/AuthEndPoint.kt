package com.bonjur.auth.data.endPoint

import com.bonjur.auth.data.DTOs.LoginRequest
import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class AuthEndpoint : AppEndpoint {

    data class Login(val request: LoginRequest) : AuthEndpoint() {
        override val path = "api/as/v1/auth/login"
        override val method = NetworkMethod.POST
        override val requiresAuth = false
        override val body = request
    }

    data object GetCommunities : AuthEndpoint() {
        override val path = "api/sd/v1/communities"
        override val method = NetworkMethod.GET
        override val requiresAuth = false
    }

    data object GetLanguages : AuthEndpoint() {
        override val path = "api/sd/v1/languages"
        override val method = NetworkMethod.GET
        override val requiresAuth = false
    }

    data object GetCategories : AuthEndpoint() {
        override val path = "api/sd/v1/categories"
        override val method = NetworkMethod.GET
        override val requiresAuth = false
    }

    data object SendOptionals : AuthEndpoint() {
        override val path = "api/us/v1/users"
        override val method = NetworkMethod.PUT
        override val requiresAuth = true
    }
}
