package com.bonjur.member.list

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MemberListSectionModel

sealed class MemberListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : MemberListSideEffect()
}

data class MemberListViewState(
    val title: String = LanguageManager.string(R.string.common_members),
    val searchText: String = "",
    val sections: List<MemberListSectionModel> = emptyList(),
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    /**
     * Server total for the current query (`totalElements`), shown next to the
     * section header. Reflects an active keyword, unlike a caller-supplied total.
     */
    val totalCount: Int? = null,
    /**
     * Bumped on every load that replaces the list (search, refresh). The view
     * scrolls back to the top when it changes — otherwise a result set that
     * collapses from 40 rows to 1 renders off-screen and the screen looks blank.
     */
    val listResetToken: Int = 0,
    val viewerRole: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.NOT_JOINED,
    val currentUserId: String? = null,
    val activityType: AppUIEntities.ActivityType = AppUIEntities.ActivityType.CLUBS
) : FeatureState

sealed class MemberListAction : FeatureAction {
    object OnAppear : MemberListAction()
    object LoadMore : MemberListAction()
    object BackTapped : MemberListAction()
    data class MemberTapped(val member: MemberCellModel) : MemberListAction()
    data class SearchTextChanged(val text: String) : MemberListAction()
    data class AssignRole(
        val userId: String,
        val role: AppUIEntities.UserActivityRole
    ) : MemberListAction()
}
