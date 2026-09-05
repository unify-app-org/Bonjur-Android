package com.bonjur.member.model

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import com.bonjur.designSystem.commonModel.AppUIEntities

/**
 * Canonical member-list models, shared across every activity module
 * (clubs / events / hangouts / communities). Mirrors iOS `CommunitiesMemberModuleModel`.
 * Single source of truth — do not duplicate these per feature.
 */

data class MemberCellModel(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val subtitle: String = "",
    // Member's role in the activity. Drives role-grouped sections. Mirrors iOS MemberCellModel.role.
    val role: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.MEMBER
)

data class MemberListSectionModel(
    val title: String,
    val memberCount: Int,
    val members: List<MemberCellModel>
)

/** One page of members plus whether more pages remain. Mirrors iOS `MembersPage`. */
data class MembersPage(
    val members: List<MemberCellModel>,
    val hasMore: Boolean,
    /**
     * Total rows the query matches (`totalElements`). Comes from the same response
     * as the page, so it also reflects an active keyword filter — unlike a total
     * handed in by the caller from a detail payload.
     */
    val totalCount: Int? = null
)

/**
 * Members grouped into role sections (President → Vise president → Event creators → Members).
 * Mirrors iOS `CommunitiesMemberModuleModel.GroupedMembersData`.
 */
data class GroupedMembersData(
    val sections: List<MemberListSectionModel>
) {
    companion object {
        /**
         * Section headings that differ by activity. A hangout's or an event's PRESIDENT
         * is shown as **Owner**, not "President" — the role is the same on the wire,
         * only the word changes. Clubs and communities keep "President". Mirrors iOS,
         * which passes `localizedTitles(overriding: [.president: ..._owner_role])` from
         * the hangout and event repos, and again when either opens its members list.
         *
         * Lives here so the detail tab and the "see all members" screen (which only
         * knows its [AppUIEntities.ActivityType]) cannot drift apart.
         */
        fun titleOverrides(
            activityType: AppUIEntities.ActivityType
        ): Map<AppUIEntities.UserActivityRole, String> = when (activityType) {
            AppUIEntities.ActivityType.HANG_OUTS,
            AppUIEntities.ActivityType.EVENTS -> mapOf(
                AppUIEntities.UserActivityRole.PRESIDENT to LanguageManager.string(R.string.role_owner)
            )
            AppUIEntities.ActivityType.CLUBS,
            AppUIEntities.ActivityType.COMMUNITY -> emptyMap()
        }

        fun from(
            users: List<MemberCellModel>,
            titleOverrides: Map<AppUIEntities.UserActivityRole, String> = emptyMap()
        ): GroupedMembersData {
            val sections = users
                // Every row here is an accepted member, so a role the client doesn't
                // recognise means "rank unknown", not "not joined" — and NOT_JOINED's
                // section title is the literal "-", which is what reached the screen on
                // an event whose second member carried an unmapped role.
                .groupBy { it.role.orMember() }
                .toList()
                .sortedBy { (role, _) -> role.sortPriority() }
                .map { (role, members) ->
                    MemberListSectionModel(
                        title = titleOverrides[role] ?: role.sectionTitle(),
                        memberCount = members.size,
                        members = members.sortedBy { it.name.lowercase() }
                    )
                }
            return GroupedMembersData(sections)
        }

        /** NOT_JOINED cannot describe someone already in a members list. */
        private fun AppUIEntities.UserActivityRole.orMember(): AppUIEntities.UserActivityRole =
            if (this == AppUIEntities.UserActivityRole.NOT_JOINED) {
                AppUIEntities.UserActivityRole.MEMBER
            } else {
                this
            }

        private fun AppUIEntities.UserActivityRole.sortPriority(): Int = when (this) {
            AppUIEntities.UserActivityRole.PRESIDENT -> 0
            AppUIEntities.UserActivityRole.VISE_PRESIDENT -> 1
            AppUIEntities.UserActivityRole.EVENT_CREATOR -> 2
            AppUIEntities.UserActivityRole.MEMBER -> 3
            AppUIEntities.UserActivityRole.NOT_JOINED -> 4
        }

        private fun AppUIEntities.UserActivityRole.sectionTitle(): String = when (this) {
            AppUIEntities.UserActivityRole.MEMBER -> LanguageManager.string(R.string.common_members)
            AppUIEntities.UserActivityRole.PRESIDENT -> LanguageManager.string(R.string.role_president)
            AppUIEntities.UserActivityRole.VISE_PRESIDENT ->
                LanguageManager.string(R.string.role_vice_president)
            AppUIEntities.UserActivityRole.EVENT_CREATOR -> LanguageManager.string(R.string.role_event_creators)
            // Unreachable via `from` (see `orMember`); kept so the `when` stays exhaustive.
            AppUIEntities.UserActivityRole.NOT_JOINED -> LanguageManager.string(R.string.common_members)
        }
    }
}
