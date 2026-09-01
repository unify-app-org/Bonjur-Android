package com.bonjur.notification.domain.models

// MARK: - Action requests (Needs your action)

/** Which entity a pending request targets, carrying the id to deep-link into. */
sealed class ActionRequestKind {
    data class Club(val id: Int) : ActionRequestKind()
    data class Hangout(val id: String) : ActionRequestKind()
    data class Event(val id: String) : ActionRequestKind()
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

/**
 * Feed row type (API `type`). Full mirror of iOS `NotificationType` — the wire
 * value round-trips into the deep-link action, so the catalog has to match.
 */
enum class NotificationType {
    EVENT_REMINDER,
    BIRTHDAY,
    HOLIDAY,
    REQUEST_OUTCOME,
    VERIFICATION_OUTCOME,

    REQUEST_CLUB,
    // The backend's name for a join request on a PRIVATE activity. Missing from the
    // catalog, so these rows fell through to GENERAL and their "Continue" went
    // nowhere — the reported "tapping the notification does not navigate".
    USER_REQUESTED_PRIVATE_CLUB,
    REJECTED_USER_FROM_CLUB,
    ACCEPTED_USER_FROM_CLUB,
    USER_JOINED_PUBLIC_CLUB,
    REQUEST_CLUB_VERIFICATION,
    VERIFIED_CLUB,
    REJECTED_CLUB_VERIFICATION,

    REQUEST_HANGOUT,
    USER_REQUESTED_PRIVATE_HANGOUT,
    REJECTED_USER_FROM_HANGOUT,
    ACCEPTED_USER_FROM_HANGOUT,
    USER_JOINED_PUBLIC_HANGOUT,

    REQUEST_EVENT,
    USER_REQUESTED_PRIVATE_EVENT,
    REJECTED_USER_FROM_EVENT,
    ACCEPTED_USER_FROM_EVENT,

    GENERAL;

    /** Wire value, round-tripped into the deep-link action; null for GENERAL. */
    val apiValue: String?
        get() = if (this == GENERAL) null else name

    /** Types whose row/hero shows a remote `imageUrl`, falling back to the local
     * type icon. The rest (celebratory + generic) always render the local icon. */
    val prefersRemoteImage: Boolean
        get() = when (this) {
            BIRTHDAY, HOLIDAY, GENERAL -> false
            else -> true
        }

    /** Where tapping a row of this type should go (mirrors iOS `tapDestination`). */
    val tapDestination: TapDestination
        get() = when (this) {
            // Incoming requests the user must act on.
            REQUEST_CLUB, REQUEST_HANGOUT, REQUEST_EVENT,
            USER_REQUESTED_PRIVATE_CLUB, USER_REQUESTED_PRIVATE_HANGOUT,
            USER_REQUESTED_PRIVATE_EVENT,
            REQUEST_CLUB_VERIFICATION -> TapDestination.NEEDS_ACTION
            // Purely informational — nowhere meaningful to go.
            BIRTHDAY, HOLIDAY, GENERAL -> TapDestination.NONE
            // Outcomes & reminders open the related detail.
            else -> TapDestination.TARGET
        }

    enum class TapDestination {
        /** The join-request / verification accept-reject screen. */
        NEEDS_ACTION,
        /** The related club/hangout/event/user detail (via target). */
        TARGET,
        /** Informational — no navigation, only mark as read. */
        NONE
    }

    companion object {
        fun from(api: String): NotificationType =
            entries.firstOrNull { it != GENERAL && it.name == api.uppercase() } ?: GENERAL
    }
}

/** Which entity a feed row deep-links into (API `targetType`). */
enum class NotificationTargetType {
    EVENT, CLUB, HANGOUT, COMMUNITY, USER, NONE;

    companion object {
        fun from(api: String?): NotificationTargetType = when (api?.uppercase()) {
            "EVENT" -> EVENT
            "CLUB" -> CLUB
            "HANGOUT" -> HANGOUT
            "COMMUNITY" -> COMMUNITY
            "USER" -> USER
            else -> NONE
        }
    }
}

/** Mirrors iOS `NotificationFeedItem`. */
data class NotificationFeedItem(
    val id: String,
    /**
     * Server UUID used by the single-row read call. Kept separate from [id],
     * which stays the numeric row id the list uses as its key. Null when the
     * backend omits it — then the row is marked read locally only.
     */
    val notificationId: String? = null,
    val type: NotificationType,
    val title: String,
    val subtitle: String,
    val note: String?,
    val imageUrl: String?,
    val timeAgo: String,
    val isRead: Boolean,
    val targetType: NotificationTargetType = NotificationTargetType.NONE,
    val targetId: String? = null,
    /** Parsed server createdAt epoch millis; drives date-bucket grouping. */
    val createdAtMillis: Long? = null
)

data class NotificationSection(
    val title: String,
    val items: List<NotificationFeedItem>
)

/** One page of the notification feed, already mapped to feed items. */
data class NotificationFeedPage(
    val items: List<NotificationFeedItem>,
    val hasMore: Boolean
)

/** Full payload backing the notification inbox screen. */
data class NotificationInbox(
    val sections: List<NotificationSection>
) {
    companion object {
        val empty = NotificationInbox(emptyList())
    }
}
