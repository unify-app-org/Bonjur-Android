package com.bonjur.network.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Error popups must never show the server's `message` — it is a raw exception
 * string. They show `errors`, comma separated.
 */
@RunWith(AndroidJUnit4::class)
class ApiErrorMessageTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun parse(body: String) = ApiException.ServerError(json.decodeFromString<NetworkError>(body))

    @Test
    fun errorsAreJoinedWithCommas() {
        val e = parse(
            """
            {"status":400,"message":"No enum constant az.unify.app...EventUserRole.REQUESTED",
             "errors":{"name":["Name is required"],"capacity":["Capacity must be positive"]}}
            """.trimIndent()
        )
        assertEquals("Capacity must be positive, Name is required", e.userMessage())
    }

    /** The reported payload: `errors` is null and `message` is unreadable. */
    @Test
    fun nullErrorsYieldNothingRatherThanTheRawMessage() {
        val e = parse(
            """
            {"status":400,"errors":null,
             "message":"No enum constant az.unify.app.discover.model.dto.enums.EventUserRole.REQUESTED",
             "path":"/ds/v1/events","error":"BAD_REQUEST"}
            """.trimIndent()
        )
        assertNull(e.userMessage())
    }

    @Test
    fun blankEntriesAreDropped() {
        val e = parse("""{"status":400,"message":"x","errors":{"a":["  ",""],"b":["Real problem"]}}""")
        assertEquals("Real problem", e.userMessage())
    }

    @Test
    fun nonServerFailuresCarryNoDetail() {
        assertNull(ApiException.Unauthorized.userMessage())
        assertNull(ApiException.NoData.userMessage())
    }
}
