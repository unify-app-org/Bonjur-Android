package com.bonjur.member.list

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.member.model.MembersPage
import kotlinx.serialization.Serializable

/**
 * Shared "see all members" destination, reused by every activity (clubs / events /
 * hangouts / communities) — mirrors iOS `communitiesModule.makeMembersListView`.
 * `data object` so the string Navigator can reach it; payload (incl. the per-activity
 * loadPage/assignRole closures) rides in-memory via NavArgs. See [[nav-parametrized-route-crash]].
 */
@Serializable
data object MemberListScreens {
    @Serializable
    data object MembersList
}

/**
 * Cross-module input for the shared members list. The closures hit each activity's own
 * endpoint/useCase, so the screen itself stays activity-agnostic (iOS injects the same way).
 */
data class MemberListInputData(
    val title: String = LanguageManager.string(R.string.common_members),
    val viewerRole: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.NOT_JOINED,
    val currentUserId: String? = null,
    val activityType: AppUIEntities.ActivityType = AppUIEntities.ActivityType.CLUBS,
    /**
     * Total member count from the detail payload, shown until the first page
     * reports its own `totalElements` (which is per-query and wins). Mirrors iOS
     * `MembersListInput.totalCount`.
     */
    val totalCount: Int? = null,
    val loadPage: suspend (page: Int, size: Int, keyword: String?) -> MembersPage,
    val assignRole: (suspend (userId: String, role: AppUIEntities.UserActivityRole) -> Unit)? = null,
    val onMemberTapped: (memberId: String) -> Unit = {}
)
