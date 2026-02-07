//
//  ClubsListViewModel.kt
//  Clubs
//
//  Created by Huseyn Hasanov on 17.01.26
//

package com.bonjur.clubs.presentation.list

import android.R.attr.text
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.list.models.ClubsListAction
import com.bonjur.clubs.presentation.list.models.ClubsListInputData
import com.bonjur.clubs.presentation.list.models.ClubsListSideEffect
import com.bonjur.clubs.presentation.list.models.ClubsListViewState
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubsListViewModel @Inject constructor(
    private val navigator: Navigator,
    private val useCase: ClubsUseCase
) : FeatureViewModel<ClubsListViewState, ClubsListAction, ClubsListSideEffect>(
    ClubsListViewState()
) {
    private lateinit var inputData: ClubsListInputData

    fun init(inputData: ClubsListInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: ClubsListAction) {
        when (action) {
            ClubsListAction.FetchData -> fetchData()
            is ClubsListAction.clubItemTapped -> handleSelectedItem(action.id)
            is ClubsListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
            is ClubsListAction.FilterSelected -> handleFilterSelected(action.items)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getClubs()
            getFilters()
        }
    }

    private suspend fun getClubs() {
        postEffect(ClubsListSideEffect.Loading(true))
        try {
            val data = useCase.fetchClubsData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(clubs = data))
            )
        } catch (e: ApiException) {
            postEffect(ClubsListSideEffect.Error(e))
        } finally {
            postEffect(ClubsListSideEffect.Loading(false))
        }
    }

    private suspend fun getFilters() {
        try {
            val data = useCase.fetchFilterData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(filters = data))
            )
        } catch (e: ApiException) {
            postEffect(ClubsListSideEffect.Error(e))
        }
    }

    private fun handleSearchTextChanged(text: String) {
        updateState(
            state.copy(uiModel = state.uiModel.copy(searchText = text))
        )
    }

    private fun handleSelectedItem(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(ClubsScreens.Details.route)
        }
    }

    private fun handleFilterSelected(items: List<FilterView.Items>) {
        // Handle filter logic
    }

    private fun navigate(to: String) {
        viewModelScope.launch {
            navigator.navigateTo(to)
        }
    }
}