package com.bonjur.notification.domain.models

import com.bonjur.notification.data.DTOs.ClubJoinRequestDTO
import com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO
import java.util.UUID

/** Normalizes the join-request / pending DTOs into domain models. */
object JoinRequestMapper {

    fun item(dto: ClubJoinRequestDTO): ActionRequestItem? {
        val clubId = dto.clubId ?: return null
        return ActionRequestItem(
            id = "club-$clubId-${dto.userId ?: UUID.randomUUID()}",
            kind = ActionRequestKind.Club(clubId),
            requesterId = dto.userId,
            requesterName = dto.fullName ?: "Someone",
            targetName = dto.clubName ?: "a club",
            avatarUrl = dto.fileUrl,
            createdAtMillis = RelativeTime.parse(dto.createdAt)
        )
    }

    fun item(dto: HangoutJoinRequestDTO): ActionRequestItem? {
        val hangoutId = dto.hangoutId ?: return null
        return ActionRequestItem(
            id = "hangout-$hangoutId-${dto.userId ?: UUID.randomUUID()}",
            kind = ActionRequestKind.Hangout(hangoutId),
            requesterId = dto.userId,
            requesterName = dto.fullName ?: "Someone",
            targetName = dto.hangoutName ?: "a hangout",
            avatarUrl = dto.userProfileUrl,
            createdAtMillis = RelativeTime.parse(dto.createdAt)
        )
    }

    fun verification(dto: ClubJoinRequestDTO): VerificationItem? {
        val clubId = dto.clubId ?: return null
        return VerificationItem(
            id = "verif-$clubId",
            clubId = clubId,
            clubName = dto.clubName ?: "A club",
            submitterName = dto.fullName ?: "Someone",
            logoUrl = dto.fileUrl
        )
    }
}
