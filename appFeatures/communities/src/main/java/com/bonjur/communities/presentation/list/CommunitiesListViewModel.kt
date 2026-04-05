package com.bonjur.communities.presentation.list

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.navigation.CommunitiesScreens
import com.bonjur.communities.presentation.list.model.CommunitiesListAction
import com.bonjur.communities.presentation.list.model.CommunitiesListInputData
import com.bonjur.communities.presentation.list.model.CommunitiesListSideEffect
import com.bonjur.communities.presentation.list.model.CommunitiesListViewState
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunitiesListViewModel @Inject constructor(
    private val useCase: CommunitiesUseCase
) : FeatureViewModel<CommunitiesListViewState, CommunitiesListAction, CommunitiesListSideEffect>(
    CommunitiesListViewState()
) {
    private lateinit var inputData: CommunitiesListInputData
    private lateinit var navigator: Navigator

    fun init(inputData: CommunitiesListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: CommunitiesListAction) {
        when (action) {
            CommunitiesListAction.FetchData -> fetchData()
            is CommunitiesListAction.CommunityItemTapped -> handleSelectedItem(action.id)
            is CommunitiesListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
            is CommunitiesListAction.FilterSelected -> handleFilterSelected(action.items)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getCommunities()
            getFilters()
        }
    }

    private suspend fun getCommunities() {
        postEffect(CommunitiesListSideEffect.Loading(true))
        try {
            val data = useCase.fetchCommunitiesData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(communities = data))
            )
        } catch (e: ApiException) {
            postEffect(CommunitiesListSideEffect.Error(e))
        } finally {
            postEffect(CommunitiesListSideEffect.Loading(false))
        }
    }

    private suspend fun getFilters() {
        try {
            val data = useCase.fetchFilterData()
            updateState(
                state.copy(uiModel = state.uiModel.copy(filters = data))
            )
        } catch (e: ApiException) {
            postEffect(CommunitiesListSideEffect.Error(e))
        }
    }

    private fun handleSearchTextChanged(text: String) {
        updateState(
            state.copy(uiModel = state.uiModel.copy(searchText = text))
        )
    }

    private fun handleSelectedItem(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(CommunitiesScreens.Details.route)
        }
    }

    private fun handleFilterSelected(items: List<FilterView.Items>) {
        // Handle filter logic
    }
}
