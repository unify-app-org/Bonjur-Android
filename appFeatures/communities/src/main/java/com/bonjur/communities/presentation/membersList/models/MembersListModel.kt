package com.bonjur.communities.presentation.membersList.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MemberListSectionModel

data class MembersListInputData(
    val communityId: Int,
    val title: String = "Members"
)

sealed class MembersListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : MembersListSideEffect()
}

data class MembersListViewState(
    val title: String = "Members",
    val sections: List<MemberListSectionModel> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true
) : FeatureState

sealed class MembersListAction : FeatureAction {
    object OnAppear : MembersListAction()
    object LoadMore : MembersListAction()
    object BackTapped : MembersListAction()
    data class MemberTapped(val member: MemberCellModel) : MembersListAction()
}
