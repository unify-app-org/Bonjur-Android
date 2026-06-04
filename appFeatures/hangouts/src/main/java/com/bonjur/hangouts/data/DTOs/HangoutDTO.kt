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
