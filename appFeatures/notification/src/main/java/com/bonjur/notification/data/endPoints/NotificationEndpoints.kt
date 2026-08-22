package com.bonjur.notification.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod
import com.bonjur.notification.data.DTOs.ClubStatusRequest
import com.bonjur.notification.data.DTOs.ClubVerificationRequest
import com.bonjur.notification.data.DTOs.EventStatusRequest
import com.bonjur.notification.data.DTOs.HangoutStatusRequest

/**
 * Notification-service endpoints. Mirrors iOS `NotificationEndPoint`.
 * The notification feed itself is still mock; only the join-request +
 * verification flows are live.
 */
sealed class NotificationEndpoints : AppEndpoint {

    /** Paged notification feed for the current user. */
    data class Feed(val query: Map<String, String>) : NotificationEndpoints() {
        override val path = "api/ns/v1/notifications"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    /** Unread notification total (bell badge). */
    data object UnreadCount : NotificationEndpoints() {
        override val path = "api/ns/v1/notifications/unread-count"
        override val method = NetworkMethod.GET
    }

    /** Marks every notification read. */
    data object ReadAll : NotificationEndpoints() {
        override val path = "api/ns/v1/notifications/read-all"
        override val method = NetworkMethod.POST
    }

    /** Marks one notification read (row tap). */
    data class Read(val notificationId: String) : NotificationEndpoints() {
        override val path = "api/ns/v1/notifications/read/$notificationId"
        override val method = NetworkMethod.POST
    }

    data class ClubJoinRequests(val query: Map<String, String>) : NotificationEndpoints() {
        override val path = "api/cs/v1/clubs/join-requests"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class HangoutJoinRequests(val query: Map<String, String>) : NotificationEndpoints() {
        override val path = "api/hs/v1/hangouts/join-requests"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    /** Accept/reject a club join request. status ∈ {ACCEPT, REJECT}. */
    data class SetClubRequestStatus(val request: ClubStatusRequest) : NotificationEndpoints() {
        override val path = "api/cs/v1/clubs/join-requests/status"
        override val method = NetworkMethod.POST
        override val body = request
    }

    /** Accept/reject a hangout join request. status ∈ {ACCEPTED, REJECTED}. */
    data class SetHangoutRequestStatus(
        val hangoutId: String,
        val request: HangoutStatusRequest
    ) : NotificationEndpoints() {
        override val path = "api/hs/v1/hangouts/requests/$hangoutId"
        override val method = NetworkMethod.POST
        override val body = request
    }

    /** Pending event join requests for events the caller organizes. */
    data class EventJoinRequests(val query: Map<String, String>) : NotificationEndpoints() {
        override val path = "api/es/v1/events/requests"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    /** Accept/reject an event join request. status ∈ {ACCEPTED, REJECTED}. */
    data class SetEventRequestStatus(
        val eventId: String,
        val request: EventStatusRequest
    ) : NotificationEndpoints() {
        override val path = "api/es/v1/events/$eventId"
        override val method = NetworkMethod.POST
        override val body = request
    }

    /** Admin: clubs awaiting verification. */
    data class ClubPending(val query: Map<String, String>) : NotificationEndpoints() {
        override val path = "api/cs/v1/clubs/pending"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    /** Admin: approve/reject a club's verification. status ∈ {ACCEPT, REJECT}. */
    data class SetClubVerification(val request: ClubVerificationRequest) : NotificationEndpoints() {
        override val path = "api/cs/v1/clubs/status"
        override val method = NetworkMethod.POST
        override val body = request
    }
}
