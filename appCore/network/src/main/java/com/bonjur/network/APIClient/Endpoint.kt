package com.bonjur.network.APIClient

import io.ktor.http.*

interface AppEndpoint {
    val path: String
    val method: NetworkMethod
    val headers: Map<String, String>?
        get() = mapOf("Content-Type" to "application/json")
    val requiresAuth: Boolean
        get() = true
    val body: Any?
        get() = null
    val queryParameters: Map<String, String>?
        get() = null
}

enum class NetworkMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
}