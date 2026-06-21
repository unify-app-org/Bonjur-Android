package com.bonjur.discover.data.DTOs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// MARK: - Member

@Serializable
data class DiscoverMember(
    val id: String? = null,
    val fullName: String? = null,
    val url: String? = null
)

// MARK: - Category (filters)

@Serializable
data class DiscoverCategoryResponse(
    val id: Int? = null,
    val title: String? = null
)

@Serializable
data class DiscoverCategorySection(
    val type: String? = null,
    val title: String? = null,
    val subCategories: List<DiscoverSubCategory> = emptyList()
)

@Serializable
data class DiscoverSubCategory(
    val id: Int? = null,
    val title: String? = null
)

// MARK: - Hangout

@Serializable
data class DiscoverHangout(
    val id: String? = null,
    val name: String? = null,
    val visibility: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val requestStatus: String? = null,
    val categoryResponses: List<DiscoverCategoryResponse> = emptyList()
)

// MARK: - Club

@Serializable
data class DiscoverClub(
    val id: Int? = null,
    val name: String? = null,
    val communityName: String? = null,
    @SerialName("background") val background: String? = null,
    val visibility: String? = null,
    val clubProfile: String? = null,
    val backgroundUrl: String? = null,
    val about: String? = null,
    val count: Int? = null,
    val capacity: Int? = null,
    val joined: Boolean? = null,
    val members: List<DiscoverMember>? = null,
    val clubUserRole: String? = null,
    val eventCount: Int? = null,
    val categoryResponses: List<DiscoverCategory> = emptyList()
)

@Serializable
data class DiscoverCategory(
    val id: Int = 0,
    val title: String = ""
)

// MARK: - Community

@Serializable
data class DiscoverCommunity(
    val id: Int? = null,
    val name: String? = null,
    val membersCount: Int? = null,
    val profile: String? = null,
    val backgroundUrl: String? = null,
    val members: List<DiscoverMember>? = null,
    @SerialName("backgroundColour") val background: String? = null
)

// MARK: - Event

@Serializable
data class DiscoverEvent(
    val id: String? = null,
    val name: String? = null,
    val visibility: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val background: String? = null,
    val location: String? = null,
    val club: DiscoverEventClub? = null,
    val eventDate: String? = null,
    val requestStatus: String? = null,
    val categoryResponses: List<DiscoverCategoryResponse> = emptyList()
)

@Serializable
data class DiscoverEventClub(
    val id: Int? = null,
    val name: String? = null
)

// MARK: - User profile

@Serializable
data class DiscoverUserResponse(
    val userId: String? = null,
    val fullName: String? = null,
    val profileUrl: String? = null,
    val fileUrl: String? = null,
    val greeting: String? = null
)

// MARK: - Requests

@Serializable
data class JoinHangoutRequest(
    val hangoutId: String
)
