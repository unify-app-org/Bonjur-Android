package com.bonjur.groups.data.dataSource

import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.groups.data.DTOs.GroupsClubResponse
import com.bonjur.groups.data.DTOs.GroupsHangoutResponse

interface GroupsDataSource {
    suspend fun fetchJoinedClubs(query: Map<String, String>): List<GroupsClubResponse>
    suspend fun fetchJoinedHangouts(query: Map<String, String>): List<GroupsHangoutResponse>
    suspend fun fetchJoinedEvents(query: Map<String, String>): List<EventListResponse>
}
