package com.bonjur.notification.domain.models

// MARK: - Action requests (Needs your action)

/** Which entity a pending request targets, carrying the id to deep-link into. */
sealed class ActionRequestKind {
    data class Club(val id: Int) : ActionRequestKind()
    data class Hangout(val id: String) : ActionRequestKind()
}

/** A single pending join request, normalized across club + hangout sources. */
data class ActionRequestItem(
    val id: String,
    val kind: ActionRequestKind,
    val requesterId: String?,
    val requesterName: String,
    val targetName: String,
    val avatarUrl: String?,
    /** Epoch millis of createdAt, or null. Used for sort + relative stamp. */
    val createdAtMillis: Long?
)

/** One source's page result, ready for the view model to accumulate. */
data class RequestPageResult(
    val items: List<ActionRequestItem>,
    val hasMore: Boolean
)

// MARK: - Verification

/** A club awaiting admin verification. */
data class VerificationItem(
    val id: String,
    val clubId: Int,
    val clubName: String,
    val submitterName: String,
    val logoUrl: String?
)

data class VerificationPageResult(
    val items: List<VerificationItem>,
    val hasMore: Boolean
)

// MARK: - Notification feed (inbox)

enum class NotificationType {
    BIRTHDAY, HOLIDAY, EVENT_REMINDER, REQUEST_OUTCOME, VERIFICATION_OUTCOME, GENERAL;

    companion object {
        fun from(api: String): NotificationType = when (api.uppercase()) {
            "BIRTHDAY" -> BIRTHDAY
            "HOLIDAY" -> HOLIDAY
            "EVENT_REMINDER" -> EVENT_REMINDER
            "REQUEST_OUTCOME" -> REQUEST_OUTCOME
            "VERIFICATION_OUTCOME" -> VERIFICATION_OUTCOME
            else -> GENERAL
        }
    }
}

/** Mirrors iOS `NotificationFeedItem`. */
data class NotificationFeedItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val subtitle: String,
    val note: String?,
    val imageUrl: String?,
    val timeAgo: String,
    val isRead: Boolean
)

data class NotificationSection(
    val title: String,
    val items: List<NotificationFeedItem>
)

/** Client-composed banner counts. */
data class NeedsActionSummary(
    val requests: Int,
    val verifications: Int
) {
    val total: Int get() = requests + verifications
    val hasActions: Boolean get() = total > 0
}

/** Full payload backing the notification inbox screen. */
data class NotificationInbox(
    val action: NeedsActionSummary,
    val sections: List<NotificationSection>
) {
    companion object {
        val empty = NotificationInbox(NeedsActionSummary(0, 0), emptyList())
    }
}
