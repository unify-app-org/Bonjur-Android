package com.bonjur.notification.data.DTOs

import kotlinx.serialization.Serializable

// MARK: - Feed rows

/**
 * `GET api/ns/v1/notifications` content row. `note` isn't sent by the backend —
 * the live payload carries that remark inside `metadata` instead, so the mapper
 * falls back to `metadata.rejectionReason`.
 */
@Serializable
data class NotificationDTO(
    val id: Int? = null,
    /**
     * Server-side UUID. This — not the numeric [id] — is what
     * `POST api/ns/v1/notifications/read/{notificationId}` expects.
     */
    val notificationId: String? = null,
    val type: String? = null,
    val title: String? = null,
    val body: String? = null,
    val note: String? = null,
    val imageUrl: String? = null,
    val isRead: Boolean? = null,
    /** House audit stamp, `dd-MM-yyyy HH:mm:ss` (see `RelativeTime.parse`). */
    val createdAt: String? = null,
    val targetType: String? = null,
    val targetId: String? = null,
    val metadata: NotificationMetadataDTO? = null
)

/**
 * Per-type extras. Arrives as `null`, `{}` or a populated object; every field is
 * optional because which keys ride along depends on the notification type.
 */
@Serializable
data class NotificationMetadataDTO(
    /** Verification/join rejection remark — rendered as the row's note. */
    val rejectionReason: String? = null,
    /** Reminder lead time, e.g. `FIFTEEN_MINUTES_BEFORE` (unused by the UI). */
    val reminderTime: String? = null
)

/** `GET api/ns/v1/notifications/unread-count` response. */
@Serializable
data class UnreadCountDTO(
    val count: Int? = null
)

// MARK: - Join request rows

/** `GET /clubs/join-requests` content row. `fileUrl` = requester photo. */
@Serializable
data class ClubJoinRequestDTO(
    val userId: String? = null,
    val fullName: String? = null,
    val clubId: Int? = null,
    val clubName: String? = null,
    val fileUrl: String? = null,
    val createdAt: String? = null
)

/** `GET /hangouts/join-requests` content row. `userProfileUrl` = requester photo. */
@Serializable
data class HangoutJoinRequestDTO(
    val userId: String? = null,
    val userProfileUrl: String? = null,
    val fullName: String? = null,
    val fileUrl: String? = null,
    val hangoutId: String? = null,
    val hangoutName: String? = null,
    val createdAt: String? = null
)

/**
 * `GET /events/requests` content row. The live API sends neither `fileUrl` nor
 * `createdAt` yet — both optional for when they ship (rows sort last meanwhile).
 */
@Serializable
data class EventJoinRequestDTO(
    val userId: String? = null,
    val userProfileUrl: String? = null,
    val fullName: String? = null,
    val fileUrl: String? = null,
    val eventId: String? = null,
    val eventName: String? = null,
    val createdAt: String? = null
)

// MARK: - Status request bodies

/** Body for `/clubs/join-requests/status`. status ∈ {ACCEPT, REJECT}. */
@Serializable
data class ClubStatusRequest(
    val clubId: Int,
    val userId: String,
    val status: String
)

/** Body for `/hangouts/requests/{hangoutId}`. status ∈ {ACCEPTED, REJECTED}. */
@Serializable
data class HangoutStatusRequest(
    val userId: String,
    val status: String
)

/** Body for `/events/{eventId}`. status ∈ {ACCEPTED, PENDING, REJECTED, LEFT}. */
@Serializable
data class EventStatusRequest(
    val userId: String,
    val status: String
)

/**
 * Body for `/clubs/status` (verification). status ∈ {ACCEPT, REJECT}.
 * [rejectionReason] is the optional admin note shown to the club; it stays null
 * on accept and is dropped from the JSON when null.
 */
@Serializable
data class ClubVerificationRequest(
    val clubId: Int,
    val status: String,
    val rejectionReason: String? = null
)
