package com.bonjur.events.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.MultipartPayload
import com.bonjur.network.APIClient.NetworkMethod

sealed class EventsEndPoints : AppEndpoint {

    data class GetEvents(val query: Map<String, String>) : EventsEndPoints() {
        override val path = "api/ds/v1/events"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class ClubsForEvents(val unused: Unit = Unit) : EventsEndPoints() {
        override val path = "api/cs/v1/clubs/forEvents"
        override val method = NetworkMethod.GET
    }

    data class GetCategories(val unused: Unit = Unit) : EventsEndPoints() {
        override val path = "api/sd/v1/categories"
        override val method = NetworkMethod.GET
    }

    data class GetEventById(val eventId: String) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId"
        override val method = NetworkMethod.GET
    }

    data class GetEventMembers(
        val eventId: String,
        val query: Map<String, String>
    ) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId/members"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetClubEvents(
        val clubId: Int,
        val query: Map<String, String>
    ) : EventsEndPoints() {
        override val path = "api/es/v1/events/$clubId/events"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class CreateEvent(val payload: MultipartPayload) : EventsEndPoints() {
        override val path = "api/es/v1/events"
        override val method = NetworkMethod.POST
        override val headers: Map<String, String>? = null
        override val multipart = payload
    }

    data class EditEvent(
        val eventId: String,
        val payload: MultipartPayload
    ) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId"
        override val method = NetworkMethod.PUT
        override val headers: Map<String, String>? = null
        override val multipart = payload
    }

    data class JoinEvent(val eventId: String) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId/join"
        override val method = NetworkMethod.POST
    }

    data class ExitEvent(val eventId: String) : EventsEndPoints() {
        override val path = "api/es/v1/events/$eventId/exit"
        override val method = NetworkMethod.DELETE
    }
}
