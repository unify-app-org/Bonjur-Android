package com.bonjur.hangouts.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.hangouts.domain.useCase.HangoutsUseCase
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsAction
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsSideEffect
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HangoutDetailsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<HangoutDetailsViewState, HangoutDetailsAction, HangoutDetailsSideEffect>(
    HangoutDetailsViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: HangoutsUseCase
    )

    private lateinit var inputData: HangoutDetailsInputData
    private lateinit var navigator: Navigator

    fun init(inputData: HangoutDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        fetchData()
    }

    override fun handle(action: HangoutDetailsAction) {
        when (action) {
            HangoutDetailsAction.FetchData -> fetchData()
            HangoutDetailsAction.BackTapped -> navigateBack()
            is HangoutDetailsAction.SegmentChanged -> {
                updateState(state.copy(selectedSegment = action.segment))
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
            val uiModel = dependencies.useCase.fetchDetailData(
                id = inputData.hangoutId
            )
            updateState(state.copy(uiModel = uiModel))
        } catch (e: Exception) {
            // Handle error
        }
    }
}

