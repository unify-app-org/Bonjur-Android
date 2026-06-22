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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsListViewModel @Inject constructor(
    private val navigator: Navigator,
    private val useCase: GroupsUseCase
) : FeatureViewModel<GroupsListViewState, GroupsListAction, GroupsListSideEffect>(
    GroupsListViewState()
) {
    private lateinit var inputData: GroupsListInputData

    private val paginationStep = 10
    private var clubsSize = 10
    private var hangoutsSize = 10
    private var isLoadingMoreClubs = false
    private var isLoadingMoreHangouts = false
    private var hasMoreClubs = true
    private var hasMoreHangouts = true

    // Unfiltered server results; the search filter is applied on top of these.
    private var sourceClubs: List<ClubCardModel> = emptyList()
    private var sourceEvents: List<EventsCardModel> = emptyList()
    private var sourceHangouts: List<HangoutsCardModel> = emptyList()

    fun init(inputData: GroupsListInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: GroupsListAction) {
        when (action) {
            GroupsListAction.FetchData -> fetchData()
            GroupsListAction.LoadMoreClubs -> loadMoreClubs()
            GroupsListAction.LoadMoreHangouts -> loadMoreHangouts()
            is GroupsListAction.SegmentChanged -> handleSegmentChanged(action.segment)
            is GroupsListAction.SearchTextChanged -> searchChanged(action.text)
            is GroupsListAction.ClubItemTapped -> clubItemTapped(action.clubId)
            is GroupsListAction.EventItemTapped -> eventItemTapped(action.eventId)
            is GroupsListAction.HangoutItemTapped -> hangoutItemTapped(action.hangoutId)
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
            val clubs = useCase.fetchClubs(makeQuery(clubsSize))
            hasMoreClubs = clubs.size >= clubsSize
            sourceClubs = clubs
            applyClubsSearch()
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private suspend fun getEvents() {
        try {
            sourceEvents = useCase.fetchEvents()
            applyEventsSearch()
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private suspend fun getHangouts() {
        try {
            val hangouts = useCase.fetchHangouts(makeQuery(hangoutsSize))
            hasMoreHangouts = hangouts.size >= hangoutsSize
            sourceHangouts = hangouts
            applyHangoutsSearch()
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private fun loadMoreClubs() {
        if (isLoadingMoreClubs || !hasMoreClubs) return
        isLoadingMoreClubs = true
        val previousSize = clubsSize
        clubsSize += paginationStep
        viewModelScope.launch {
            try {
                val previousCount = sourceClubs.size
                val clubs = useCase.fetchClubs(makeQuery(clubsSize))
                hasMoreClubs = clubs.size > previousCount
                sourceClubs = clubs
                applyClubsSearch()
            } catch (e: ApiException) {
                clubsSize = previousSize
                postEffect(GroupsListSideEffect.Error(e))
            } finally {
                isLoadingMoreClubs = false
            }
        }
    }

    private fun loadMoreHangouts() {
        if (isLoadingMoreHangouts || !hasMoreHangouts) return
        isLoadingMoreHangouts = true
        val previousSize = hangoutsSize
        hangoutsSize += paginationStep
        viewModelScope.launch {
            try {
                val previousCount = sourceHangouts.size
                val hangouts = useCase.fetchHangouts(makeQuery(hangoutsSize))
                hasMoreHangouts = hangouts.size > previousCount
                sourceHangouts = hangouts
                applyHangoutsSearch()
            } catch (e: ApiException) {
                hangoutsSize = previousSize
                postEffect(GroupsListSideEffect.Error(e))
            } finally {
                isLoadingMoreHangouts = false
            }
        }
    }

    private fun searchChanged(text: String) {
        updateState(state.copy(searchText = text))
        clubsSize = paginationStep
        hangoutsSize = paginationStep
        hasMoreClubs = true
        hasMoreHangouts = true

        // Instant local feedback, then refetch clubs + hangouts with the name query.
        // Events are search-filtered locally only (never refetched), mirroring iOS.
        applyClubsSearch()
        applyEventsSearch()
        applyHangoutsSearch()

        viewModelScope.launch {
            getClubs()
            getHangouts()
        }
    }

    private fun applyClubsSearch() {
        val query = searchQuery()
        val filtered = if (query.isEmpty()) {
            sourceClubs
        } else {
            sourceClubs.filter { club ->
                club.name.lowercase().contains(query)
                    || club.communityName.lowercase().contains(query)
                    || club.community.lowercase().contains(query)
            }
        }
        updateState(state.copy(uiModel = state.uiModel.copy(clubs = filtered)))
    }

    private fun applyEventsSearch() {
        val query = searchQuery()
        val filtered = if (query.isEmpty()) {
            sourceEvents
        } else {
            sourceEvents.filter { event ->
                event.name.lowercase().contains(query)
                    || event.club.name.lowercase().contains(query)
                    || event.tags.any { it.title.lowercase().contains(query) }
            }
        }
        updateState(state.copy(uiModel = state.uiModel.copy(events = filtered)))
    }

    private fun applyHangoutsSearch() {
        val query = searchQuery()
        val filtered = if (query.isEmpty()) {
            sourceHangouts
        } else {
            sourceHangouts.filter { hangout ->
                hangout.name.lowercase().contains(query)
                    || hangout.description.lowercase().contains(query)
                    || hangout.tags.any { it.title.lowercase().contains(query) }
            }
        }
        updateState(state.copy(uiModel = state.uiModel.copy(hangouts = filtered)))
    }

    private fun makeQuery(size: Int): GroupsPaginationQuery {
        val name = state.searchText.trim()
        return GroupsPaginationQuery(
            page = 0,
            size = size,
            name = name.ifEmpty { null }
        )
    }

    private fun searchQuery(): String = state.searchText.trim().lowercase()

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

    private fun hangoutItemTapped(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(HangoutsScreens.Details.route, HangoutDetailsInputData(hangoutId = id))
        }
    }
}
