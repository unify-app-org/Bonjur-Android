package com.bonjur.clubs.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.MultipartPayload
import com.bonjur.network.APIClient.NetworkMethod

sealed class ClubsEndpoints : AppEndpoint {

    data class GetClubs(val query: Map<String, String>) : ClubsEndpoints() {
        override val path = "api/ds/v1/clubs"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetCategories(val unused: Unit = Unit) : ClubsEndpoints() {
        override val path = "api/sd/v1/categories"
        override val method = NetworkMethod.GET
    }

    data class GetClubById(val clubId: Int) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId"
        override val method = NetworkMethod.GET
    }

    data class GetClubMembers(val clubId: Int) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId/members"
        override val method = NetworkMethod.GET
    }

    data class CreateClub(val payload: MultipartPayload) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs"
        override val method = NetworkMethod.POST
        override val headers: Map<String, String>? = null
        override val multipart = payload
    }

    data class EditClub(val clubId: Int, val payload: MultipartPayload) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId"
        override val method = NetworkMethod.PUT
        override val headers: Map<String, String>? = null
        override val multipart = payload
    }

    data class JoinClub(val clubId: Int) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId/join-club"
        override val method = NetworkMethod.POST
    }
}