package com.bonjur.network.APIClient

abstract class NetworkService<EndpointType : AppEndpoint>(
     val apiClient: ApiClientProtocol
) {

    suspend inline fun <reified T> fetch(endpoint: EndpointType): T {
        return apiClient.request(endpoint)
    }

    suspend fun fetchRawData(endpoint: EndpointType): ByteArray {
        return apiClient.requestRawData(endpoint)
    }
}