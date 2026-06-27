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

    data class GetHangoutMembers(
        val hangoutId: String,
        val page: Int = 0,
        val size: Int = 100,
        val keyword: String? = null
    ) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts/$hangoutId/members"
        override val method = NetworkMethod.GET
        override val queryParameters = mutableMapOf(
            "page" to page.toString(),
            "size" to size.toString()
        ).apply { keyword?.takeIf { it.isNotBlank() }?.let { put("keyword", it) } }
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

    data class GetCategories(val unused: Unit = Unit) : HangoutsEndPoints() {
        override val path = "api/sd/v1/categories"
        override val method = NetworkMethod.GET
    }

    data class JoinHangout(
        val request: com.bonjur.hangouts.data.DTOs.HangoutJoinRequest
    ) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts/join"
        override val method = NetworkMethod.POST
        override val body = request
    }

    data class ExitHangout(val hangoutId: String) : HangoutsEndPoints() {
        override val path = "api/hs/v1/hangouts/exit/$hangoutId"
        override val method = NetworkMethod.DELETE
    }
}