package com.bonjur.discover.domain.models

data class UserModel(
    val id: Int,
    val name: String,
    val profileImage: String?,
    val greeting: String
)
