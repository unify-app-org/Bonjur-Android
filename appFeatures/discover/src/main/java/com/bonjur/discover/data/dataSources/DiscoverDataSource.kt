package com.bonjur.discover.data.dataSources

import com.bonjur.discover.data.DTOs.DiscoverCategorySection
import com.bonjur.discover.data.DTOs.DiscoverClub
import com.bonjur.discover.data.DTOs.DiscoverCommunity
import com.bonjur.discover.data.DTOs.DiscoverEvent
import com.bonjur.discover.data.DTOs.DiscoverHangout
import com.bonjur.discover.data.DTOs.DiscoverUserResponse
import com.bonjur.discover.data.DTOs.JoinHangoutRequest

interface DiscoverDataSource {
    suspend fun getHangouts(query: Map<String, String>): List<DiscoverHangout>
    suspend fun getClubs(query: Map<String, String>): List<DiscoverClub>
    suspend fun getEvents(query: Map<String, String>): List<DiscoverEvent>
    suspend fun getCommunities(query: Map<String, String>): List<DiscoverCommunity>
    suspend fun getCategories(): List<DiscoverCategorySection>
    suspend fun getUserById(userId: String): DiscoverUserResponse
    suspend fun joinHangout(request: JoinHangoutRequest): ByteArray
}
