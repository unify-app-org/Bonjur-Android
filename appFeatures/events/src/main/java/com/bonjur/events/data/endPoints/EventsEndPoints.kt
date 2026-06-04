package com.bonjur.events.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class EventsEndPoints : AppEndpoint {

    data class GetEvents(val query: Map<String, String>) : EventsEndPoints() {
        override val path = "api/ds/v1/events"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetEventById(val eventId: String) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId"
        override val method = NetworkMethod.GET
    }

    data class CreateEvent(
        val request: com.bonjur.events.data.DTOs.EventCreateRequest
    ) : EventsEndPoints() {
        override val path = "api/es/v1/events"
        override val method = NetworkMethod.POST
        override val body = request
    }

    data class EditEvent(
        val eventId: String,
        val request: com.bonjur.events.data.DTOs.EventCreateRequest
    ) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId"
        override val method = NetworkMethod.PUT
        override val body = request
    }
}