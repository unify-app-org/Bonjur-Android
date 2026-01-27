package com.bonjur.auth.data.dataSource

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.DTOs.RegisterResponse

interface AuthDataSource {
    suspend fun register(
        body: RegisterRequest
    ): RegisterResponse
}