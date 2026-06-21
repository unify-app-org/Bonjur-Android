package com.bonjur.network.APIClient

import com.bonjur.network.AppConfig
import com.bonjur.network.logger.NetworkLogger
import com.bonjur.network.manager.TokenManager
import com.bonjur.network.model.ApiException
import com.bonjur.network.model.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

interface ApiClientProtocol {
    suspend fun <T> request(endpoint: AppEndpoint, serializer: KSerializer<T>): T
    suspend fun requestRawData(endpoint: AppEndpoint): ByteArray
}

@Singleton
class ApiClient @Inject constructor(
    private val client: HttpClient,
    private val json: Json,
    private val tokenManager: TokenManager,
    private val logger: NetworkLogger,
    private val configs: AppConfig
) : ApiClientProtocol {

    override suspend fun <T> request(endpoint: AppEndpoint, serializer: KSerializer<T>): T {
        val data = performRequest(endpoint)

        return try {
            json.decodeFromString(serializer, data)
        } catch (e: SerializationException) {
            throw ApiException.DecodingError(e)
        }
    }

    override suspend fun requestRawData(endpoint: AppEndpoint): ByteArray =
        requestRawData(endpoint, isRetry = false)

    private suspend fun requestRawData(endpoint: AppEndpoint, isRetry: Boolean): ByteArray {
        val url = buildUrl(endpoint)
        var durationMs = 0L

        try {
            val response: HttpResponse

            durationMs = measureTimeMillis {
                response = client.request(url) {
                    method = endpoint.method.toKtor()

                    val multipart = endpoint.multipart

                    endpoint.headers?.forEach { (key, value) ->
                        // Let MultiPartFormDataContent own the Content-Type (boundary).
                        if (multipart != null && key.equals("Content-Type", ignoreCase = true)) {
                            return@forEach
                        }
                        header(key, value)
                    }

                    header("Accept-Language", acceptLanguage())

                    if (endpoint.requiresAuth) {
                        tokenManager.getAccessToken()?.let { token ->
                            header("Authorization", "Bearer $token")
                        }
                    }

                    if (multipart != null) {
                        setBody(multipart.toFormDataContent())
                    } else {
                        endpoint.body?.let {
                            contentType(ContentType.Application.Json)
                            setBody(json.encodeToString(it))
                        }
                    }

                    // Log request
                    logger.logRequest(
                        this,
                        multipart?.let { "[multipart: ${it.files.size} file(s)]" }
                            ?: endpoint.body?.let { json.encodeToString(it) }
                    )
                }
            }

            val bytes = response.body<ByteArray>()

            val isOk = response.status.value in 200..299
            logger.logResponse(
                response = response,
                // Decode error bodies so the failure reason is visible; keep success raw.
                bodyText = if (isOk) "[Binary data: ${bytes.size} bytes]"
                else runCatching { bytes.decodeToString() }.getOrElse { "[${bytes.size} bytes]" },
                durationMs = durationMs
            )

            when (response.status.value) {
                in 200..299 -> return bytes

                401 -> {
                    if (!isRetry && endpoint.requiresAuth) {
                        refreshTokenIfNeeded()
                        return requestRawData(endpoint, isRetry = true)
                    }
                    throw decodeError(bytes) ?: ApiException.Unauthorized
                }

                else -> throw decodeError(bytes) ?: ApiException.Unknown
            }

        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            logger.logError(e, url)
            throw ApiException.NetworkException(e)
        }
    }

    private fun decodeError(bytes: ByteArray): ApiException? =
        try {
            ApiException.ServerError(json.decodeFromString<NetworkError>(bytes.decodeToString()))
        } catch (e: Exception) {
            null
        }

    private suspend fun performRequest(
        endpoint: AppEndpoint,
        isRetry: Boolean = false
    ): String {
        val url = buildUrl(endpoint)
        var durationMs = 0L

        try {
            val response: HttpResponse

            durationMs = measureTimeMillis {
                response = client.request(url) {
                    method = endpoint.method.toKtor()

                    val multipart = endpoint.multipart

                    endpoint.headers?.forEach { (key, value) ->
                        // Let MultiPartFormDataContent own the Content-Type (boundary).
                        if (multipart != null && key.equals("Content-Type", ignoreCase = true)) {
                            return@forEach
                        }
                        header(key, value)
                    }

                    header("Accept-Language", acceptLanguage())

                    if (endpoint.requiresAuth) {
                        tokenManager.getAccessToken()?.let { token ->
                            header("Authorization", "Bearer $token")
                        }
                    }

                    if (multipart != null) {
                        setBody(multipart.toFormDataContent())
                    } else {
                        endpoint.body?.let {
                            contentType(ContentType.Application.Json)
                            setBody(it)
                        }
                    }

                    // Log request
                    logger.logRequest(
                        this,
                        multipart?.let { "[multipart: ${it.files.size} file(s)]" }
                            ?: endpoint.body?.toString()
                    )
                }
            }

            val bodyText = response.bodyAsText()

            logger.logResponse(
                response = response,
                bodyText = bodyText,
                durationMs = durationMs
            )

            when (response.status.value) {
                in 200..299 -> {
                    return bodyText
                }

                401 -> {
                    if (!isRetry && endpoint.requiresAuth) {
                        refreshTokenIfNeeded()
                        return performRequest(endpoint, isRetry = true)
                    } else {
                        val networkError = try {
                            json.decodeFromString<NetworkError>(bodyText)
                        } catch (e: Exception) {
                            null
                        }

                        val exception = networkError?.let { ApiException.ServerError(it) }
                            ?: ApiException.Unauthorized

                        logger.logError(
                            exception,
                            url,
                            statusCode = response.status.value,
                            errorBody = bodyText
                        )

                        throw exception
                    }
                }

                in 400..499 -> {
                    val networkError = try {
                        json.decodeFromString<NetworkError>(bodyText)
                    } catch (e: Exception) {
                        null
                    }

                    val exception = networkError?.let { ApiException.ServerError(it) }
                        ?: ApiException.Unknown

                    logger.logError(
                        exception,
                        url,
                        statusCode = response.status.value,
                        errorBody = bodyText
                    )

                    throw exception
                }

                in 500..599 -> {
                    val networkError = try {
                        json.decodeFromString<NetworkError>(bodyText)
                    } catch (e: Exception) {
                        null
                    }

                    val exception = networkError?.let { ApiException.ServerError(it) }
                        ?: ApiException.Unknown

                    logger.logError(
                        exception,
                        url,
                        statusCode = response.status.value,
                        errorBody = bodyText
                    )

                    throw exception
                }

                else -> {
                    val exception = ApiException.Unknown
                    logger.logError(
                        exception,
                        url,
                        statusCode = response.status.value,
                        errorBody = bodyText
                    )
                    throw exception
                }
            }

        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            logger.logError(e, url, statusCode = null, errorBody = null)
            throw ApiException.NetworkException(e)
        }
    }

    private fun buildUrl(endpoint: AppEndpoint): String {
        val url = configs.apiBaseUrl + endpoint.path

        return if (endpoint.queryParameters != null) {
            val params = endpoint.queryParameters!!
                .map { "${it.key}=${it.value}" }
                .joinToString("&")
            "$url?$params"
        } else {
            url
        }
    }

    private suspend fun refreshTokenIfNeeded() {
        val refreshToken = tokenManager.getRefreshToken()
            ?: throw ApiException.Unauthorized

        try {
            // Create refresh endpoint
            val refreshEndpoint = object : AppEndpoint {
                override val path = "/auth/refresh"
                override val method = NetworkMethod.POST
                override val requiresAuth = false
                override val body = mapOf("refreshToken" to refreshToken)
            }

            @Serializable
            data class RefreshTokenData(
                val accessToken: String,
                val refreshToken: String
            )

            val newTokens: RefreshTokenData = request(refreshEndpoint, serializer())

            tokenManager.saveAccessToken(newTokens.accessToken)
            tokenManager.saveRefreshToken(newTokens.refreshToken)

        } catch (e: Exception) {
            tokenManager.clearTokens()
            throw ApiException.Unauthorized
        }
    }

    /** Restrict device locale to supported languages; default to English. */
    private fun acceptLanguage(): String {
        val lang = java.util.Locale.getDefault().language.lowercase()
        return if (lang in setOf("az", "en", "ru")) lang else "en"
    }

    private fun MultipartPayload.toFormDataContent(): MultiPartFormDataContent =
        MultiPartFormDataContent(
            formData {
                jsonParts.forEach { (name, jsonString) ->
                    append(
                        name,
                        jsonString,
                        Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        }
                    )
                }
                files.forEach { file ->
                    // Ktor's formData() auto-adds `Content-Disposition: form-data; name=<key>`;
                    // only append filename here (adding a full disposition duplicates it and
                    // the server fails to parse the multipart body).
                    append(
                        file.name,
                        file.bytes,
                        Headers.build {
                            append(HttpHeaders.ContentType, file.mimeType)
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${file.fileName}\""
                            )
                        }
                    )
                }
            }
        )

    internal fun NetworkMethod.toKtor(): HttpMethod =
        when (this) {
            NetworkMethod.GET -> HttpMethod.Get
            NetworkMethod.POST -> HttpMethod.Post
            NetworkMethod.PUT -> HttpMethod.Put
            NetworkMethod.PATCH -> HttpMethod.Patch
            NetworkMethod.DELETE -> HttpMethod.Delete
        }

}