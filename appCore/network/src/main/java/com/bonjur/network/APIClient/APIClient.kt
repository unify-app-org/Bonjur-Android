package com.bonjur.network.APIClient

import com.bonjur.network.model.ApiException
import com.bonjur.network.model.BaseResponse
import com.bonjur.network.model.NetworkError
import com.bonjur.network.logger.NetworkLogger
import com.bonjur.network.manager.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis
import io.ktor.http.contentType
import io.ktor.http.ContentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

interface ApiClientProtocol {
    suspend fun <T> request(endpoint: AppEndpoint): T
    suspend fun requestRawData(endpoint: AppEndpoint): ByteArray
}

@Singleton
class ApiClient @Inject constructor(
    private val client: HttpClient,
    private val json: Json,
    private val tokenManager: TokenManager,
    private val logger: NetworkLogger,
    private val baseUrl: String = "YOUR_BASE_URL" // Inject from BuildConfig or DI
) : ApiClientProtocol {

    override suspend fun <T> request(endpoint: AppEndpoint): T {
        val data = performRequest(endpoint)

        return try {
            val response = json.decodeFromString<BaseResponse<T>>(data)
            response.data
        } catch (e: SerializationException) {
            throw ApiException.DecodingError(e)
        }
    }

    override suspend fun requestRawData(endpoint: AppEndpoint): ByteArray {
        val url = buildUrl(endpoint)
        var durationMs = 0L

        try {
            val response: HttpResponse

            durationMs = measureTimeMillis {
                response = client.request(url) {
                    method = endpoint.method

                    endpoint.headers?.forEach { (key, value) ->
                        header(key, value)
                    }

                    if (endpoint.requiresAuth) {
                        tokenManager.getAccessToken()?.let { token ->
                            header("Authorization", "Bearer $token")
                        }
                    }

                    endpoint.body?.let {
                        contentType(ContentType.Application.Json)
                        setBody(json.encodeToString(it))
                    }

                    // Log request
                    logger.logRequest(
                        this,
                        endpoint.body?.let { json.encodeToString(it) }
                    )
                }
            }

            val bytes = response.body<ByteArray>()

            logger.logResponse(
                response = response,
                bodyText = "[Binary data: ${bytes.size} bytes]",
                durationMs = durationMs
            )

            return bytes

        } catch (e: Exception) {
            logger.logError(e, url)
            throw ApiException.NetworkException(e)
        }
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
                    method = endpoint.method

                    endpoint.headers?.forEach { (key, value) ->
                        header(key, value)
                    }

                    if (endpoint.requiresAuth) {
                        tokenManager.getAccessToken()?.let { token ->
                            header("Authorization", "Bearer $token")
                        }
                    }

                    endpoint.body?.let {
                        contentType(ContentType.Application.Json)
                        setBody(it)
                    }

                    // Log request
                    logger.logRequest(
                        this,
                        endpoint.body?.toString()
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
        val url = baseUrl + endpoint.path

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
                override val method = HttpMethod.Post
                override val requiresAuth = false
                override val body = mapOf("refreshToken" to refreshToken)
            }

            @Serializable
            data class RefreshTokenData(
                val accessToken: String,
                val refreshToken: String
            )

            val newTokens: RefreshTokenData = request(refreshEndpoint)

            tokenManager.saveAccessToken(newTokens.accessToken)
            tokenManager.saveRefreshToken(newTokens.refreshToken)

        } catch (e: Exception) {
            tokenManager.clearTokens()
            throw ApiException.Unauthorized
        }
    }
}