package com.bonjur.network.APIClient

import javax.inject.Inject

abstract class NetworkService(
    val apiClient: ApiClientProtocol
) {

    suspend inline fun <reified T> fetch(endpoint: AppEndpoint): T {
        return apiClient.request(endpoint)
    }

    suspend fun fetchRawData(endpoint: AppEndpoint): ByteArray {
        return apiClient.requestRawData(endpoint)
    }
}
