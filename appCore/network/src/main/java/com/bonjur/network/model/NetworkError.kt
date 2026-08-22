package com.bonjur.network.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/**
 * `status` comes back as a **number** (`"status": 400`) from most services but as
 * a string from some. Typing it as `String` made the whole error body fail to
 * decode, and the decode failure is swallowed in `APIClient` — so `errors` never
 * reached the UI at all. iOS has always tried Int then String; this matches it.
 */
object LenientStringSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LenientString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String? {
        val input = decoder as? JsonDecoder ?: return decoder.decodeString()
        val element = input.decodeJsonElement()
        return (element as? JsonPrimitive)?.contentOrNull
    }

    override fun serialize(encoder: Encoder, value: String?) {
        encoder.encodeString(value.orEmpty())
    }
}

@Serializable
data class NetworkError(
    @Serializable(with = LenientStringSerializer::class)
    val status: String? = null,
    /**
     * Raw server/exception text ("No enum constant …EventUserRole.REQUESTED").
     * Useful in logs, never shown to a user — see [userMessage].
     */
    val message: String? = null,
    val detail: String? = null,
    val errors: Map<String, List<String>>? = null
)

/**
 * The server's field validation messages, flattened. `errors` is keyed by field,
 * so the keys are sorted to keep the order stable between calls.
 */
fun NetworkError.userMessages(): List<String> =
    errors.orEmpty()
        .toSortedMap()
        .values
        .flatten()
        .map { it.trim() }
        .filter { it.isNotEmpty() }

/**
 * The server's `errors` entries, comma separated, or null when there are none.
 *
 * `message` is a raw server/exception string ("No enum constant
 * …EventUserRole.REQUESTED") and must never reach a user, so it is deliberately
 * not used as a fallback here — the UI supplies a generic line instead.
 */
fun Throwable.userMessage(): String? =
    (this as? ApiException.ServerError)
        ?.error
        ?.userMessages()
        ?.takeIf { it.isNotEmpty() }
        ?.joinToString(", ")

sealed class ApiException(message: String) : Exception(message) {
    data class ServerError(val error: NetworkError) : ApiException(error.message.orEmpty())
    object Unauthorized: ApiException("Unauthorized. Please login again.")
    data class NetworkException(val throwable: Throwable) : ApiException(throwable.message ?: "Network error")
    data class DecodingError(val throwable: Throwable) : ApiException("Failed to decode response")
    object InvalidURL : ApiException("Invalid URL")
    object NoData : ApiException("No data received")
    object Unknown : ApiException("Unknown error occurred")
}