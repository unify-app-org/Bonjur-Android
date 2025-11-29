package com.bonjur.network.model

import kotlinx.serialization.Serializable


@Serializable
data class NetworkError(
    val status: String,
    val message: String,
    val detail: String? = null,
    val errors: Map<String, List<String>>? = null
)

sealed class ApiException(message: String) : Exception(message) {
    data class ServerError(val error: NetworkError) : ApiException(error.message)
    object Unauthorized: ApiException("Unauthorized. Please login again.")
    data class NetworkException(val throwable: Throwable) : ApiException(throwable.message ?: "Network error")
    data class DecodingError(val throwable: Throwable) : ApiException("Failed to decode response")
    object InvalidURL : ApiException("Invalid URL")
    object NoData : ApiException("No data received")
    object Unknown : ApiException("Unknown error occurred")
}