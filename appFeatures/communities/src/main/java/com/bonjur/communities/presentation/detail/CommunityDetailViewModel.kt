package com.bonjur.communities.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.presentation.detail.model.CommunityDetailAction
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.detail.model.CommunityDetailSideEffect
import com.bonjur.communities.presentation.detail.model.CommunityDetailViewState
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<CommunityDetailViewState, CommunityDetailAction, CommunityDetailSideEffect>(
    CommunityDetailViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase
    )

    private lateinit var inputData: CommunityDetailInputData
    private lateinit var navigator: Navigator

    fun init(inputData: CommunityDetailInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        fetchData()
    }

    override fun handle(action: CommunityDetailAction) {
        when (action) {
            CommunityDetailAction.FetchData -> fetchData()
            CommunityDetailAction.BackTapped -> navigateBack()
            is CommunityDetailAction.ClubItemTapped -> handleClubItemTapped(action.id)
            is CommunityDetailAction.SegmentChanged -> {
                updateState(state.copy(selectedSegment = action.segment))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun handleClubItemTapped(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(
                ClubsScreens.Details.route,
                ClubDetailsInputData(clubId = id)
            )
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchUIModel()
        }
    }

    private suspend fun fetchUIModel() {
        try {
            val uiModel = dependencies.useCase.fetchCommunityDetails(
                communityId = inputData.communityId
            )
            updateState(state.copy(uiModel = uiModel))
        } catch (e: Exception) {
            // Handle error
        }
    }
}
