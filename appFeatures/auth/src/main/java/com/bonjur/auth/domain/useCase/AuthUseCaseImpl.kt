package com.bonjur.auth.domain.useCase

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.domain.models.RegisterModel
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    val dataSource: AuthDataSource
): AuthUseCase {
    override suspend fun register(
        request: RegisterRequest
    ): RegisterModel {
        val response = dataSource.register(
            request
        )
        return RegisterModel(
            response.token,
            response.refreshToken
        )
    }
}