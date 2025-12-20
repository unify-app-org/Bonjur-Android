package com.bonjur.auth.data.dataSource

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.DTOs.RegisterResponse
import com.bonjur.auth.data.endPoint.AuthEndpoint
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), AuthDataSource {

    override suspend fun register(
        body: RegisterRequest
    ): RegisterResponse {
        return fetch(
            AuthEndpoint.Register(body)
        )
    }
}