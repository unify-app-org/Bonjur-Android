package com.bonjur.auth.data.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String = ""
)

data class RegisterResponse(
    val token: String,
    val refreshToken: String
)