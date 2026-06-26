package com.bonjur.events.presentation.details.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption

import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel

// MARK: - EventDetails input
data class EventDetailsInputData(
    val eventId: String
)

// MARK: - Side effects
sealed class EventDetailsSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : EventDetailsSideEffect()
}

// MARK: - View State
data class EventDetailsViewState(
    val uiModel: EventsDetails.UIModel? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT,
    val isFileUploadReachedMaxLimit: Boolean = false,
    val membersData: GroupedMembersData? = null,
    val currentUserId: String? = null
) : FeatureState {

    val isPrivate: Boolean
        get() = uiModel?.accessType == AppUIEntities.AccessType.PRIVATE

    enum class SegmentTypes(
        override val title: String
    ) : SegmentedPickerOption {
        ABOUT("About"),
        MEMBERS("Members");

        override val id: String get() = name

        fun toIndex(): Int = ordinal

        companion object {
            fun fromIndex(index: Int): SegmentTypes = values()[index]
        }
    }
}

// MARK: - Feature Action
sealed class EventDetailsAction : FeatureAction {
    object FetchData : EventDetailsAction()
    object BackTapped : EventDetailsAction()
    data class SegmentChanged(val segment: EventDetailsViewState.SegmentTypes) : EventDetailsAction()
    object EditTapped : EventDetailsAction()
    object JoinTapped : EventDetailsAction()
    object ExitTapped : EventDetailsAction()
    object ClubTapped : EventDetailsAction()
    object SeeAllMembersTapped : EventDetailsAction()
    data class MemberTapped(val member: MemberCellModel) : EventDetailsAction()
}