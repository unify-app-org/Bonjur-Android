//
//  ClubsListViewModel.kt
//  Clubs
//
//  Created by Huseyn Hasanov on 17.01.26
//

package com.bonjur.clubs.presentation.list

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.list.models.ClubCardModel
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
    private val useCase: ClubsUseCase
) : FeatureViewModel<ClubsListViewState, ClubsListAction, ClubsListSideEffect>(
    ClubsListViewState()
) {
    private lateinit var inputData: ClubsListInputData
    private lateinit var navigator: Navigator

    // Pagination — mirrors iOS ClubsViewModel (grow size, refetch page 0, replace).
    private val paginationStep = 10
    private var clubsSize = paginationStep
    private var isLoadingMoreClubs = false
    private var hasMoreClubs = true
    private var sourceClubs: List<ClubCardModel> = emptyList()
    private var selectedCategoryIds: List<Int> = emptyList()

    fun init(inputData: ClubsListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
    }

    override fun handle(action: ClubsListAction) {
        when (action) {
            ClubsListAction.FetchData -> fetchData()
            ClubsListAction.LoadMore -> loadMoreClubs()
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
            val data = useCase.fetchClubsData(
                size = clubsSize,
                name = state.uiModel.searchText,
                categoryIds = selectedCategoryIds
            )
            hasMoreClubs = data.size >= clubsSize
            sourceClubs = data
            applySearch()
        } catch (e: ApiException) {
            postEffect(ClubsListSideEffect.Error(e))
        } finally {
            postEffect(ClubsListSideEffect.Loading(false))
        }
    }

    private fun loadMoreClubs() {
        if (isLoadingMoreClubs || !hasMoreClubs) return
        isLoadingMoreClubs = true
        val previousSize = clubsSize
        val previousCount = sourceClubs.size
        clubsSize += paginationStep

        viewModelScope.launch {
            try {
                val data = useCase.fetchClubsData(
                    size = clubsSize,
                    name = state.uiModel.searchText,
                    categoryIds = selectedCategoryIds
                )
                hasMoreClubs = data.size > previousCount
                sourceClubs = data
                applySearch()
            } catch (e: ApiException) {
                clubsSize = previousSize
                postEffect(ClubsListSideEffect.Error(e))
            } finally {
                isLoadingMoreClubs = false
            }
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
        applySearch()
    }

    /** Client-side filter over the already-fetched source list. Mirrors iOS `applySearch`. */
    private fun applySearch() {
        val query = state.uiModel.searchText.trim().lowercase()
        val filtered = if (query.isEmpty()) {
            sourceClubs
        } else {
            sourceClubs.filter { club ->
                club.name.lowercase().contains(query) ||
                    club.communityName.lowercase().contains(query) ||
                    club.community.lowercase().contains(query)
            }
        }
        updateState(state.copy(uiModel = state.uiModel.copy(clubs = filtered)))
    }

    /** Filter selection -> categoryIds -> reset paging -> refetch. Mirrors Discover. */
    private fun handleFilterSelected(items: List<FilterView.Items>) {
        selectedCategoryIds = items.map { it.id }
        clubsSize = paginationStep
        hasMoreClubs = true
        viewModelScope.launch {
            getClubs()
        }
    }

    private fun handleSelectedItem(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(
                ClubsScreens.Details.route,
                com.bonjur.clubs.presentation.model.ClubDetailsInputData(clubId = id)
            )
        }
    }
}
