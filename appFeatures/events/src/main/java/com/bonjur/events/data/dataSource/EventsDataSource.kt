package com.bonjur.events.data.dataSource

import com.bonjur.events.data.DTOs.ClubForEventResponse
import com.bonjur.events.data.DTOs.EventCategorySectionResponse
import com.bonjur.events.data.DTOs.EventCreateRequest
import com.bonjur.events.data.DTOs.EventDetailResponse
import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.events.data.DTOs.EventMembersResponse

/** A picked attachment ready for multipart upload. */
data class EventAttachmentFile(
    val name: String,
    val mimeType: String,
    val bytes: ByteArray
)

interface EventsDataSource {
    suspend fun getEvents(query: Map<String, String>): List<EventListResponse>
    suspend fun getClubsForEvents(): List<ClubForEventResponse>
    suspend fun getCategories(): List<EventCategorySectionResponse>
    suspend fun getEventById(eventId: String): EventDetailResponse
    suspend fun getEventMembers(eventId: String, query: Map<String, String>): EventMembersResponse
    suspend fun createEvent(
        request: EventCreateRequest,
        background: ByteArray?,
        attachments: List<EventAttachmentFile>
    ): EventDetailResponse
    suspend fun editEvent(
        eventId: String,
        request: EventCreateRequest,
        background: ByteArray?,
        attachments: List<EventAttachmentFile>
    ): EventDetailResponse
}
