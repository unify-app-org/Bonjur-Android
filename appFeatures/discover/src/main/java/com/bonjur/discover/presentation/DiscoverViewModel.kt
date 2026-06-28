//
//  DiscoverViewModel.kt
//  Discover
//
//  Created by Huseyn Hasanov on 11.01.26
//

package com.bonjur.discover.presentation

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.communities.navigation.CommunitiesScreens
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.discover.domain.useCase.DiscoverUseCase
import com.bonjur.discover.presentation.models.DiscoverAction
import com.bonjur.discover.presentation.models.DiscoverInputData
import com.bonjur.discover.presentation.models.DiscoverSideEffect
import com.bonjur.discover.presentation.models.DiscoverViewState
import com.bonjur.events.navigation.EventsScreens
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.hangouts.navigation.HangoutsScreens
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.navigation.MainScreen
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val useCase: DiscoverUseCase
) : FeatureViewModel<DiscoverViewState, DiscoverAction, DiscoverSideEffect>(
    DiscoverViewState()
) {
    private lateinit var inputData: DiscoverInputData
    private lateinit var navigator: Navigator
    private var selectedCategoryIds: List<Int> = emptyList()

    private val paginationStep = 10
    private var communitiesSize = paginationStep
    private var clubsSize = paginationStep
    private var eventsSize = paginationStep
    private var hangoutsSize = paginationStep
    private var isLoadingMoreCommunities = false
    private var isLoadingMoreClubs = false
    private var isLoadingMoreEvents = false
    private var isLoadingMoreHangouts = false
    private var hasMoreCommunities = true
    private var hasMoreClubs = true
    private var hasMoreEvents = true
    private var hasMoreHangouts = true

    fun init(inputData: DiscoverInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: DiscoverAction) {
        when (action) {
            DiscoverAction.FetchData -> fetchData()
            DiscoverAction.PullToRefresh -> pullToRefresh()
            DiscoverAction.RefreshActivities -> refreshActivities()
            DiscoverAction.ProfileTapped -> profileTapped()
            DiscoverAction.NotificationTapped ->
                viewModelScope.launch { navigator.navigateTo(com.bonjur.navigation.SharedRoutes.NOTIFICATION_FEED) }
            is DiscoverAction.FilterChanged -> filterChanged(action.categoryIds)
            is DiscoverAction.LoadMore -> loadMore(action.type)
            is DiscoverAction.JoinHangout -> joinHangout(action.hangoutId)
            is DiscoverAction.ViewAllTapped -> viewAllTapped(action.type)
            is DiscoverAction.CommunityItemTapped -> communityItemTapped(action.communityId)
            is DiscoverAction.CLubItemTapped -> clubItemTapped(action.clubId)
            is DiscoverAction.EventItemTapped -> eventItemTapped(action.eventId)
            is DiscoverAction.HangoutItemTapped -> hangoutItemTapped(action.hangoutId)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            // Initial load streams in inline — no blocking overlay (only filters show it).
            fetchUserData()
            fetchFilterData()
            fetchCommunitiesData()
            fetchClubsData()
            fetchEventsData()
            fetchHangoutsData()
        }
    }

    /// Refetch only the 4 activity sections when Discover reappears, so changes
    /// made in a detail screen (join/request/exit/edit) show up. Active filter
    /// and current page depth are preserved. User + categories are NOT refetched.
    private fun refreshActivities() {
        viewModelScope.launch {
            // Silent refresh on reappear — no overlay (only filters show loading).
            fetchCommunitiesData()
            fetchClubsData()
            fetchEventsData()
            fetchHangoutsData()
        }
    }

    /// Pull-to-refresh: same full refetch as fetchData (current page depths and
    /// active filter preserved), but drives the pull spinner via isRefreshing
    /// instead of the full-screen Loading effect. Mirrors iOS `.refreshable`.
    private fun pullToRefresh() {
        viewModelScope.launch {
            updateState(state.copy(isRefreshing = true))
            fetchUserData()
            fetchFilterData()
            fetchCommunitiesData()
            fetchClubsData()
            fetchEventsData()
            fetchHangoutsData()
            updateState(state.copy(isRefreshing = false))
        }
    }

    private fun filterChanged(categoryIds: List<Int>) {
        selectedCategoryIds = categoryIds
        applySelectedCategoryIdsToFilters()
        resetPagination()
        viewModelScope.launch {
            postEffect(DiscoverSideEffect.Loading(true))
            fetchCommunitiesData()
            fetchClubsData()
            fetchEventsData()
            fetchHangoutsData()
            postEffect(DiscoverSideEffect.Loading(false))
        }
    }

    private fun loadMore(type: AppUIEntities.ActivityType) {
        when (type) {
            AppUIEntities.ActivityType.COMMUNITY -> loadMoreCommunities()
            AppUIEntities.ActivityType.CLUBS -> loadMoreClubs()
            AppUIEntities.ActivityType.EVENTS -> loadMoreEvents()
            AppUIEntities.ActivityType.HANG_OUTS -> loadMoreHangouts()
        }
    }

    private fun loadMoreCommunities() {
        if (isLoadingMoreCommunities || !hasMoreCommunities) return
        isLoadingMoreCommunities = true
        val previousSize = communitiesSize
        communitiesSize += paginationStep
        viewModelScope.launch {
            try {
                val data = useCase.fetchCommunitiesData(size = communitiesSize, categoryIds = selectedCategoryIds)
                hasMoreCommunities = data.size > state.uiModel.communities.size
                updateState(state.copy(uiModel = state.uiModel.copy(communities = data)))
            } catch (e: ApiException) {
                communitiesSize = previousSize
                postEffect(DiscoverSideEffect.Error(e))
            } finally {
                isLoadingMoreCommunities = false
            }
        }
    }

    private fun loadMoreClubs() {
        if (isLoadingMoreClubs || !hasMoreClubs) return
        isLoadingMoreClubs = true
        val previousSize = clubsSize
        clubsSize += paginationStep
        viewModelScope.launch {
            try {
                val data = useCase.fetchClubsData(size = clubsSize, categoryIds = selectedCategoryIds)
                hasMoreClubs = data.size > state.uiModel.clubs.size
                updateState(state.copy(uiModel = state.uiModel.copy(clubs = data)))
            } catch (e: ApiException) {
                clubsSize = previousSize
                postEffect(DiscoverSideEffect.Error(e))
            } finally {
                isLoadingMoreClubs = false
            }
        }
    }

    private fun loadMoreEvents() {
        if (isLoadingMoreEvents || !hasMoreEvents) return
        isLoadingMoreEvents = true
        val previousSize = eventsSize
        eventsSize += paginationStep
        viewModelScope.launch {
            try {
                val data = useCase.fetchEvents(size = eventsSize, categoryIds = selectedCategoryIds)
                hasMoreEvents = data.size > state.uiModel.events.size
                updateState(state.copy(uiModel = state.uiModel.copy(events = data)))
            } catch (e: ApiException) {
                eventsSize = previousSize
                postEffect(DiscoverSideEffect.Error(e))
            } finally {
                isLoadingMoreEvents = false
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
                val data = useCase.fetchHangoutsData(size = hangoutsSize, categoryIds = selectedCategoryIds)
                hasMoreHangouts = data.size > state.uiModel.hangouts.size
                updateState(state.copy(uiModel = state.uiModel.copy(hangouts = data)))
            } catch (e: ApiException) {
                hangoutsSize = previousSize
                postEffect(DiscoverSideEffect.Error(e))
            } finally {
                isLoadingMoreHangouts = false
            }
        }
    }

    private fun joinHangout(id: String) {
        viewModelScope.launch {
            postEffect(DiscoverSideEffect.Loading(true))
            try {
                useCase.joinHangout(id)
                val data = useCase.fetchHangoutsData(size = hangoutsSize, categoryIds = selectedCategoryIds)
                updateState(state.copy(uiModel = state.uiModel.copy(hangouts = data)))
            } catch (e: ApiException) {
                postEffect(DiscoverSideEffect.Error(e))
            } finally {
                postEffect(DiscoverSideEffect.Loading(false))
            }
        }
    }

    private fun resetPagination() {
        communitiesSize = paginationStep
        clubsSize = paginationStep
        eventsSize = paginationStep
        hangoutsSize = paginationStep
        hasMoreCommunities = true
        hasMoreClubs = true
        hasMoreEvents = true
        hasMoreHangouts = true
    }

    private fun applySelectedCategoryIdsToFilters() {
        val updated = applySelectedCategoryIds(state.uiModel.filters)
        updateState(state.copy(uiModel = state.uiModel.copy(filters = updated)))
    }

    private fun applySelectedCategoryIds(
        filters: List<FilterView.Model>
    ): List<FilterView.Model> = filters.map { section ->
        section.copy(
            items = section.items.map { it.copy(selected = selectedCategoryIds.contains(it.id)) }
        )
    }

    private fun profileTapped() {
        inputData.onProfileTab?.invoke()
    }

    private fun communityItemTapped(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(
                CommunitiesScreens.Details.route,
                CommunityDetailInputData(communityId = id)
            )
        }
    }

    private fun clubItemTapped(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(
                ClubsScreens.Details.route,
                ClubDetailsInputData(clubId = id)
            )
        }
    }

    private fun eventItemTapped(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(
                EventsScreens.Details.route,
                EventDetailsInputData(eventId = id)
            )
        }
    }

    private fun hangoutItemTapped(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(
                HangoutsScreens.Details.route,
                HangoutDetailsInputData(hangoutId = id)
            )
        }
    }

    private fun viewAllTapped(type: AppUIEntities.ActivityType) {
        viewModelScope.launch {
            when (type) {
                AppUIEntities.ActivityType.COMMUNITY -> {
                    // Handle community view all
                }
                AppUIEntities.ActivityType.EVENTS -> {
                    navigator.navigateTo(MainScreen.Events.route)
                }
                AppUIEntities.ActivityType.CLUBS -> {
                    inputData.onTabChange?.invoke()
                }
                AppUIEntities.ActivityType.HANG_OUTS -> {
                    navigator.navigateTo(MainScreen.Hangouts.route)
                }
            }
        }
    }

    private suspend fun fetchUserData() {
        try {
            val data = useCase.fetchUserData()
            updateState(state.copy(uiModel = state.uiModel.copy(user = data)))
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchFilterData() {
        try {
            val data = applySelectedCategoryIds(useCase.fetchFilterData())
            updateState(state.copy(uiModel = state.uiModel.copy(filters = data)))
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchCommunitiesData() {
        try {
            val data = useCase.fetchCommunitiesData(size = communitiesSize, categoryIds = selectedCategoryIds)
            hasMoreCommunities = data.size >= communitiesSize
            updateState(state.copy(uiModel = state.uiModel.copy(communities = data)))
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchClubsData() {
        try {
            val data = useCase.fetchClubsData(size = clubsSize, categoryIds = selectedCategoryIds)
            hasMoreClubs = data.size >= clubsSize
            updateState(state.copy(uiModel = state.uiModel.copy(clubs = data)))
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchEventsData() {
        try {
            val data = useCase.fetchEvents(size = eventsSize, categoryIds = selectedCategoryIds)
            hasMoreEvents = data.size >= eventsSize
            updateState(state.copy(uiModel = state.uiModel.copy(events = data)))
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchHangoutsData() {
        try {
            val data = useCase.fetchHangoutsData(size = hangoutsSize, categoryIds = selectedCategoryIds)
            hasMoreHangouts = data.size >= hangoutsSize
            updateState(state.copy(uiModel = state.uiModel.copy(hangouts = data)))
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }
}