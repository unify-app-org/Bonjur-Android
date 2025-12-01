package com.bonjur.network.logger

import android.util.Log
import com.bonjur.network.AppConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkLogger @Inject constructor(
    private val configs: AppConfig
) {

    companion object {
        private const val TAG = "NetworkLogger"
    }

    fun logRequest(request: HttpRequestBuilder, body: String?) {
        if (configs.enableLogging) {
            Log.d(TAG, "\n🚀 ============ REQUEST START ============")
            Log.d(TAG, "📍 ${request.method.value} ${request.url}")

            if (request.headers.entries().isNotEmpty()) {
                Log.d(TAG, "\n📋 Headers:")
                request.headers.entries().forEach { (key, values) ->
                    val value = values.firstOrNull() ?: ""
                    if (key.lowercase() == "authorization") {
                        Log.d(TAG, "  $key: Bearer ***")
                    } else {
                        Log.d(TAG, "  $key: $value")
                    }
                }
            }

            body?.let {
                Log.d(TAG, "\n📦 Body:")
                Log.d(TAG, prettyPrintJson(it))
            }

            Log.d(TAG, "============ REQUEST END ============\n")
        }
    }

    fun logResponse(
        response: HttpResponse,
        bodyText: String,
        durationMs: Long
    ) {
        if (configs.enableLogging) {
            val emoji = statusEmoji(response.status.value)

            Log.d(TAG, "\n$emoji ============ RESPONSE START ============")
            Log.d(TAG, "📍 ${response.status.value} ${response.request.url}")

            val durationSeconds = durationMs / 1000.0
            Log.d(TAG, "⏱️  Duration: %.3fs".format(durationSeconds))

            Log.d(TAG, "\n📋 Headers:")
            response.headers.entries().forEach { (key, values) ->
                Log.d(TAG, "  $key: ${values.firstOrNull()}")
            }

            Log.d(TAG, "\n📦 Response Body (${bodyText.length} bytes):")
            Log.d(TAG, prettyPrintJson(bodyText))

            Log.d(TAG, "============ RESPONSE END ============\n")
        }
    }

    fun logError(
        error: Throwable,
        url: String?,
        statusCode: Int? = null,
        errorBody: String? = null
    ) {
        if (configs.enableLogging) {
            Log.e(TAG, "\n❌ ============ ERROR START ============")
            url?.let { Log.e(TAG, "📍 URL: $it") }

            statusCode?.let {
                val emoji = statusEmoji(it)
                Log.e(TAG, "$emoji Status Code: $it")
            }

            Log.e(TAG, "⚠️  Error: ${error.message}")

            errorBody?.let {
                Log.e(TAG, "\n📦 Error Response:")
                Log.e(TAG, prettyPrintJson(it))
            }

            Log.e(TAG, "Stack trace:", error)
            Log.e(TAG, "============ ERROR END ============\n")
        }
    }

    private fun prettyPrintJson(json: String): String {
        return try {
            val jsonObject = JSONObject(json)
            jsonObject.toString(2)
        } catch (e: Exception) {
            json
        }
    }

    private fun statusEmoji(statusCode: Int): String {
        return when (statusCode) {
            in 200..299 -> "✅"
            in 300..399 -> "↪️"
            in 400..499 -> "⚠️"
            in 500..599 -> "❌"
            else -> "❓"
        }
    }
}