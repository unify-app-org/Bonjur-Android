package com.bonjur.events.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class EventsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), EventsDataSource {

    override suspend fun getEvents(query: Map<String, String>): List<com.bonjur.events.data.DTOs.EventListResponse> =
        fetch(com.bonjur.events.data.endPoints.EventsEndPoints.GetEvents(query))

    override suspend fun getEventById(eventId: String): com.bonjur.events.data.DTOs.EventDetailResponse =
        fetch(com.bonjur.events.data.endPoints.EventsEndPoints.GetEventById(eventId))

    override suspend fun createEvent(request: com.bonjur.events.data.DTOs.EventCreateRequest): com.bonjur.events.data.DTOs.EventDetailResponse =
        fetch(com.bonjur.events.data.endPoints.EventsEndPoints.CreateEvent(request))

    override suspend fun editEvent(eventId: String, request: com.bonjur.events.data.DTOs.EventCreateRequest): com.bonjur.events.data.DTOs.EventDetailResponse =
        fetch(com.bonjur.events.data.endPoints.EventsEndPoints.EditEvent(eventId, request))
}