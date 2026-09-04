//
//  GroupsListViewModel.kt
//  Groups
//
//  Created by Huseyn Hasanov on 23.01.26
//

package com.bonjur.groups.presentation

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.events.navigation.EventsScreens
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.models.GroupsPaginationQuery
import com.bonjur.groups.domain.useCase.GroupsUseCase
import com.bonjur.groups.presentation.models.GroupsListAction
import com.bonjur.groups.presentation.models.GroupsListInputData
import com.bonjur.groups.presentation.models.GroupsListSideEffect
import com.bonjur.groups.presentation.models.GroupsListViewState
import com.bonjur.hangouts.navigation.HangoutsScreens
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsListViewModel @Inject constructor(
    private val useCase: GroupsUseCase
) : FeatureViewModel<GroupsListViewState, GroupsListAction, GroupsListSideEffect>(
    GroupsListViewState()
) {
    private lateinit var inputData: GroupsListInputData

    // Each tab owns its own Navigator instance (see AppTabBar); the Hilt-injected
    // singleton drives the ROOT NavHost, which has no club/event/hangout details
    // destination — navigating with it crashed with "destination ... cannot be found
    // in the navigation graph". The tab's navigator is handed in from the screen.
    private lateinit var navigator: Navigator

    private val searchDebounceMs = 300L
    /** Last page index fetched per tab. Pages are appended; the previous version grew
     *  `size` and refetched page 0 every time, which re-downloaded the whole list on
     *  every scroll and reset the scroll position. */
    private var clubsPage = 0
    private var eventsPage = 0
    private var hangoutsPage = 0
    private var isLoadingMoreClubs = false
    private var isLoadingMoreEvents = false
    private var isLoadingMoreHangouts = false
    private var searchJob: Job? = null

    fun init(inputData: GroupsListInputData, navigator: Navigator) {
        this.navigator = navigator
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: GroupsListAction) {
        when (action) {
            GroupsListAction.FetchData -> fetchData()
            GroupsListAction.LoadMoreClubs -> loadMoreClubs()
            GroupsListAction.LoadMoreEvents -> loadMoreEvents()

            GroupsListAction.LoadMoreHangouts -> loadMoreHangouts()
            is GroupsListAction.SegmentChanged -> handleSegmentChanged(action.segment)
            is GroupsListAction.SearchTextChanged -> searchChanged(action.text)
            is GroupsListAction.ClubItemTapped -> clubItemTapped(action.clubId)
            is GroupsListAction.EventItemTapped -> eventItemTapped(action.eventId)
            is GroupsListAction.HangoutItemTapped -> hangoutItemTapped(action.hangoutId)
            is GroupsListAction.EmptyStateActionTapped -> emptyStateActionTapped(action.segment)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postEffect(GroupsListSideEffect.Loading(true))
            getClubs()
            getEvents()
            getHangouts()
            postEffect(GroupsListSideEffect.Loading(false))
        }
    }

    private suspend fun getClubs() {
        try {
            val result = useCase.fetchClubs(makeQuery(page = 0))
            clubsPage = result.page
            updateState(
                state.copy(
                    uiModel = state.uiModel.copy(clubs = result.items),
                    clubsHasMore = result.hasMore
                )
            )
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private suspend fun getEvents() {
        try {
            val result = useCase.fetchEvents(makeQuery(page = 0))
            eventsPage = result.page
            updateState(
                state.copy(
                    uiModel = state.uiModel.copy(events = result.items),
                    eventsHasMore = result.hasMore
                )
            )
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private suspend fun getHangouts() {
        try {
            val result = useCase.fetchHangouts(makeQuery(page = 0))
            hangoutsPage = result.page
            updateState(
                state.copy(
                    uiModel = state.uiModel.copy(hangouts = result.items),
                    hangoutsHasMore = result.hasMore
                )
            )
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private fun loadMoreClubs() {
        if (isLoadingMoreClubs || !state.clubsHasMore) return
        isLoadingMoreClubs = true
        val nextPage = clubsPage + 1
        viewModelScope.launch {
            try {
                val result = useCase.fetchClubs(makeQuery(nextPage))
                clubsPage = result.page
                updateState(
                    state.copy(
                        uiModel = state.uiModel.copy(
                            clubs = appendPage(state.uiModel.clubs, result.items) { it.id }
                        ),
                        clubsHasMore = result.hasMore
                    )
                )
            } catch (e: ApiException) {
                // Stop paging rather than retry-looping the loader on every scroll.
                updateState(state.copy(clubsHasMore = false))
                postEffect(GroupsListSideEffect.Error(e))
            } finally {
                isLoadingMoreClubs = false
            }
        }
    }

    private fun loadMoreEvents() {
        if (isLoadingMoreEvents || !state.eventsHasMore) return
        isLoadingMoreEvents = true
        val nextPage = eventsPage + 1
        viewModelScope.launch {
            try {
                val result = useCase.fetchEvents(makeQuery(nextPage))
                eventsPage = result.page
                updateState(
                    state.copy(
                        uiModel = state.uiModel.copy(
                            events = appendPage(state.uiModel.events, result.items) { it.id }
                        ),
                        eventsHasMore = result.hasMore
                    )
                )
            } catch (e: ApiException) {
                updateState(state.copy(eventsHasMore = false))
                postEffect(GroupsListSideEffect.Error(e))
            } finally {
                isLoadingMoreEvents = false
            }
        }
    }

    private fun loadMoreHangouts() {
        if (isLoadingMoreHangouts || !state.hangoutsHasMore) return
        isLoadingMoreHangouts = true
        val nextPage = hangoutsPage + 1
        viewModelScope.launch {
            try {
                val result = useCase.fetchHangouts(makeQuery(nextPage))
                hangoutsPage = result.page
                updateState(
                    state.copy(
                        uiModel = state.uiModel.copy(
                            hangouts = appendPage(state.uiModel.hangouts, result.items) { it.id }
                        ),
                        hangoutsHasMore = result.hasMore
                    )
                )
            } catch (e: ApiException) {
                updateState(state.copy(hangoutsHasMore = false))
                postEffect(GroupsListSideEffect.Error(e))
            } finally {
                isLoadingMoreHangouts = false
            }
        }
    }

    /**
     * Appends a page, dropping rows already on screen. The server re-sorts by
     * `modifiedAt`, so a row can shift across the page boundary and arrive twice —
     * and a duplicate key crashes LazyColumn.
     */
    private fun <T, ID> appendPage(
        existing: List<T>,
        newItems: List<T>,
        id: (T) -> ID
    ): List<T> {
        val seen = existing.mapTo(mutableSetOf(), id)
        return existing + newItems.filter { seen.add(id(it)) }
    }

    private fun searchChanged(text: String) {
        updateState(state.copy(searchText = text))
        clubsPage = 0
        eventsPage = 0
        hangoutsPage = 0

        // Pure server-side search, mirroring iOS: debounce, then refetch all three
        // activity lists with the keyword. No local filtering.
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(searchDebounceMs)
            getClubs()
            getEvents()
            getHangouts()
        }
    }

    private fun makeQuery(page: Int): GroupsPaginationQuery {
        return GroupsPaginationQuery(
            page = page,
            size = PAGE_SIZE,
            keyword = currentKeyword()
        )
    }

    private fun currentKeyword(): String? = state.searchText.trim().ifEmpty { null }

    private fun handleSegmentChanged(segment: GroupsListViewState.SegmentType) {
        updateState(state.copy(selectedSegment = segment))
    }

    private fun clubItemTapped(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(ClubsScreens.Details.route, ClubDetailsInputData(clubId = id))
        }
    }

    private fun eventItemTapped(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(EventsScreens.Details.route, EventDetailsInputData(eventId = id))
        }
    }

    // Clubs/events send the user somewhere to join one; hangouts can be started
    // outright, so that tab opens the create flow. Mirrors iOS GroupsListViewModel.
    private fun emptyStateActionTapped(segment: GroupsListViewState.SegmentType) {
        val route = when (segment) {
            GroupsListViewState.SegmentType.CLUBS -> ClubsScreens.List.route
            GroupsListViewState.SegmentType.EVENTS -> EventsScreens.List.route
            GroupsListViewState.SegmentType.HANGOUTS -> HangoutsScreens.Create.route
        }
        viewModelScope.launch {
            navigator.navigateTo(route)
        }
    }

    private fun hangoutItemTapped(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(HangoutsScreens.Details.route, HangoutDetailsInputData(hangoutId = id))
        }
    }

    private companion object {
        const val PAGE_SIZE = 10
    }
}
