package com.bonjur.notification.data.DTOs

import kotlinx.serialization.Serializable

// MARK: - Feed rows

/**
 * `GET api/ns/v1/notifications` content row. `note` isn't sent by the backend
 * yet (spec has it as the admin's extra remark) — optional so the UI lights up
 * as soon as it ships.
 */
@Serializable
data class NotificationDTO(
    val id: Int? = null,
    val type: String? = null,
    val title: String? = null,
    val body: String? = null,
    val note: String? = null,
    val imageUrl: String? = null,
    val isRead: Boolean? = null,
    val createdAt: String? = null,
    val targetType: String? = null,
    val targetId: String? = null
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

/** Body for `/clubs/status` (verification). status ∈ {ACCEPT, REJECT}. */
@Serializable
data class ClubVerificationRequest(
    val clubId: Int,
    val status: String
)
