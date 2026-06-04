package com.bonjur.events.data.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class EventCreateRequest(
    val name: String,
    val about: String = "",
    val location: String = "",
    val ownerContact: String = "",
    val capacity: Int? = null,
    val rules: String? = null,
    val visibility: String = "PUBLIC",
    val eventDate: String = "",
    val categoryIds: List<Int> = emptyList(),
    val links: List<EventLinkDTO> = emptyList()
)

@Serializable
data class EventLinkDTO(
    val type: String = "",
    val name: String = "",
    val url: String = ""
)

@Serializable
data class EventListResponse(
    val id: String? = null,
    val name: String? = null,
    val visibility: String? = null,
    val about: String? = null,
    val capacity: Int? = null,
    val membersCount: Int? = null,
    val eventDate: String? = null,
    val categories: List<EventCategoryResponse> = emptyList()
)

@Serializable
data class EventDetailResponse(
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
    val eventDate: String? = null,
    val links: List<EventLinkDTO> = emptyList(),
    val categories: List<EventCategoryResponse> = emptyList()
)

@Serializable
data class EventCategoryResponse(
    val id: Int = 0,
    val title: String = ""
)
