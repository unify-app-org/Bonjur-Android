package com.bonjur.communities.presentation.detail.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption

data class CommunityDetailInputData(
    val communityId: Int
)

sealed class CommunityDetailSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : CommunityDetailSideEffect()
}

data class CommunityDetailViewState(
    val uiModel: CommunityDetails.UIModel? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT
) : FeatureState {

    enum class SegmentTypes(
        override val title: String
    ) : SegmentedPickerOption {

        ABOUT("About"),
        CLUBS("Clubs");

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes {
                return when (index) {
                    0 -> ABOUT
                    1 -> CLUBS
                    else -> ABOUT
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                ABOUT -> 0
                CLUBS -> 1
            }
        }
    }
}

sealed class CommunityDetailAction : FeatureAction {
    object FetchData : CommunityDetailAction()
    object BackTapped : CommunityDetailAction()
    data class ClubItemTapped(val id: Int) : CommunityDetailAction()
    data class SegmentChanged(val segment: CommunityDetailViewState.SegmentTypes) : CommunityDetailAction()
}
