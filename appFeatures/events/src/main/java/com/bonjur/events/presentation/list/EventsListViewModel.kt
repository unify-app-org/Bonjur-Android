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
import com.bonjur.events.presentation.list.models.EventsListAction
import com.bonjur.events.presentation.list.models.EventsListInputData
import com.bonjur.events.presentation.list.models.EventsListSideEffect
import com.bonjur.events.presentation.list.models.EventsListViewState
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun init(inputData: EventsListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: EventsListAction) {
        when (action) {
            EventsListAction.FetchData -> fetchData()
            is EventsListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
            is EventsListAction.FilterSelected -> handleFilterSelected(action.items)
            is EventsListAction.EventItemTapped -> handleItemTap(action.id)
            EventsListAction.Dismiss -> dismiss()
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
            navigator.navigateTo(EventsScreens.Details.route)
        }
    }

    private suspend fun getEventsData() {
        postEffect(EventsListSideEffect.Loading(true))
        try {
            val data = useCase.fetchEventsData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(events = data))
            )
        } catch (e: ApiException) {
            postEffect(EventsListSideEffect.Error(e))
        } finally {
            postEffect(EventsListSideEffect.Loading(false))
        }
    }

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
    }

    private fun handleFilterSelected(items: List<FilterView.Items>) {
        // Handle filter logic
    }
}