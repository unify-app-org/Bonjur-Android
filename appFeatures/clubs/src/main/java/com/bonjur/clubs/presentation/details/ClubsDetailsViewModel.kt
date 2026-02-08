package com.bonjur.clubs.presentation

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.presentation.model.*
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubDetailsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ClubDetailsViewState, ClubDetailsAction, ClubDetailsSideEffect>(
    ClubDetailsViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ClubsUseCase
    )

    private lateinit var inputData: ClubDetailsInputData
    private lateinit var navigator: Navigator
    fun init(inputData: ClubDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        fetchData()
    }

    override fun handle(action: ClubDetailsAction) {
        when (action) {
            ClubDetailsAction.FetchData -> fetchData()
            ClubDetailsAction.BackTapped -> navigateBack()
            is ClubDetailsAction.SegmentChanged -> {
                updateState(
                    state.copy(selectedSegment = action.segment)
                )
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchUIModel()
        }
    }

    private suspend fun fetchUIModel() {
        try {
            val uiModel = dependencies.useCase.fetchClubsDetails(
                clubId = inputData.clubId
            )
            updateState(state.copy(uiModel = uiModel))
        } catch (e: Exception) {
            // Handle error
        }
    }
}