package com.bonjur.hangouts.presentation.detail.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.hangouts.domain.model.HangoutDetails

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
    val selectedSegment: SegmentTypes = SegmentTypes.ABOUT
) : FeatureState {

    enum class SegmentTypes(
        override val title: String
    ) : SegmentedPickerOption {

        ABOUT("About"),
        MEMBERS("Members");

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
}