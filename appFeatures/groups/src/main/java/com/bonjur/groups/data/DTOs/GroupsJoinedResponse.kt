package com.bonjur.groups.data.DTOs

import kotlinx.serialization.Serializable

/**
 * Groups-local DTOs for the joined-activity endpoints. The `/joined` payloads differ in
 * field names from the plain list DTOs (`ClubListResponse`/`HangoutListResponse`), so — like
 * iOS `GroupsDTOModel` — Groups owns its own shapes. Joined events match the events module's
 * `EventListResponse` exactly, so that one is reused.
 *
 * Club joined: memberCount (not membersCount), requestStatus + role (not joined/clubUserRole).
 * Hangout joined: status (not requestStatus), categories (not categoryResponses).
 */

@Serializable
data class GroupsClubResponse(
    val id: Int? = null,
    val name: String? = null,
    val communityName: String? = null,
    val background: String? = null,
    val clubProfile: String? = null,
    val backgroundUrl: String? = null,
    val capacity: Int? = null,
    val visibility: String? = null,
    val clubStatus: String? = null,
    val requestStatus: String? = null,
    val role: String? = null,
    val about: String? = null,
    val members: List<GroupsMember> = emptyList(),
    val categoryResponses: List<GroupsCategory> = emptyList(),
    val memberCount: Int? = null,
    val eventCount: Int? = null
)

@Serializable
data class GroupsHangoutResponse(
    val id: String? = null,
    val name: String? = null,
    val location: String? = null,
    val visibility: String? = null,
    val hangoutDate: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val status: String? = null,
    val hangoutActivityStatus: String? = null,
    val role: String? = null,
    val categories: List<GroupsCategory> = emptyList()
)

@Serializable
data class GroupsMember(
    val id: String? = null,
    val fullName: String? = null,
    val url: String? = null
)

@Serializable
data class GroupsCategory(
    val id: Int = 0,
    val title: String = ""
)
