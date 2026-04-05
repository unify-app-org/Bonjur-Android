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
import com.bonjur.discover.domain.useCase.DiscoverUseCase
import com.bonjur.discover.presentation.models.DiscoverAction
import com.bonjur.discover.presentation.models.DiscoverInputData
import com.bonjur.discover.presentation.models.DiscoverSideEffect
import com.bonjur.discover.presentation.models.DiscoverViewState
import com.bonjur.events.navigation.EventsScreens
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

    fun init(inputData: DiscoverInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: DiscoverAction) {
        when (action) {
            DiscoverAction.FetchData -> fetchData()
            is DiscoverAction.ViewAllTapped -> viewAllTapped(action.type)
            is DiscoverAction.CommunityItemTapped -> communityItemTapped(action.communityId)
            is DiscoverAction.CLubItemTapped -> clubItemTapped(action.clubId)
            is DiscoverAction.EventItemTapped -> eventItemTapped(action.eventId)
            is DiscoverAction.HangoutItemTapped -> hangoutItemTapped(action.hangoutId)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchUserData()
            fetchFilterData()
            fetchCommunitiesData()
            fetchClubsData()
            fetchEventsData()
            fetchHangoutsData()
        }
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
            navigator.navigateTo(EventsScreens.Details.route)
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
        postEffect(DiscoverSideEffect.Loading(true))
        try {
            val data = useCase.fetchUserData()
            updateState (
                state.copy(uiModel = state.uiModel.copy(user = data))
            )
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchCommunitiesData() {
        try {
            val data = useCase.fetchCommunitiesData()
            updateState (
                state.copy(uiModel = state.uiModel.copy(communities = data))
            )
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchFilterData() {
        try {
            val data = useCase.fetchFilterData()
            updateState (
                state.copy(uiModel = state.uiModel.copy(filters = data))
            )
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchClubsData() {
        try {
            val data = useCase.fetchClubsData()
            updateState (
                state.copy(uiModel = state.uiModel.copy(clubs = data))
            )
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchEventsData() {
        try {
            val data = useCase.fetchEvents()
            updateState (
                state.copy(uiModel = state.uiModel.copy(events = data))
            )
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        }
    }

    private suspend fun fetchHangoutsData() {
        try {
            val data = useCase.fetchHangoutsData()
            updateState (
                state.copy(uiModel = state.uiModel.copy(hangouts = data))
            )
        } catch (e: ApiException) {
            postEffect(DiscoverSideEffect.Error(e))
        } finally {
            postEffect(DiscoverSideEffect.Loading(false))
        }
    }
}