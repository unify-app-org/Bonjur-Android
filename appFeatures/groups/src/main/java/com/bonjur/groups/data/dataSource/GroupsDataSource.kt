package com.bonjur.groups.data.dataSource

import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.hangouts.data.DTOs.HangoutListResponse

interface GroupsDataSource {
    suspend fun fetchJoinedClubs(): List<ClubListResponse>
    suspend fun fetchJoinedHangouts(): List<HangoutListResponse>
}
