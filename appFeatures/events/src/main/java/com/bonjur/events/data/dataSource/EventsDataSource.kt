package com.bonjur.events.data.dataSource

import com.bonjur.events.data.DTOs.EventCreateRequest
import com.bonjur.events.data.DTOs.EventDetailResponse
import com.bonjur.events.data.DTOs.EventListResponse

interface EventsDataSource {
    suspend fun getEvents(query: Map<String, String>): List<EventListResponse>
    suspend fun getEventById(eventId: String): EventDetailResponse
    suspend fun createEvent(request: EventCreateRequest): EventDetailResponse
    suspend fun editEvent(eventId: String, request: EventCreateRequest): EventDetailResponse
}