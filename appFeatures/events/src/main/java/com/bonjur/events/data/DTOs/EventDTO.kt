package com.bonjur.events.data.DTOs

import kotlinx.serialization.Serializable

// MARK: - Create request (JSON "request" part of multipart POST api/es/v1/events)

@Serializable
data class EventCreateRequest(
    val clubId: Int,
    val name: String,
    val about: String = "",
    val location: String = "",
    val ownerContact: String = "",
    val capacity: Int? = null,
    val rule: String? = null,
    val visibility: String = "PUBLIC",
    val eventDate: String = "",
    val reminderTime: String = "NONE",
    val categoryIds: List<Int> = emptyList(),
    val links: List<EventLinkDTO> = emptyList(),
    val userIds: List<String> = emptyList()
)

@Serializable
data class EventLinkDTO(
    val type: String = "",
    val name: String = "",
    val url: String = ""
)

// MARK: - Club selector (GET api/cs/v1/clubs/forEvents)

@Serializable
data class ClubForEventResponse(
    val clubId: Int = 0,
    val clubName: String? = null,
    val profileUrl: String? = null
)

// MARK: - Categories (GET api/sd/v1/categories)

@Serializable
data class EventCategorySectionResponse(
    val type: String? = null,
    val title: String? = null,
    val subCategories: List<EventCategoryItemResponse> = emptyList()
)

@Serializable
data class EventCategoryItemResponse(
    val id: Int? = null,
    val title: String? = null
)

// MARK: - Discover list (GET api/ds/v1/events)

@Serializable
data class EventListResponse(
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
    val categoryResponses: List<EventCategoryResponse> = emptyList()
)

// MARK: - Detail (GET api/es/v1/events/{eventId})

@Serializable
data class EventDetailResponse(
    val id: String = "",
    val name: String = "",
    val visibility: String? = null,
    val ownerContact: String? = null,
    val location: String? = null,
    val about: String? = null,
    val rule: String? = null,
    val capacity: Int? = null,
    val club: EventClubResponse? = null,
    val backgroundUrl: String? = null,
    val membersCount: Int? = null,
    val eventUserRole: String? = null,
    val attachments: List<String> = emptyList(),
    val links: List<EventLinkDTO> = emptyList(),
    val categories: List<EventCategoryResponse> = emptyList(),
    val isDeleted: Boolean? = null,
    val modifiedAt: String? = null
)

@Serializable
data class EventClubResponse(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class EventCategoryResponse(
    val id: Int = 0,
    val title: String = ""
)

// MARK: - Members (GET api/es/v1/events/{eventId}/members, paginated)

@Serializable
data class EventMembersResponse(
    val content: List<EventMember> = emptyList(),
    val page: Int? = null,
    val size: Int? = null,
    val totalElements: Int? = null,
    val numberOfElements: Int? = null,
    val totalPages: Int? = null
) {
    @Serializable
    data class EventMember(
        val userId: String? = null,
        val profileUrl: String? = null,
        val role: String? = null,
        val fullName: String? = null,
        val degree: String? = null,
        val specialization: String? = null,
        val entryYear: Int? = null
    )
}
