package com.bonjur.member.policy

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import com.bonjur.designSystem.commonModel.AppUIEntities

/**
 * Member 3-dot options menu: report reasons + role-assignment policy.
 * Pure domain rules (no Compose) so they can be unit-tested and reused by both
 * the shared options sheet and each activity's detail view models.
 * Compose port of iOS `AppPresentationModel.MemberOptionsPolicy`.
 */

/**
 * Hardcoded report reasons backing the "Report user" sheet.
 * `displayTitle` resolves through [LanguageManager] on every read so the sheet
 * follows an in-app language switch.
 */
enum class ReportReason(val code: String, private val titleRes: Int) {
    FAKE_PROFILE("FAKE_PROFILE", R.string.report_reason_fake_profile),
    INAPPROPRIATE_PROFILE_PICTURE(
        "INAPPROPRIATE_PROFILE_PICTURE",
        R.string.report_reason_inappropriate_profile_picture
    ),
    INAPPROPRIATE_PROFILE_TEXT(
        "INAPPROPRIATE_PROFILE_TEXT",
        R.string.report_reason_inappropriate_profile_text
    ),
    INAPPROPRIATE_OFFERS("INAPPROPRIATE_OFFERS", R.string.report_reason_inappropriate_offers),
    OFFENSIVE("OFFENSIVE", R.string.report_reason_offensive),
    UNDERAGE("UNDERAGE", R.string.report_reason_underage),
    SCAM_AND_COMMERCIAL("SCAM_AND_COMMERCIAL", R.string.report_reason_scam_and_commercial),
    OTHER("OTHER", R.string.report_reason_other);

    val displayTitle: String
        get() = LanguageManager.string(titleRes)
}

/** Report reasons for an activity itself (club / event / hangout). */
enum class ActivityReportReason(val code: String, private val titleRes: Int) {
    INAPPROPRIATE_CONTENT(
        "INAPPROPRIATE_CONTENT",
        R.string.activity_report_reason_inappropriate_content
    ),
    SPAM("SPAM", R.string.activity_report_reason_spam),
    SCAM_AND_COMMERCIAL(
        "SCAM_AND_COMMERCIAL",
        R.string.activity_report_reason_scam_and_commercial
    ),
    HARASSMENT("HARASSMENT", R.string.activity_report_reason_harassment),
    MISLEADING_INFO("MISLEADING_INFO", R.string.activity_report_reason_misleading_info),
    OTHER("OTHER", R.string.activity_report_reason_other);

    val displayTitle: String
        get() = LanguageManager.string(titleRes)
}

/**
 * Single source of truth for who may change roles and which roles they may grant.
 * Both the shared options sheet and the detail view models call into this so the
 * rules can never diverge across activities.
 */
object MemberOptionsPolicy {

    /**
     * Roles a viewer may grant to another member.
     * - President: Member, Vice president and Event creator — a president
     *   cannot hand the presidency to someone else.
     * - Vice president: only Member and Event creator — a vice president
     *   cannot create peers (vice president) or superiors (president).
     * - Anyone else: none.
     */
    fun assignableRoles(
        viewer: AppUIEntities.UserActivityRole
    ): List<AppUIEntities.UserActivityRole> = when (viewer) {
        AppUIEntities.UserActivityRole.PRESIDENT -> listOf(
            AppUIEntities.UserActivityRole.MEMBER,
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
        AppUIEntities.UserActivityRole.MEMBER -> LanguageManager.string(R.string.role_assign_title_member)
        AppUIEntities.UserActivityRole.PRESIDENT -> LanguageManager.string(R.string.role_assign_title_president)
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> LanguageManager.string(R.string.role_assign_title_vice_president)
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> LanguageManager.string(R.string.role_assign_title_event_creator)
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
    }

/** Subtitle under each picker row describing the role's powers. Mirrors iOS `assignSubtitle`. */
val AppUIEntities.UserActivityRole.assignSubtitle: String
    get() = when (this) {
        AppUIEntities.UserActivityRole.MEMBER -> LanguageManager.string(R.string.role_assign_subtitle_member)
        AppUIEntities.UserActivityRole.PRESIDENT -> LanguageManager.string(R.string.role_assign_subtitle_president)
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> LanguageManager.string(R.string.role_assign_subtitle_vice_president)
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> LanguageManager.string(R.string.role_assign_subtitle_event_creator)
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
    }
