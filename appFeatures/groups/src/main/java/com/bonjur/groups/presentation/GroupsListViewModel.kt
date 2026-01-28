//
//  GroupsListViewModel.kt
//  Groups
//
//  Created by Huseyn Hasanov on 23.01.26
//

package com.bonjur.groups.presentation

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.groups.domain.useCase.GroupsUseCase
import com.bonjur.groups.presentation.models.GroupsListAction
import com.bonjur.groups.presentation.models.GroupsListInputData
import com.bonjur.groups.presentation.models.GroupsListSideEffect
import com.bonjur.groups.presentation.models.GroupsListViewState
import com.bonjur.navigation.Navigator
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

    fun init(inputData: GroupsListInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: GroupsListAction) {
        when (action) {
            GroupsListAction.FetchData -> fetchData()
            GroupsListAction.Dismiss -> dismiss()
            is GroupsListAction.SegmentChanged -> handleSegmentChanged(action.segment)
            is GroupsListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getClubs()
            getEvents()
            getHangouts()
        }
    }

    private suspend fun getClubs() {
        postEffect(GroupsListSideEffect.Loading(true))
        try {
            val data = useCase.fetchClubsData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(clubs = data))
            )
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        } finally {
            postEffect(GroupsListSideEffect.Loading(false))
        }
    }

    private suspend fun getEvents() {
        try {
            val data = useCase.fetchEvents()
            updateState(
                state.copy(uiModel = state.uiModel.copy(events = data))
            )
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private suspend fun getHangouts() {
        try {
            val data = useCase.fetchHangoutsData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(hangouts = data))
            )
        } catch (e: ApiException) {
            postEffect(GroupsListSideEffect.Error(e))
        }
    }

    private fun handleSegmentChanged(segment: GroupsListViewState.SegmentType) {
        updateState(state.copy(selectedSegment = segment))
    }

    private fun handleSearchTextChanged(text: String) {
        // Handle search if needed
    }

    private fun dismiss() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}