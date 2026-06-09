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
    /** When non-null, the request is sent as multipart/form-data instead of JSON. */
    val multipart: MultipartPayload?
        get() = null
}

/**
 * Multipart/form-data payload. Mirrors iOS `MultipartFormData`:
 * [jsonParts] are appended as `application/json` form fields (e.g. "request"),
 * [files] are appended as binary file parts (e.g. "clubProfile", "backgroundPhoto").
 */
data class MultipartPayload(
    val jsonParts: Map<String, String> = emptyMap(),
    val files: List<MultipartFile> = emptyList()
)

data class MultipartFile(
    val name: String,
    val fileName: String,
    val mimeType: String,
    val bytes: ByteArray
)

enum class NetworkMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
}