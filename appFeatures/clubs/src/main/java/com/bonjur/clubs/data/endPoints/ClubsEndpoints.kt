package com.bonjur.clubs.data.endPoints

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class ClubsEndpoints : AppEndpoint {

    data class GetClubs(val query: Map<String, String>) : ClubsEndpoints() {
        override val path = "api/ds/v1/clubs"
        override val method = NetworkMethod.GET
        override val queryParameters = query
    }

    data class GetClubById(val clubId: Int) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId"
        override val method = NetworkMethod.GET
    }

    data class GetClubMembers(val clubId: Int) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId/members"
        override val method = NetworkMethod.GET
    }

    data class CreateClub(val request: com.bonjur.clubs.data.DTOs.ClubCreateRequest) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs"
        override val method = NetworkMethod.POST
        override val body = request
    }

    data class EditClub(val clubId: Int, val request: com.bonjur.clubs.data.DTOs.ClubCreateRequest) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId"
        override val method = NetworkMethod.PUT
        override val body = request
    }

    data class JoinClub(val clubId: Int) : ClubsEndpoints() {
        override val path = "api/cs/v1/clubs/$clubId/join-club"
        override val method = NetworkMethod.POST
    }
}