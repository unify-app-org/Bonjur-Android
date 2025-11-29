package com.bonjur.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val status: String,
    val message: String,
    val data: T
)

@Serializable
class EmptyData