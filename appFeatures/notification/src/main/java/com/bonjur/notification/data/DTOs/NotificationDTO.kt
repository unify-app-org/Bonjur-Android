package com.bonjur.notification.data.DTOs

import kotlinx.serialization.Serializable

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

/** Body for `/clubs/status` (verification). status ∈ {ACCEPT, REJECT}. */
@Serializable
data class ClubVerificationRequest(
    val clubId: Int,
    val status: String
)
