package com.bonjur.network.APIClient

import kotlinx.serialization.serializer

abstract class NetworkService(
    val apiClient: ApiClientProtocol
) {

    suspend inline fun <reified T> fetch(endpoint: AppEndpoint): T {
        return apiClient.request(endpoint, serializer())
    }

    suspend fun fetchRawData(endpoint: AppEndpoint): ByteArray {
        return apiClient.requestRawData(endpoint)
    }
}
