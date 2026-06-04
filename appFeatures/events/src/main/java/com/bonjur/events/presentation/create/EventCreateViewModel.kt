package com.bonjur.events.presentation.create

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.presentation.create.models.EventCreateAction
import com.bonjur.events.presentation.create.models.EventCreateInputData
import com.bonjur.events.presentation.create.models.EventCreateSideEffect
import com.bonjur.events.presentation.create.models.EventCreateViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<EventCreateViewState, EventCreateAction, EventCreateSideEffect>(
    EventCreateViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: EventsUseCase
    )

    private lateinit var inputData: EventCreateInputData
    private lateinit var navigator: Navigator

    fun init(inputData: EventCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.eventId != null) {
            updateState(state.copy(isEdit = true))
        }
        fetchData()
    }

    override fun handle(action: EventCreateAction) {
        when (action) {
            EventCreateAction.FetchData -> fetchData()
            EventCreateAction.BackTapped -> navigateBack()
            EventCreateAction.ContinueTapped -> continueTapped()
            is EventCreateAction.NameChanged -> updateState(state.copy(name = action.value))
            is EventCreateAction.AboutChanged -> updateState(state.copy(about = action.value))
            is EventCreateAction.LocationChanged -> updateState(state.copy(location = action.value))
            is EventCreateAction.OwnerContactChanged -> updateState(state.copy(ownerContact = action.value))
            is EventCreateAction.CapacityChanged -> updateState(state.copy(capacity = action.value))
            is EventCreateAction.RulesChanged -> updateState(state.copy(rules = action.value))
            is EventCreateAction.VisibilityChanged -> updateState(state.copy(isPublic = action.isPublic))
            is EventCreateAction.EventDateChanged -> updateState(state.copy(eventDate = action.value))
        }
    }

    private fun fetchData() {
        // Prefill data if editing
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun continueTapped() {
        viewModelScope.launch {
            if (inputData.eventId != null) {
                editEvent(inputData.eventId)
            } else {
                createEvent()
            }
        }
    }

    private suspend fun createEvent() {
        postEffect(EventCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.createEvent(
                name = state.name,
                about = state.about,
                location = state.location,
                ownerContact = state.ownerContact,
                capacity = state.capacity.toIntOrNull(),
                rules = state.rules,
                isPublic = state.isPublic,
                eventDate = state.eventDate
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(EventCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(EventCreateSideEffect.Loading(false))
        }
    }

    private suspend fun editEvent(eventId: String?) {
        postEffect(EventCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.editEvent(
                eventId = eventId ?: "",
                name = state.name,
                about = state.about,
                location = state.location,
                ownerContact = state.ownerContact,
                capacity = state.capacity.toIntOrNull(),
                rules = state.rules,
                isPublic = state.isPublic,
                eventDate = state.eventDate
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(EventCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(EventCreateSideEffect.Loading(false))
        }
    }
}
