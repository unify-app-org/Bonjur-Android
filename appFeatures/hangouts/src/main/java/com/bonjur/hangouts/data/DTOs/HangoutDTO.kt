package com.bonjur.hangouts.data.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class HangoutCreateRequest(
    val visibility: String = "PUBLIC",
    val name: String,
    val ownerContact: String = "",
    val categoriesId: List<Int> = emptyList(),
    val capacity: Int = 0,
    val links: List<HangoutLinkDTO> = emptyList(),
    val rules: String = "",
    val location: String = "",
    val about: String = "",
    val hangoutDate: String = ""
)

@Serializable
data class HangoutLinkDTO(
    val type: String = "",
    val name: String = "",
    val url: String = ""
)

@Serializable
data class HangoutListResponse(
    val id: String? = null,
    val name: String? = null,
    val visibility: String? = null,
    val requestStatus: String? = null,
    val hangoutActivityStatus: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val hangoutDate: String? = null,
    val location: String? = null,
    // Viewer's role; iOS Hangout + backend HangoutGeneralResponse have it, Android was missing.
    val role: String? = null,
    val categoryResponses: List<HangoutCategoryResponse> = emptyList()
)

@Serializable
data class HangoutDetailResponse(
    val id: String = "",
    val name: String = "",
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val rules: String? = null,
    val location: String? = null,
    val ownerContact: String? = null,
    val visibility: String? = null,
    val role: String? = null,
    /** Pending/accepted state for the current user. Optional; nil → Join/Request fallback. */
    val requestStatus: String? = null,
    val hangoutDate: String? = null,
    val links: List<HangoutLinkDTO> = emptyList(),
    val community: HangoutCommunityResponse? = null,
    val categories: List<HangoutCategoryResponse> = emptyList()
)

@Serializable
data class HangoutCommunityResponse(
    val id: Int = 0,
    val name: String = ""
)

@Serializable
data class HangoutCategoryResponse(
    val id: Int = 0,
    val title: String = ""
)

/** Category picker sections (`GET api/sd/v1/categories`). Mirrors iOS `CategoriesResponse`. */
@Serializable
data class HangoutCategorySectionResponse(
    val type: String? = null,
    val title: String? = null,
    val subCategories: List<HangoutCategoryItemResponse> = emptyList()
)

@Serializable
data class HangoutCategoryItemResponse(
    val id: Int? = null,
    val title: String? = null
)

/** Join-hangout body (`POST api/hs/v1/hangouts/join`). Mirrors iOS `JoinRequest`. */
@Serializable
data class HangoutJoinRequest(
    val hangoutId: String
)

/** Paged members envelope: `GET hs/v1/hangouts/{id}/members` returns `{ content: [...] }`, NOT a bare array. */
@Serializable
data class HangoutMembersResponse(
    val content: List<HangoutMemberResponse> = emptyList()
)

@Serializable
data class HangoutMemberResponse(
    val userId: String? = null,
    val role: String? = null,
    val fullName: String? = null,
    val profileUrl: String? = null,
    val degree: String? = null,
    val specialization: String? = null,
    val entryYear: Int? = null
)
