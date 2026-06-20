package com.bonjur.clubs.data.dataSource

import com.bonjur.clubs.data.DTOs.CategorySectionResponse
import com.bonjur.clubs.data.DTOs.ClubCreateRequest
import com.bonjur.clubs.data.DTOs.ClubDetailResponse
import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.clubs.data.DTOs.ClubMemberResponse
import com.bonjur.clubs.data.DTOs.RoleAssignRequest

interface ClubsDataSource {
    suspend fun getClubs(query: Map<String, String>): List<ClubListResponse>
    suspend fun getCategories(): List<CategorySectionResponse>
    suspend fun getClubById(clubId: Int): ClubDetailResponse
    suspend fun getClubMembers(clubId: Int): ClubMemberResponse
    suspend fun createClub(
        request: ClubCreateRequest,
        logo: ByteArray?,
        cover: ByteArray?
    ): ClubDetailResponse
    suspend fun editClub(
        clubId: Int,
        request: ClubCreateRequest,
        logo: ByteArray?,
        cover: ByteArray?
    ): ClubDetailResponse
    suspend fun joinClub(clubId: Int): ByteArray
    suspend fun exitClub(clubId: Int): ByteArray
    suspend fun assignRole(clubId: Int, request: RoleAssignRequest): ByteArray
}