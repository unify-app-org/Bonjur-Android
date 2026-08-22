package com.bonjur.auth.data.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val mail: String,
    val password: String?,
    val communityId: Int,
    val deviceId: String,
    val devicePlatform: String = "ANDROID",
    val deviceOs: String,
    val deviceModel: String,
    val appVersion: String,
    val idToken: String?
)

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val isFirstLogin: Boolean,
    /**
     * The user's role in the community they signed in to (raw backend code, e.g.
     * "PRESIDENT"). Drives the community-president shortcuts (skip club verify
     * prompt) and admin-only screens. Mirrors iOS `LoginResponse.userCommunityRole`.
     */
    val userCommunityRole: String? = null
)

@Serializable
data class CommunityResponse(
    val id: Int,
    val name: String
)

@Serializable
data class LanguageResponse(
    val id: Int,
    val name: String
)

@Serializable
data class CategoryResponse(
    val id: Int,
    val title: String
)

// Legacy — kept for any remaining references
@Serializable
data class RegisterRequest(val email: String = "")
data class RegisterResponse(val token: String, val refreshToken: String)
