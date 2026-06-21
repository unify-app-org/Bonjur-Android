package com.bonjur.discover.data.dataSources

import com.bonjur.discover.data.DTOs.DiscoverCategorySection
import com.bonjur.discover.data.DTOs.DiscoverClub
import com.bonjur.discover.data.DTOs.DiscoverCommunity
import com.bonjur.discover.data.DTOs.DiscoverEvent
import com.bonjur.discover.data.DTOs.DiscoverHangout
import com.bonjur.discover.data.DTOs.DiscoverUserResponse
import com.bonjur.discover.data.DTOs.JoinHangoutRequest
import com.bonjur.discover.data.endPoints.DiscoverEndPoints
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class DiscoverDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), DiscoverDataSource {

    override suspend fun getHangouts(query: Map<String, String>): List<DiscoverHangout> =
        fetch(DiscoverEndPoints.GetHangouts(query))

    override suspend fun getClubs(query: Map<String, String>): List<DiscoverClub> =
        fetch(DiscoverEndPoints.GetClubs(query))

    override suspend fun getEvents(query: Map<String, String>): List<DiscoverEvent> =
        fetch(DiscoverEndPoints.GetEvents(query))

    override suspend fun getCommunities(query: Map<String, String>): List<DiscoverCommunity> =
        fetch(DiscoverEndPoints.GetCommunities(query))

    override suspend fun getCategories(): List<DiscoverCategorySection> =
        fetch(DiscoverEndPoints.GetCategories)

    override suspend fun getUserById(userId: String): DiscoverUserResponse =
        fetch(DiscoverEndPoints.GetUserById(userId))

    override suspend fun joinHangout(request: JoinHangoutRequest): ByteArray =
        fetchRawData(DiscoverEndPoints.JoinHangout(request))
}
