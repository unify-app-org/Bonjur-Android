package com.bonjur.clubs.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class ClubsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), ClubsDataSource {

    override suspend fun getClubs(query: Map<String, String>): List<com.bonjur.clubs.data.DTOs.ClubListResponse> =
        fetch(com.bonjur.clubs.data.endPoints.ClubsEndpoints.GetClubs(query))

    override suspend fun getClubById(clubId: Int): com.bonjur.clubs.data.DTOs.ClubDetailResponse =
        fetch(com.bonjur.clubs.data.endPoints.ClubsEndpoints.GetClubById(clubId))

    override suspend fun getClubMembers(clubId: Int): com.bonjur.clubs.data.DTOs.ClubMemberResponse =
        fetch(com.bonjur.clubs.data.endPoints.ClubsEndpoints.GetClubMembers(clubId))

    override suspend fun createClub(request: com.bonjur.clubs.data.DTOs.ClubCreateRequest): com.bonjur.clubs.data.DTOs.ClubDetailResponse =
        fetch(com.bonjur.clubs.data.endPoints.ClubsEndpoints.CreateClub(request))

    override suspend fun editClub(clubId: Int, request: com.bonjur.clubs.data.DTOs.ClubCreateRequest): com.bonjur.clubs.data.DTOs.ClubDetailResponse =
        fetch(com.bonjur.clubs.data.endPoints.ClubsEndpoints.EditClub(clubId, request))

    override suspend fun joinClub(clubId: Int): ByteArray =
        fetchRawData(com.bonjur.clubs.data.endPoints.ClubsEndpoints.JoinClub(clubId))
}