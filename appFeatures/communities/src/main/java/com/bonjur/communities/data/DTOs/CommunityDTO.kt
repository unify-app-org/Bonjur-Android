package com.bonjur.communities.data.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class CommunityMemberResponse(
    val content: List<CommunityMember> = emptyList()
) {
    @Serializable
    data class CommunityMember(
        val userId: String? = null,
        val role: String? = null,
        val fullName: String? = null,
        val profileUrl: String? = null,
        val degree: String? = null,
        val specialization: String? = null,
        val entryYear: Int? = null
    )
}

@Serializable
data class CommunityListResponse(
    val id: Int? = null,
    val name: String? = null,
    val about: String? = null,
    val backgroundColour: String? = null,
    val backgroundUrl: String? = null,
    val logoUrl: String? = null
)

@Serializable
data class CommunityDetailResponse(
    val id: Int? = null,
    val name: String = "",
    val about: String? = null,
    val ownerContact: String? = null,
    val location: String? = null,
    val backgroundColour: String? = null,
    val backgroundUrl: String? = null,
    val logoUrl: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null
)
