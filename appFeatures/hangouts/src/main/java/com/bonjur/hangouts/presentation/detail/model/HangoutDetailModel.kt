package com.bonjur.hangouts.presentation.detail.model

import androidx.annotation.StringRes
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.hangouts.R
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel

// MARK: - Input Data
data class HangoutDetailsInputData(
    val hangoutId: String
)

// MARK: - Side Effects
sealed class HangoutDetailsSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : HangoutDetailsSideEffect()
}

// MARK: - View State
data class HangoutDetailsViewState(
    val uiModel: HangoutDetails.UIModel? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT,
    val membersData: GroupedMembersData? = null,
    val currentUserId: String? = null
) : FeatureState {

    val isPrivate: Boolean
        get() = uiModel?.accessType == AppUIEntities.AccessType.PRIVATE

    /** Owner/VP may edit the hangout. Mirrors iOS `isEditable`. */
    val isEditable: Boolean
        get() = uiModel?.userActivityType == AppUIEntities.UserActivityRole.VISE_PRESIDENT ||
            uiModel?.userActivityType == AppUIEntities.UserActivityRole.PRESIDENT

    /** Any joined non-member role may create events. Mirrors iOS `canCreateEvent`. */
    val canCreateEvent: Boolean
        get() = uiModel != null &&
            uiModel.userActivityType != AppUIEntities.UserActivityRole.MEMBER &&
            uiModel.userActivityType != AppUIEntities.UserActivityRole.NOT_JOINED

    val hasJoined: Boolean
        get() = uiModel != null &&
            uiModel.userActivityType != AppUIEntities.UserActivityRole.NOT_JOINED

    enum class SegmentTypes(
        @StringRes private val titleRes: Int
    ) : SegmentedPickerOption {

        ABOUT(R.string.hangouts_about_label),
        MEMBERS(R.string.hangouts_members_title);

        // Resolved on read; see ClubsDetailsModel.SegmentTypes.
        override val title: String get() = LanguageManager.string(titleRes)

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes = when (index) {
                0 -> ABOUT
                1 -> MEMBERS
                else -> ABOUT
            }
        }

        fun toIndex(): Int = when (this) {
            ABOUT -> 0
            MEMBERS -> 1
        }
    }
}

// MARK: - Actions
sealed class HangoutDetailsAction : FeatureAction {
    object FetchData : HangoutDetailsAction()
    object BackTapped : HangoutDetailsAction()
    data class SegmentChanged(val segment: HangoutDetailsViewState.SegmentTypes) : HangoutDetailsAction()
    object EditTapped : HangoutDetailsAction()
    object JoinTapped : HangoutDetailsAction()
    object ExitTapped : HangoutDetailsAction()
    object SeeAllMembersTapped : HangoutDetailsAction()
    data class MemberTapped(val member: MemberCellModel) : HangoutDetailsAction()
}