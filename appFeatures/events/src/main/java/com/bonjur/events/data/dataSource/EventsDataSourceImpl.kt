package com.bonjur.events.data.dataSource

import com.bonjur.events.data.DTOs.ClubForEventResponse
import com.bonjur.events.data.DTOs.EventCategorySectionResponse
import com.bonjur.events.data.DTOs.EventCreateRequest
import com.bonjur.events.data.DTOs.EventDetailResponse
import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.events.data.DTOs.EventMembersResponse
import com.bonjur.events.data.endPoints.EventsEndPoints
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.MultipartFile
import com.bonjur.network.APIClient.MultipartPayload
import com.bonjur.network.APIClient.NetworkService
import com.bonjur.network.model.PageNationResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class EventsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol,
    private val json: Json
) : NetworkService(apiClient), EventsDataSource {

    override suspend fun getEvents(query: Map<String, String>): List<EventListResponse> =
        fetch(EventsEndPoints.GetEvents(query))

    override suspend fun getClubsForEvents(): PageNationResponse<List<ClubForEventResponse>> =
        fetch(EventsEndPoints.ClubsForEvents())

    override suspend fun getCategories(): List<EventCategorySectionResponse> =
        fetch(EventsEndPoints.GetCategories())

    override suspend fun getEventById(eventId: String): EventDetailResponse =
        fetch(EventsEndPoints.GetEventById(eventId))

    override suspend fun getEventMembers(
        eventId: String,
        query: Map<String, String>
    ): EventMembersResponse =
        fetch(EventsEndPoints.GetEventMembers(eventId, query))

    // Mirrors iOS (returns Void): don't decode a typed response — create/edit may
    // return an empty/201 body, and a parse failure would mask success.
    override suspend fun createEvent(
        request: EventCreateRequest,
        background: ByteArray?,
        attachments: List<EventAttachmentFile>
    ): ByteArray =
        fetchRawData(EventsEndPoints.CreateEvent(buildPayload(request, background, attachments)))

    override suspend fun editEvent(
        eventId: String,
        request: EventCreateRequest,
        background: ByteArray?,
        attachments: List<EventAttachmentFile>
    ): ByteArray =
        fetchRawData(EventsEndPoints.EditEvent(eventId, buildPayload(request, background, attachments)))

    override suspend fun joinEvent(eventId: String): ByteArray =
        fetchRawData(EventsEndPoints.JoinEvent(eventId))

    override suspend fun exitEvent(eventId: String): ByteArray =
        fetchRawData(EventsEndPoints.ExitEvent(eventId))

    /** Builds the multipart body mirroring iOS: JSON "request" part + required background + optional attachments. */
    private fun buildPayload(
        request: EventCreateRequest,
        background: ByteArray?,
        attachments: List<EventAttachmentFile>
    ): MultipartPayload = MultipartPayload(
        jsonParts = mapOf("request" to json.encodeToString(request)),
        files = buildList {
            background?.let {
                add(MultipartFile("background", "background.jpg", "image/jpeg", it))
            }
            attachments.forEach { file ->
                add(MultipartFile("attachments", file.name, file.mimeType, file.bytes))
            }
        }
    )
}
