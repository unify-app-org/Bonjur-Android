package com.bonjur.network.APIClient

import com.bonjur.network.AppConfig
import com.bonjur.storage.language.AppLanguageStore
import com.bonjur.network.logger.NetworkLogger
import com.bonjur.network.manager.SessionEvents
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val configs: AppConfig,
    private val sessionEvents: SessionEvents
) : ApiClientProtocol {

    /** Serialises `auth/refresh` so a burst of parallel 401s refreshes once, not N times. */
    private val refreshMutex = Mutex()

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
        val sentToken = if (endpoint.requiresAuth) tokenManager.getAccessToken() else null

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

                    sentToken?.let { token ->
                        header("Authorization", "Bearer $token")
                    }

                    if (multipart != null) {
                        setBody(multipart.toFormDataContent())
                    } else {
                        endpoint.body?.let {
                            contentType(ContentType.Application.Json)
                            // setBody(it) lets ktor serialize by RUNTIME type. encodeToString(it)
                            // would serialize the compile-time `Any` → "Serializer for class 'Any'".
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
                        refreshTokenIfNeeded(staleAccessToken = sentToken)
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
        val sentToken = if (endpoint.requiresAuth) tokenManager.getAccessToken() else null

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

                    sentToken?.let { token ->
                        header("Authorization", "Bearer $token")
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
                        refreshTokenIfNeeded(staleAccessToken = sentToken)
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

    /**
     * Swaps the refresh token for a new pair so the caller can replay the 401'd request —
     * mirrors iOS `APIClient.refreshTokenIfNeeded()`.
     *
     * [staleAccessToken] is the token the failed request was sent with. The backend rotates
     * the refresh token on every refresh, so when several requests 401 together only the
     * first may call `auth/refresh`; the rest wait on [refreshMutex], see the access token
     * has already moved on, and just replay. Refreshing again with the spent refresh token
     * would be rejected and log the user out for no reason.
     */
    private suspend fun refreshTokenIfNeeded(staleAccessToken: String?) = refreshMutex.withLock {
        val current = tokenManager.getAccessToken()
        if (current != null && current != staleAccessToken) return@withLock

        val refreshToken = tokenManager.getRefreshToken()
            ?: throw failRefresh()

        val refreshEndpoint = object : AppEndpoint {
            // Same route as iOS `RefreshEndpoint`. This used to be `/auth/refresh`, which
            // doesn't exist behind the gateway, so every refresh failed, the tokens were
            // wiped and the original request was never replayed.
            override val path = "api/as/v1/auth/refresh"
            override val method = NetworkMethod.POST
            override val requiresAuth = false
            override val body = RefreshTokenRequest(refreshToken)
        }

        val newTokens: RefreshTokenResponse = try {
            request(refreshEndpoint, serializer())
        } catch (e: ApiException.NetworkException) {
            // No connectivity says nothing about the session — keep it and let the caller
            // surface a network error instead of bouncing the user to onboarding.
            throw e
        } catch (e: Exception) {
            throw failRefresh()
        }

        tokenManager.saveAccessToken(newTokens.accessToken)
        tokenManager.saveRefreshToken(newTokens.refreshToken)
    }

    /** Tears the session down, tells the app shell to show onboarding, returns the error to throw. */
    private fun failRefresh(): ApiException {
        tokenManager.clearTokens()
        sessionEvents.notifyExpired()
        return ApiException.Unauthorized
    }

    @Serializable
    internal data class RefreshTokenRequest(val refreshToken: String)

    @Serializable
    internal data class RefreshTokenResponse(
        val accessToken: String,
        val refreshToken: String
    )

    /** Restrict device locale to supported languages; default to English. */
    // The in-app language, not `Locale.getDefault()`: Android re-applies the device
    // configuration when an Activity is created, which used to clobber the stored
    // choice and send `Accept-Language: en` on every cold start.
    private fun acceptLanguage(): String = AppLanguageStore.code

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