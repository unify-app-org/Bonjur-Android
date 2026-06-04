package com.bonjur.clubs.data.dataSource

import com.bonjur.clubs.data.DTOs.ClubCreateRequest
import com.bonjur.clubs.data.DTOs.ClubDetailResponse
import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.clubs.data.DTOs.ClubMemberResponse

interface ClubsDataSource {
    suspend fun getClubs(query: Map<String, String>): List<ClubListResponse>
    suspend fun getClubById(clubId: Int): ClubDetailResponse
    suspend fun getClubMembers(clubId: Int): ClubMemberResponse
    suspend fun createClub(request: ClubCreateRequest): ClubDetailResponse
    suspend fun editClub(clubId: Int, request: ClubCreateRequest): ClubDetailResponse
    suspend fun joinClub(clubId: Int): ByteArray
}