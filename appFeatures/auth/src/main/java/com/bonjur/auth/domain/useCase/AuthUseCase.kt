package com.bonjur.auth.domain.useCase

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.domain.models.RegisterModel
import javax.inject.Inject

interface AuthUseCase {
    suspend fun register(
        request: RegisterRequest
    ): RegisterModel
}