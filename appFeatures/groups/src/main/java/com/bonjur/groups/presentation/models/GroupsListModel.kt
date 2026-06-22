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
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
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
    val selectedSegment: SegmentType = SegmentType.CLUBS,
    val searchText: String = ""
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

        /** One-line explanation of what this tab lists, shown above its cards. */
        val caption: String
            get() = when (this) {
                CLUBS -> "Clubs you belong to or run. Members organise events and meet up."
                EVENTS -> "Events you've joined or requested. Each one is hosted by a club."
                HANGOUTS -> "Casual, one-off meetups — no club needed. Anyone can start one."
            }

        /** Copy shown when this tab has no items. */
        val emptyText: String
            get() = when (this) {
                CLUBS -> "You haven't joined or created any clubs yet. Explore communities or start your own."
                EVENTS -> "No events yet. Join a club to discover its events, or request to attend one."
                HANGOUTS -> "No hangouts yet. They're casual meetups — start one and invite friends."
            }

        val emptyButtonTitle: String
            get() = when (this) {
                CLUBS -> "Explore clubs"
                EVENTS -> "Explore events"
                HANGOUTS -> "Start a hangout +"
            }

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
    object LoadMoreClubs : GroupsListAction()
    object LoadMoreHangouts : GroupsListAction()
    data class SegmentChanged(val segment: GroupsListViewState.SegmentType) : GroupsListAction()
    data class SearchTextChanged(val text: String) : GroupsListAction()
    data class ClubItemTapped(val clubId: Int) : GroupsListAction()
    data class EventItemTapped(val eventId: String) : GroupsListAction()
    data class HangoutItemTapped(val hangoutId: String) : GroupsListAction()
}
