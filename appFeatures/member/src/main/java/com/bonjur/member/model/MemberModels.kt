package com.bonjur.member.model

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
    val hasMore: Boolean
)

/**
 * Members grouped into role sections (President → Vise president → Event creators → Members).
 * Mirrors iOS `CommunitiesMemberModuleModel.GroupedMembersData`.
 */
data class GroupedMembersData(
    val sections: List<MemberListSectionModel>
) {
    companion object {
        fun from(
            users: List<MemberCellModel>,
            titleOverrides: Map<AppUIEntities.UserActivityRole, String> = emptyMap()
        ): GroupedMembersData {
            val sections = users
                .groupBy { it.role }
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

        private fun AppUIEntities.UserActivityRole.sortPriority(): Int = when (this) {
            AppUIEntities.UserActivityRole.PRESIDENT -> 0
            AppUIEntities.UserActivityRole.VISE_PRESIDENT -> 1
            AppUIEntities.UserActivityRole.EVENT_CREATOR -> 2
            AppUIEntities.UserActivityRole.MEMBER -> 3
            AppUIEntities.UserActivityRole.NOT_JOINED -> 4
        }

        private fun AppUIEntities.UserActivityRole.sectionTitle(): String = when (this) {
            AppUIEntities.UserActivityRole.MEMBER -> "Members"
            AppUIEntities.UserActivityRole.PRESIDENT -> "President"
            AppUIEntities.UserActivityRole.VISE_PRESIDENT -> "Vise president"
            AppUIEntities.UserActivityRole.EVENT_CREATOR -> "Event creators"
            AppUIEntities.UserActivityRole.NOT_JOINED -> "-"
        }
    }
}
