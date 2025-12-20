package com.bonjur.auth.data.dataSource

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.DTOs.RegisterResponse
import com.bonjur.auth.data.endPoint.AuthEndpoint
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

interface AuthDataSource {
    suspend fun register(
        body: RegisterRequest
    ): RegisterResponse
}