package com.bonjur.auth.domain.models

data class RegisterModel(
    val token: String,
    val refreshToken: String
)