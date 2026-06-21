package com.bonjur.profile.data.endPoint

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.MultipartFile
import com.bonjur.network.APIClient.MultipartPayload
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

    // PUT /api/us/v1/users  — update profile. Mirrors iOS `updateUserData`:
    // fields are sent as query params, the avatar (if any) as a multipart file.
    data class UpdateProfile(
        val fields: Map<String, String>,
        val imageFile: MultipartFile? = null
    ) : ProfileEndpoints() {
        override val path = "api/us/v1/users"
        override val method = NetworkMethod.PUT
        override val queryParameters = fields
        // Always multipart/form-data (matches iOS .formData) — server rejects
        // application/json here (415). File optional; empty parts when no avatar.
        override val multipart =
            MultipartPayload(files = imageFile?.let { listOf(it) } ?: emptyList())
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

    // GET /api/es/v1/events/my  — logged-in user's events (paginated)
    data object MyEvents : ProfileEndpoints() {
        override val path = "api/es/v1/events/my"
        override val method = NetworkMethod.GET
    }

    // GET /api/hs/v1/hangouts/{userId}/myhangouts  — user's activities (paginated)
    data class MyHangouts(val userId: String) : ProfileEndpoints() {
        override val path = "api/hs/v1/hangouts/$userId/myhangouts"
        override val method = NetworkMethod.GET
    }

    // GET /api/cs/v1/clubs/{userId}/myclubs  — user's clubs (paginated)
    data class MyClubs(val userId: String) : ProfileEndpoints() {
        override val path = "api/cs/v1/clubs/$userId/myclubs"
        override val method = NetworkMethod.GET
    }
}