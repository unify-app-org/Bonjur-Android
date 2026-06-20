package com.bonjur.communities.presentation.detail.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption

data class CommunityDetailInputData(
    val communityId: Int
)

sealed class CommunityDetailSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : CommunityDetailSideEffect()
}

data class CommunityDetailViewState(
    val uiModel: CommunityDetails.UIModel? = null,
    val clubsData: List<ClubCardModel> = emptyList(),
    val membersData: GroupedMembersData? = null,
    val currentUserId: String? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT
) : FeatureState {

    val isEditable: Boolean
        get() = uiModel?.userActivity == AppUIEntities.UserActivityRole.VISE_PRESIDENT ||
            uiModel?.userActivity == AppUIEntities.UserActivityRole.PRESIDENT
    val canCreateEvent: Boolean
        get() = uiModel != null &&
            uiModel.userActivity != AppUIEntities.UserActivityRole.MEMBER &&
            uiModel.userActivity != AppUIEntities.UserActivityRole.NOT_JOINED
    val hasJoined: Boolean
        get() = uiModel != null &&
            uiModel.userActivity != AppUIEntities.UserActivityRole.NOT_JOINED

    enum class SegmentTypes(
        override val title: String
    ) : SegmentedPickerOption {

        ABOUT("About"),
        CLUBS("Clubs"),
        MEMBERS("Members");

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes {
                return when (index) {
                    0 -> ABOUT
                    1 -> CLUBS
                    2 -> MEMBERS
                    else -> ABOUT
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                ABOUT -> 0
                CLUBS -> 1
                MEMBERS -> 2
            }
        }
    }
}

sealed class CommunityDetailAction : FeatureAction {
    object FetchData : CommunityDetailAction()
    object BackTapped : CommunityDetailAction()
    object EditTapped : CommunityDetailAction()
    object SeeAllMembersTapped : CommunityDetailAction()
    data class AssignRole(
        val userId: String,
        val role: AppUIEntities.UserActivityRole
    ) : CommunityDetailAction()
    data class UserTapped(val userId: String) : CommunityDetailAction()
    data class ClubItemTapped(val id: Int) : CommunityDetailAction()
    data class SegmentChanged(val segment: CommunityDetailViewState.SegmentTypes) : CommunityDetailAction()
}
