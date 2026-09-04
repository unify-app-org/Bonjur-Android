package com.bonjur.groups.data.dataSource

import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.groups.data.DTOs.GroupsClubResponse
import com.bonjur.groups.data.DTOs.GroupsHangoutResponse
import com.bonjur.network.model.PageNationResponse

interface GroupsDataSource {
    suspend fun fetchJoinedClubs(query: Map<String, String>): PageNationResponse<List<GroupsClubResponse>>
    suspend fun fetchJoinedHangouts(query: Map<String, String>): PageNationResponse<List<GroupsHangoutResponse>>
    suspend fun fetchJoinedEvents(query: Map<String, String>): PageNationResponse<List<EventListResponse>>
}
