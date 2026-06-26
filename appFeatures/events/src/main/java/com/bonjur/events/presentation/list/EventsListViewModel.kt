//
//  EventsListViewModel.kt
//  Events
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.events.presentation.list

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.navigation.EventsScreens
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.list.models.EventsListAction
import com.bonjur.events.presentation.list.models.EventsListInputData
import com.bonjur.events.presentation.list.models.EventsListSideEffect
import com.bonjur.events.presentation.list.models.EventsListViewState
import com.bonjur.navigation.ClubDetailsNavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.SharedRoutes
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsListViewModel @Inject constructor(
    private val useCase: EventsUseCase
) : FeatureViewModel<EventsListViewState, EventsListAction, EventsListSideEffect>(
    EventsListViewState()
) {
    private lateinit var inputData: EventsListInputData
    private lateinit var navigator: Navigator

    private val paginationStep = 10
    private val searchDebounceMs = 300L
    private var eventsSize = paginationStep
    private var isLoadingMore = false
    private var hasMore = true
    private var selectedCategoryIds: List<Int> = emptyList()
    private var searchJob: Job? = null

    fun init(inputData: EventsListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: EventsListAction) {
        when (action) {
            EventsListAction.FetchData -> fetchData()
            EventsListAction.LoadMore -> loadMore()
            is EventsListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
            is EventsListAction.FilterSelected -> handleFilterSelected(action.items)
            is EventsListAction.EventItemTapped -> handleItemTap(action.id)
            is EventsListAction.JoinEvent -> joinEvent(action.id)
            is EventsListAction.ClubTapped -> handleClubTap(action.clubId)
            EventsListAction.Dismiss -> dismiss()
        }
    }

    private fun handleClubTap(clubId: Int) {
        if (clubId == 0) return
        viewModelScope.launch {
            navigator.navigateTo(SharedRoutes.CLUB_DETAILS, ClubDetailsNavArgs(clubId))
        }
    }

    private fun dismiss() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getEventsData()
            getFilters()
        }
    }

    private fun handleItemTap(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(
                EventsScreens.Details.route,
                EventDetailsInputData(eventId = id)
            )
        }
    }

    private fun joinEvent(id: String) {
        viewModelScope.launch {
            postEffect(EventsListSideEffect.Loading(true))
            try {
                useCase.joinEvent(id)
                getEventsData()
            } catch (e: ApiException) {
                postEffect(EventsListSideEffect.Error(e))
            } finally {
                postEffect(EventsListSideEffect.Loading(false))
            }
        }
    }

    private suspend fun getEventsData(showLoading: Boolean = true) {
        if (showLoading) postEffect(EventsListSideEffect.Loading(true))
        try {
            val data = useCase.fetchEventsData(
                categoryIds = selectedCategoryIds,
                keyword = currentKeyword(),
                page = 0,
                size = eventsSize
            )
            hasMore = data.size >= eventsSize
            updateState(
                state.copy(uiModel = state.uiModel.copy(events = data))
            )
        } catch (e: ApiException) {
            postEffect(EventsListSideEffect.Error(e))
        } finally {
            if (showLoading) postEffect(EventsListSideEffect.Loading(false))
        }
    }

    private fun loadMore() {
        if (isLoadingMore || !hasMore) return
        isLoadingMore = true
        val previousSize = eventsSize
        eventsSize += paginationStep

        viewModelScope.launch {
            try {
                val previousCount = state.uiModel.events.size
                val data = useCase.fetchEventsData(
                    categoryIds = selectedCategoryIds,
                    keyword = currentKeyword(),
                    page = 0,
                    size = eventsSize
                )
                hasMore = data.size > previousCount
                updateState(state.copy(uiModel = state.uiModel.copy(events = data)))
            } catch (e: ApiException) {
                eventsSize = previousSize
                postEffect(EventsListSideEffect.Error(e))
            } finally {
                isLoadingMore = false
            }
        }
    }

    private fun currentKeyword(): String? =
        state.uiModel.searchText.trim().ifEmpty { null }

    private suspend fun getFilters() {
        try {
            val data = useCase.fetchFilterData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(filters = data))
            )
        } catch (e: ApiException) {
            postEffect(EventsListSideEffect.Error(e))
        }
    }

    private fun handleSearchTextChanged(text: String) {
        updateState(
            state.copy(uiModel = state.uiModel.copy(searchText = text))
        )
        eventsSize = paginationStep
        hasMore = true

        // Debounced server-side search, mirroring iOS. No local filtering.
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(searchDebounceMs)
            getEventsData(showLoading = false)
        }
    }

    private fun handleFilterSelected(items: List<FilterView.Items>) {
        selectedCategoryIds = items.map { it.id }
        eventsSize = paginationStep
        hasMore = true
        viewModelScope.launch {
            getEventsData()
        }
    }
}