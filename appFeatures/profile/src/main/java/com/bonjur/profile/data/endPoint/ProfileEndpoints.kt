package com.bonjur.profile.data.endPoint

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class ProfileEndpoints : AppEndpoint {

    // GET /api/us/v1/users/profile  — current user's profile
    data object GetMyProfile : ProfileEndpoints() {
        override val path = "api/us/v1/users/profile"
        override val method = NetworkMethod.GET
    }

    // GET /api/us/v1/users/{id}  — any user by id
    data class GetUserById(val userId: String) : ProfileEndpoints() {
        override val path = "api/us/v1/users/$userId"
        override val method = NetworkMethod.GET
    }

    // PUT /api/us/v1/users  — update profile (JSON body; image handled separately)
    data class UpdateProfile(
        val request: com.bonjur.profile.data.DTOs.ProfileUpdateRequest
    ) : ProfileEndpoints() {
        override val path = "api/us/v1/users"
        override val method = NetworkMethod.PUT
        override val body = request
    }

    // DELETE /api/us/v1/users
    data object DeleteAccount : ProfileEndpoints() {
        override val path = "api/us/v1/users"
        override val method = NetworkMethod.DELETE
    }

    // GET /api/sd/v1/categories
    data object GetCategories : ProfileEndpoints() {
        override val path = "api/sd/v1/categories"
        override val method = NetworkMethod.GET
    }

    // GET /api/sd/v1/languages
    data object GetLanguages : ProfileEndpoints() {
        override val path = "api/sd/v1/languages"
        override val method = NetworkMethod.GET
    }
}