package com.bonjur.member.list

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
    val title: String = "Members",
    val sections: List<MemberListSectionModel> = emptyList(),
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val viewerRole: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.NOT_JOINED,
    val currentUserId: String? = null,
    val activityType: AppUIEntities.ActivityType = AppUIEntities.ActivityType.CLUBS
) : FeatureState

sealed class MemberListAction : FeatureAction {
    object OnAppear : MemberListAction()
    object LoadMore : MemberListAction()
    object BackTapped : MemberListAction()
    data class MemberTapped(val member: MemberCellModel) : MemberListAction()
    data class AssignRole(
        val userId: String,
        val role: AppUIEntities.UserActivityRole
    ) : MemberListAction()
}
