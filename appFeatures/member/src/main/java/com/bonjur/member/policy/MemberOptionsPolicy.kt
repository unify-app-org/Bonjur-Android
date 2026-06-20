package com.bonjur.member.policy

import com.bonjur.designSystem.commonModel.AppUIEntities

/**
 * Member 3-dot options menu: report reasons + role-assignment policy.
 * Pure domain rules (no Compose) so they can be unit-tested and reused by both
 * the shared options sheet and each activity's detail view models.
 * Compose port of iOS `AppPresentationModel.MemberOptionsPolicy`.
 */

/** Hardcoded report reasons backing the "Report user" sheet. */
enum class ReportReason(val code: String, val displayTitle: String) {
    FAKE_PROFILE("FAKE_PROFILE", "Fake profile"),
    INAPPROPRIATE_PROFILE_PICTURE("INAPPROPRIATE_PROFILE_PICTURE", "Inappropriate profile picture"),
    INAPPROPRIATE_PROFILE_TEXT("INAPPROPRIATE_PROFILE_TEXT", "Inappropriate profile text"),
    INAPPROPRIATE_OFFERS("INAPPROPRIATE_OFFERS", "Inappropriate offers"),
    OFFENSIVE("OFFENSIVE", "Ofensive word, threats, insults"),
    UNDERAGE("UNDERAGE", "Underage"),
    SCAM_AND_COMMERCIAL("SCAM_AND_COMMERCIAL", "Scam and commercial"),
    OTHER("OTHER", "Other")
}

/** Report reasons for an activity itself (club / event / hangout). */
enum class ActivityReportReason(val code: String, val displayTitle: String) {
    INAPPROPRIATE_CONTENT("INAPPROPRIATE_CONTENT", "Inappropriate content"),
    SPAM("SPAM", "Spam"),
    SCAM_AND_COMMERCIAL("SCAM_AND_COMMERCIAL", "Scam and commercial"),
    HARASSMENT("HARASSMENT", "Harassment or hate speech"),
    MISLEADING_INFO("MISLEADING_INFO", "Misleading information"),
    OTHER("OTHER", "Other")
}

/**
 * Single source of truth for who may change roles and which roles they may grant.
 * Both the shared options sheet and the detail view models call into this so the
 * rules can never diverge across activities.
 */
object MemberOptionsPolicy {

    /**
     * Roles a viewer may grant to another member.
     * - President: all assignable roles.
     * - Vice president: only Member and Event creator.
     * - Anyone else: none.
     */
    fun assignableRoles(
        viewer: AppUIEntities.UserActivityRole
    ): List<AppUIEntities.UserActivityRole> = when (viewer) {
        AppUIEntities.UserActivityRole.PRESIDENT -> listOf(
            AppUIEntities.UserActivityRole.MEMBER,
            AppUIEntities.UserActivityRole.PRESIDENT,
            AppUIEntities.UserActivityRole.VISE_PRESIDENT,
            AppUIEntities.UserActivityRole.EVENT_CREATOR
        )
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> listOf(
            AppUIEntities.UserActivityRole.MEMBER,
            AppUIEntities.UserActivityRole.EVENT_CREATOR
        )
        AppUIEntities.UserActivityRole.MEMBER,
        AppUIEntities.UserActivityRole.EVENT_CREATOR,
        AppUIEntities.UserActivityRole.NOT_JOINED -> emptyList()
    }

    /**
     * Whether the "Change role" row should be shown.
     * Only clubs and communities have roles; never on your own row.
     */
    fun canChangeRole(
        viewer: AppUIEntities.UserActivityRole,
        activity: AppUIEntities.ActivityType,
        isSelf: Boolean
    ): Boolean {
        if (isSelf) return false
        if (activity != AppUIEntities.ActivityType.CLUBS &&
            activity != AppUIEntities.ActivityType.COMMUNITY
        ) return false
        return assignableRoles(viewer).isNotEmpty()
    }

    /** Whether the "Report user" row should be shown. Everyone but yourself. */
    fun canReport(isSelf: Boolean): Boolean = !isSelf

    /**
     * Whether the "Report <activity>" row should be shown in the activity options
     * sheet. Everyone may report the activity except its creator/owner.
     */
    fun canReportActivity(
        viewer: AppUIEntities.UserActivityRole
    ): Boolean = when (viewer) {
        AppUIEntities.UserActivityRole.PRESIDENT,
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> false
        AppUIEntities.UserActivityRole.MEMBER,
        AppUIEntities.UserActivityRole.VISE_PRESIDENT,
        AppUIEntities.UserActivityRole.NOT_JOINED -> true
    }
}

/**
 * Title shown in the assign-role picker. Differs from the badge title — e.g.
 * event creator reads "Event organizer" here. Mirrors iOS `assignTitle`.
 */
val AppUIEntities.UserActivityRole.assignTitle: String
    get() = when (this) {
        AppUIEntities.UserActivityRole.MEMBER -> "Member"
        AppUIEntities.UserActivityRole.PRESIDENT -> "President"
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> "Vice president"
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> "Event organizer"
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
    }

/** Subtitle under each picker row describing the role's powers. Mirrors iOS `assignSubtitle`. */
val AppUIEntities.UserActivityRole.assignSubtitle: String
    get() = when (this) {
        AppUIEntities.UserActivityRole.MEMBER -> "Just can view content"
        AppUIEntities.UserActivityRole.PRESIDENT -> "Can manage club"
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> "Can manage club, can't assign roles"
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> "Just can create event in club"
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
    }
