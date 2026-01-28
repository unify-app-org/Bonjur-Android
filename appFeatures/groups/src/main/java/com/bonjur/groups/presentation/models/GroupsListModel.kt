//
//  GroupsListModels.kt
//  Groups
//
//  Created by Huseyn Hasanov on 23.01.26
//

package com.bonjur.groups.presentation.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.clubs.presentation.models.ClubCardModel
import com.bonjur.events.presentation.models.EventsCardModel
import com.bonjur.hangouts.presentation.model.HangoutsCardModel
import com.bonjur.network.model.ApiException

// MARK: - Input Data
data class GroupsListInputData(
    val initialValue: String = ""
)

// MARK: - Side Effects
sealed class GroupsListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : GroupsListSideEffect()
    data class Error(val error: ApiException) : GroupsListSideEffect()
}

// MARK: - View State
data class GroupsListViewState(
    val uiModel: UIModel = UIModel(),
    val selectedSegment: SegmentType = SegmentType.CLUBS
) : FeatureState {
    data class UIModel(
        val events: List<EventsCardModel> = emptyList(),
        val clubs: List<ClubCardModel> = emptyList(),
        val hangouts: List<HangoutsCardModel> = emptyList()
    )

    enum class SegmentType(val title: String) {
        CLUBS("Clubs"),
        EVENTS("Events"),
        HANGOUTS("Hangouts");

        companion object {
            fun fromIndex(index: Int): SegmentType {
                return when (index) {
                    0 -> CLUBS
                    1 -> EVENTS
                    2 -> HANGOUTS
                    else -> CLUBS
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                CLUBS -> 0
                EVENTS -> 1
                HANGOUTS -> 2
            }
        }
    }
}

// MARK: - Actions
sealed class GroupsListAction : FeatureAction {
    object FetchData : GroupsListAction()
    object Dismiss : GroupsListAction()
    data class SegmentChanged(val segment: GroupsListViewState.SegmentType) : GroupsListAction()
    data class SearchTextChanged(val text: String) : GroupsListAction()
}