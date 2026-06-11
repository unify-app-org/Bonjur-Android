package com.bonjur.profile.data.DTOs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Requests ──────────────────────────────────────────────────────────────────

@Serializable
data class ProfileUpdateRequest(
    val birthDate: String? = null,
    val gender: String? = null,
    val about: String? = null,
    val categoriesId: List<Int> = emptyList(),
    val languagesId: List<Int> = emptyList(),
    @SerialName("backgroundColour") val backgroundColour: String? = null
)

// ── Responses ─────────────────────────────────────────────────────────────────

@Serializable
data class UserProfileResponse(
    val userId: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val profileUrl: String? = null,
    val degree: String? = null,
    val specialization: String? = null,
    val entryYear: Int? = null,
    val gender: String? = null,
    val birthDate: String? = null,
    val about: String? = null,
    @SerialName("backgroundColour") val backgroundColour: String? = null,
    val communityName: String? = null,
    val categories: List<ProfileCategoryResponse> = emptyList(),
    val languages: List<ProfileLanguageResponse> = emptyList()
)

@Serializable
data class ProfileCategoryResponse(
    val id: Int = 0,
    val title: String = ""
)

@Serializable
data class ProfileLanguageResponse(
    val id: Int = 0,
    val name: String? = null,
    val code: String? = null
)

@Serializable
data class CategorySectionResponse(
    val type: String? = null,
    val title: String? = null,
    val subCategories: List<SubCategoryResponse> = emptyList()
)

@Serializable
data class SubCategoryResponse(
    val id: Int? = null,
    val title: String? = null
)

@Serializable
data class LanguageResponse(
    val id: Int = 0,
    val name: String? = null,
    val code: String? = null
)

// ── My events (GET api/es/v1/events/my, paginated) ───────────────────────────

@Serializable
data class MyEventResponse(
    val id: String? = null,
    val name: String? = null,
    val visibility: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val background: String? = null,
    val requestStatus: String? = null,
    val eventActivityStatus: String? = null,
    val role: String? = null,
    val categoryResponses: List<ProfileCategoryResponse> = emptyList()
)

// ── My activities (GET api/hs/v1/hangouts/{userId}/myhangouts, paginated) ────

@Serializable
data class MyHangoutResponse(
    val id: String? = null,
    val name: String? = null,
    val visibility: String? = null,
    val status: String? = null,
    val role: String? = null,
    val hangoutActivityStatus: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val categories: List<ProfileCategoryResponse> = emptyList()
)
