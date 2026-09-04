//
//  GroupsListModels.kt
//  Groups
//
//  Created by Huseyn Hasanov on 23.01.26
//

package com.bonjur.groups.presentation.models

import androidx.annotation.StringRes
import com.bonjur.designsystem.R as DesignR
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.groups.R
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
    val searchText: String = "",
    /** Per-tab paging flags; drive the end-of-list loader in each activity list. */
    val clubsHasMore: Boolean = false,
    val eventsHasMore: Boolean = false,
    val hangoutsHasMore: Boolean = false
) : FeatureState {
    data class UIModel(
        val events: List<EventsCardModel> = emptyList(),
        val clubs: List<ClubCardModel> = emptyList(),
        val hangouts: List<HangoutsCardModel> = emptyList()
    )

    enum class SegmentType(@StringRes private val titleRes: Int) {
        CLUBS(DesignR.string.clubs),
        EVENTS(DesignR.string.events),
        HANGOUTS(DesignR.string.hangouts);

        // Resolved on read: enum constructors run at class load, before
        // LanguageManager has a Context, and would freeze the first language.
        val title: String get() = LanguageManager.string(titleRes)

        /** One-line explanation of what this tab lists, shown above its cards. */
        val caption: String
            get() = when (this) {
                CLUBS -> LanguageManager.string(R.string.groups_clubs_desc)
                EVENTS -> LanguageManager.string(R.string.groups_events_desc)
                HANGOUTS -> LanguageManager.string(R.string.groups_hangouts_desc)
            }

        /** Copy shown when this tab has no items. */
        val emptyText: String
            get() = when (this) {
                CLUBS -> LanguageManager.string(R.string.groups_clubs_empty)
                EVENTS -> LanguageManager.string(R.string.groups_events_empty)
                HANGOUTS -> LanguageManager.string(R.string.groups_hangouts_empty)
            }

        val emptyButtonTitle: String
            get() = when (this) {
                CLUBS -> LanguageManager.string(R.string.groups_explore_clubs)
                EVENTS -> LanguageManager.string(R.string.groups_explore_events)
                HANGOUTS -> LanguageManager.string(R.string.groups_start_hangout)
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
    object LoadMoreEvents : GroupsListAction()
    object LoadMoreHangouts : GroupsListAction()
    data class SegmentChanged(val segment: GroupsListViewState.SegmentType) : GroupsListAction()
    data class SearchTextChanged(val text: String) : GroupsListAction()
    data class ClubItemTapped(val clubId: Int) : GroupsListAction()
    data class EventItemTapped(val eventId: String) : GroupsListAction()
    data class HangoutItemTapped(val hangoutId: String) : GroupsListAction()
    data class EmptyStateActionTapped(val segment: GroupsListViewState.SegmentType) : GroupsListAction()
}
