package com.bonjur.clubs.presentation.model

import com.bonjur.appfoundation.*
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption

// MARK: - ClubDetails input
data class ClubDetailsInputData(
    val clubId: Int
)

// MARK: - Side effects
sealed class ClubDetailsSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ClubDetailsSideEffect()
}

// MARK: - View State
data class ClubDetailsViewState(
    val uiModel: ClubsDetails.UIModel? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT
) : FeatureState {

    enum class SegmentTypes(
        override val title: String
    ) : SegmentedPickerOption {

        ABOUT("About"),
        EVENTS("Events"),
        MEMBERS("Members");

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes {
                return when (index) {
                    0 -> SegmentTypes.ABOUT
                    1 -> SegmentTypes.EVENTS
                    2 -> SegmentTypes.MEMBERS
                    else -> SegmentTypes.ABOUT
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                SegmentTypes.ABOUT -> 0
                SegmentTypes.EVENTS -> 1
                SegmentTypes.MEMBERS -> 2
            }
        }
    }
}

// MARK: - Feature Action
sealed class ClubDetailsAction : FeatureAction {
    object FetchData : ClubDetailsAction()
    object BackTapped : ClubDetailsAction()
    data class SegmentChanged(val segment: ClubDetailsViewState.SegmentTypes) : ClubDetailsAction()
}