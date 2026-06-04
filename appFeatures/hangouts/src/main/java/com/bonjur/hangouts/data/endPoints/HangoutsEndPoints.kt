package com.bonjur.hangouts.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class HangoutsEndPoints : AppEndpoint {

    data class GetHangouts(val query: Map<String, String>) : HangoutsEndPoints() {
        override val path = "api/ds/v1/hangouts"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetHangoutById(val hangoutId: String) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts/$hangoutId"
        override val method = NetworkMethod.GET
    }

    data class GetHangoutMembers(val hangoutId: String) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts/$hangoutId/members"
        override val method = NetworkMethod.GET
    }

    data class CreateHangout(
        val request: com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
    ) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts"
        override val method = NetworkMethod.POST
        override val body = request
    }

    data class EditHangout(
        val hangoutId: String,
        val request: com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
    ) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts/$hangoutId"
        override val method = NetworkMethod.PUT
        override val body = request
    }
}