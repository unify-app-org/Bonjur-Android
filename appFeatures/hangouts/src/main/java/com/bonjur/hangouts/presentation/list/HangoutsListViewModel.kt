//
//  HangoutsListViewModel.kt
//  Hangouts
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.hangouts.presentation.list

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.hangouts.domain.useCase.HangoutsUseCase
import com.bonjur.hangouts.navigation.HangoutsScreens
import com.bonjur.hangouts.presentation.list.model.HangoutsListAction
import com.bonjur.hangouts.presentation.list.model.HangoutsListInputData
import com.bonjur.hangouts.presentation.list.model.HangoutsListSideEffect
import com.bonjur.hangouts.presentation.list.model.HangoutsListViewState
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HangoutsListViewModel @Inject constructor(
    private val useCase: HangoutsUseCase
) : FeatureViewModel<HangoutsListViewState, HangoutsListAction, HangoutsListSideEffect>(
    HangoutsListViewState()
) {
    private lateinit var inputData: HangoutsListInputData
    private lateinit var navigator: Navigator

    fun init(inputData: HangoutsListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: HangoutsListAction) {
        when (action) {
            HangoutsListAction.FetchData -> fetchData()
            HangoutsListAction.Dismiss -> dismiss()
            is HangoutsListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
            is HangoutsListAction.FilterSelected -> handleFilterSelected(action.items)
            is HangoutsListAction.ItemSelected -> handleItemSelect(action.id)
        }
    }

    private fun handleItemSelect(id: String) {
        viewModelScope.launch {
            navigator.navigateTo(HangoutsScreens.Details.route)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getHangoutsData()
            getFilters()
        }
    }

    private suspend fun getHangoutsData() {
        postEffect(HangoutsListSideEffect.Loading(true))
        try {
            val data = useCase.fetchHangoutsData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(hangouts = data))
            )
        } catch (e: ApiException) {
            postEffect(HangoutsListSideEffect.Error(e))
        } finally {
            postEffect(HangoutsListSideEffect.Loading(false))
        }
    }

    private suspend fun getFilters() {
        try {
            val data = useCase.fetchFilterData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(filters = data))
            )
        } catch (e: ApiException) {
            postEffect(HangoutsListSideEffect.Error(e))
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

    private fun dismiss() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}