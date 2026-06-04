package com.bonjur.auth.data.dataSource

import com.bonjur.auth.data.DTOs.CategoryResponse
import com.bonjur.auth.data.DTOs.CommunityResponse
import com.bonjur.auth.data.DTOs.LanguageResponse
import com.bonjur.auth.data.DTOs.LoginRequest
import com.bonjur.auth.data.DTOs.LoginResponse
import com.bonjur.auth.data.endPoint.AuthEndpoint
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), AuthDataSource {

    override suspend fun login(body: LoginRequest): LoginResponse =
        fetch(AuthEndpoint.Login(body))

    override suspend fun getCommunities(): List<CommunityResponse> =
        fetch(AuthEndpoint.GetCommunities)

    override suspend fun getLanguages(): List<LanguageResponse> =
        fetch(AuthEndpoint.GetLanguages)

    override suspend fun getCategories(): List<CategoryResponse> =
        fetch(AuthEndpoint.GetCategories)
}
