package com.bonjur.clubs.data.dataSource

import com.bonjur.clubs.data.DTOs.CategorySectionResponse
import com.bonjur.clubs.data.DTOs.ClubCreateRequest
import com.bonjur.clubs.data.DTOs.ClubDetailResponse
import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.clubs.data.DTOs.ClubMemberResponse
import com.bonjur.clubs.data.endPoints.ClubsEndpoints
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.MultipartFile
import com.bonjur.network.APIClient.MultipartPayload
import com.bonjur.network.APIClient.NetworkService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ClubsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol,
    private val json: Json
) : NetworkService(apiClient), ClubsDataSource {

    override suspend fun getClubs(query: Map<String, String>): List<ClubListResponse> =
        fetch(ClubsEndpoints.GetClubs(query))

    override suspend fun getCategories(): List<CategorySectionResponse> =
        fetch(ClubsEndpoints.GetCategories())

    override suspend fun getClubById(clubId: Int): ClubDetailResponse =
        fetch(ClubsEndpoints.GetClubById(clubId))

    override suspend fun getClubMembers(clubId: Int): ClubMemberResponse =
        fetch(ClubsEndpoints.GetClubMembers(clubId))

    override suspend fun createClub(
        request: ClubCreateRequest,
        logo: ByteArray?,
        cover: ByteArray?
    ): ClubDetailResponse =
        fetch(ClubsEndpoints.CreateClub(buildPayload(request, logo, cover)))

    override suspend fun editClub(
        clubId: Int,
        request: ClubCreateRequest,
        logo: ByteArray?,
        cover: ByteArray?
    ): ClubDetailResponse =
        fetch(ClubsEndpoints.EditClub(clubId, buildPayload(request, logo, cover)))

    override suspend fun joinClub(clubId: Int): ByteArray =
        fetchRawData(ClubsEndpoints.JoinClub(clubId))

    /** Builds the multipart body mirroring iOS: a JSON "request" part plus optional image files. */
    private fun buildPayload(
        request: ClubCreateRequest,
        logo: ByteArray?,
        cover: ByteArray?
    ): MultipartPayload = MultipartPayload(
        jsonParts = mapOf("request" to json.encodeToString(request)),
        files = buildList {
            logo?.let {
                add(MultipartFile("clubProfile", "clubProfile.jpg", "image/jpeg", it))
            }
            cover?.let {
                add(MultipartFile("backgroundPhoto", "backgroundPhoto.jpg", "image/jpeg", it))
            }
        }
    )
}
