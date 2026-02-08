package com.bonjur.events.presentation.details

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.presentation.details.model.EventDetailsAction
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.details.model.EventDetailsSideEffect
import com.bonjur.events.presentation.details.model.EventDetailsViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<EventDetailsViewState, EventDetailsAction, EventDetailsSideEffect>(
    EventDetailsViewState()
) {
    
    data class Dependencies @Inject constructor(
        val useCase: EventsUseCase
    )
    
    private lateinit var inputData: EventDetailsInputData
    private lateinit var navigator: Navigator

    fun init(inputData: EventDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        fetchData()
    }

    override fun handle(action: EventDetailsAction) {
        when (action) {
            EventDetailsAction.FetchData -> fetchData()
            EventDetailsAction.BackTapped -> navigateBack()
            is EventDetailsAction.SegmentChanged -> {
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
            getDetails()
        }
    }

    private suspend fun getDetails() {
        try {
            val data = dependencies.useCase.fetchDetailsData(
                id = inputData.eventId
            )
            updateState(
                state.copy(uiModel = data)
            )
        } catch (e: Exception) {
            // Handle error
        }
    }
}