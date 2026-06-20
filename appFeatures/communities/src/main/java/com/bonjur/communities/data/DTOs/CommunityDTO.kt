package com.bonjur.communities.data.DTOs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Body for `POST /v1/clubs/{clubId}/role`. Mirrors iOS `CommunityDTO.RoleAssignRequest`.
 * A community is modeled as a club server-side, so the community id is passed as the club id.
 * [role] is the API code string (e.g. "VISE_PRESIDENT"), not the display title.
 */
@Serializable
data class RoleAssignRequest(
    val userId: String,
    val role: String
)

/** Page query for the paged members endpoint. Mirrors iOS `CommunityDTO.PaginationQuery`. */
@Serializable
data class PaginationQuery(
    val page: Int,
    val size: Int
)

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

/**
 * A sub-club within a community. Returned by `GET api/ds/v1/clubs?parentId={communityId}`.
 * Mirrors iOS `CommunityDTO.ClubResponse`.
 */
@Serializable
data class CommunityClubResponse(
    val id: Int? = null,
    val name: String? = null,
    val communityName: String? = null,
    @SerialName("background") val background: String? = null,
    val visibility: String? = null,
    val role: String? = null,
    val clubProfile: String? = null,
    val backgroundUrl: String? = null,
    val about: String? = null,
    val count: Int? = null,
    val capacity: Int? = null,
    val joined: Boolean? = null,
    val members: List<CommunityClubMember> = emptyList(),
    val eventCount: Int? = null,
    val categoryResponses: List<CommunityCategory> = emptyList()
)

/** Member thumbnail shown on a club card. Mirrors iOS `CommunityDTO.Member`. */
@Serializable
data class CommunityClubMember(
    val id: String? = null,
    val fullName: String? = null,
    val url: String? = null
)

/**
 * Community detail. Returned by `GET api/cs/v1/clubs/{id}` (a community is a club server-side).
 * Mirrors iOS `CommunityDTO.Response`.
 */
@Serializable
data class CommunityDetailResponse(
    val communityId: Int? = null,
    val visibility: String? = null,
    val clubUserRole: String? = null,
    @SerialName("backgroundColour") val backgroundColour: String? = null,
    val name: String = "",
    val ownerContact: String? = null,
    val location: String? = null,
    val about: String? = null,
    val rule: String? = null,
    val backgroundUrl: String? = null,
    val logoUrl: String? = null,
    val modifiedAt: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val communityName: String? = null,
    val links: List<CommunityLink>? = null,
    val categories: List<CommunityCategory> = emptyList()
)

@Serializable
data class CommunityCategory(
    val id: Int = 0,
    val title: String = ""
)

/**
 * Top-level community card. Returned by `GET api/ds/v1/clubs/communities`.
 * Shape mirrors `DiscoverCommunity` (the proven community-list response).
 */
@Serializable
data class CommunityListResponse(
    val id: Int? = null,
    val name: String? = null,
    val membersCount: Int? = null,
    val clubCount: Int? = null,
    val profile: String? = null,
    val backgroundUrl: String? = null,
    val members: List<CommunityListMember> = emptyList(),
    @SerialName("backgroundColour") val background: String? = null
)

@Serializable
data class CommunityListMember(
    val id: String? = null,
    val fullName: String? = null,
    val url: String? = null
)

@Serializable
data class CommunityLink(
    val type: String = "",
    val name: String = "",
    val url: String = ""
)
