package com.bonjur.communities.presentation.detail.model

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R as DesignR
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
    /** Sub-club paging. `api/ds/v1/clubs` sends no page envelope, so "more exists"
     *  means only "the last page came back full". */
    val clubsHasMore: Boolean = false,
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
        private val titleRes: Int
    ) : SegmentedPickerOption {

        ABOUT(DesignR.string.about),
        // TODO: Clubs section temporarily hidden on community details.
        // CLUBS(DesignR.string.clubs),
        MEMBERS(DesignR.string.common_members);

    /** Resolved per read, not in the constructor: enum constants are built once at class
     *  load, so a title captured there keeps the language the app was launched in and the
     *  tabs stop following a language switch. */
        override val title: String get() = LanguageManager.string(titleRes)

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes {
                return when (index) {
                    0 -> ABOUT
                    // 1 -> CLUBS
                    1 -> MEMBERS
                    else -> ABOUT
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                ABOUT -> 0
                // CLUBS -> 1
                MEMBERS -> 1
            }
        }
    }
}

sealed class CommunityDetailAction : FeatureAction {
    object FetchData : CommunityDetailAction()
    object LoadMoreClubs : CommunityDetailAction()
    object BackTapped : CommunityDetailAction()
    object EditTapped : CommunityDetailAction()
    object SeeAllMembersTapped : CommunityDetailAction()
    object CreateClubTapped : CommunityDetailAction()
    object CreateEventTapped : CommunityDetailAction()
    data class AssignRole(
        val userId: String,
        val role: AppUIEntities.UserActivityRole
    ) : CommunityDetailAction()
    data class UserTapped(val userId: String) : CommunityDetailAction()
    data class ClubItemTapped(val id: Int) : CommunityDetailAction()
    data class SegmentChanged(val segment: CommunityDetailViewState.SegmentTypes) : CommunityDetailAction()
}
