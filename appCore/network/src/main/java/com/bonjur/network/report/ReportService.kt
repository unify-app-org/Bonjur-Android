package com.bonjur.network.report

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod
import com.bonjur.network.APIClient.NetworkService
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * One endpoint reports every kind of target (user / event / club / hangout):
 * POST api/us/v1/users/reports. Lives in core because member reports are raised
 * from every activity's member list, not from one feature. Mirrors iOS
 * `ReportService`.
 */

/**
 * What is being reported. Each case carries the id in the type the backend
 * expects for it — clubs are numeric, everything else is a UUID string.
 */
sealed class ReportTarget {
    data class User(val id: String) : ReportTarget()
    data class Event(val id: String) : ReportTarget()
    data class Club(val id: Int) : ReportTarget()
    data class Hangout(val id: String) : ReportTarget()
}

@Singleton
class ReportService @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient) {

    /**
     * @param reason the reason code (e.g. `SPAM`, `FAKE_PROFILE`).
     * @param details free text for moderators.
     */
    suspend fun report(target: ReportTarget, reason: String, details: String?) {
        // Raw bytes: the response body isn't needed, and decoding one could
        // turn a successful report into a failure.
        fetchRawData(ReportEndpoint(ReportRequest.from(target, reason, details)))
    }
}

private class ReportEndpoint(request: ReportRequest) : AppEndpoint {
    override val path = "api/us/v1/users/reports"
    override val method = NetworkMethod.POST
    override val body: Any = request
}

/**
 * Only the id matching [reportType] is set; the other three go out as `null`
 * (the shared Json encodes defaults).
 */
@Serializable
data class ReportRequest(
    val request: Reason,
    val reportType: String,
    val reportedUserId: String? = null,
    val reportedEventId: String? = null,
    val reportedClubId: Int? = null,
    val reportedHangoutId: String? = null
) {
    @Serializable
    data class Reason(
        val reason: String,
        val details: String? = null
    )

    companion object {
        fun from(target: ReportTarget, reason: String, details: String?): ReportRequest {
            val body = Reason(reason = reason, details = details)
            return when (target) {
                is ReportTarget.User -> ReportRequest(body, "USER", reportedUserId = target.id)
                is ReportTarget.Event -> ReportRequest(body, "EVENT", reportedEventId = target.id)
                is ReportTarget.Club -> ReportRequest(body, "CLUB", reportedClubId = target.id)
                is ReportTarget.Hangout -> ReportRequest(body, "HANGOUT", reportedHangoutId = target.id)
            }
        }
    }
}
