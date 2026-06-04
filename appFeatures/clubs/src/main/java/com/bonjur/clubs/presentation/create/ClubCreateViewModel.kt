package com.bonjur.clubs.presentation.create

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.presentation.create.models.ClubCreateAction
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.clubs.presentation.create.models.ClubCreateViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ClubCreateViewState, ClubCreateAction, ClubCreateSideEffect>(
    ClubCreateViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ClubsUseCase
    )

    private lateinit var inputData: ClubCreateInputData
    private lateinit var navigator: Navigator

    fun init(inputData: ClubCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.clubId != null) {
            updateState(state.copy(isEdit = true))
        }
        fetchData()
    }

    override fun handle(action: ClubCreateAction) {
        when (action) {
            ClubCreateAction.FetchData -> fetchData()
            ClubCreateAction.BackTapped -> navigateBack()
            ClubCreateAction.ContinueTapped -> continueTapped()
            is ClubCreateAction.NameChanged -> updateState(state.copy(name = action.value))
            is ClubCreateAction.AboutChanged -> updateState(state.copy(about = action.value))
            is ClubCreateAction.LocationChanged -> updateState(state.copy(location = action.value))
            is ClubCreateAction.OwnerContactChanged -> updateState(state.copy(ownerContact = action.value))
            is ClubCreateAction.CapacityChanged -> updateState(state.copy(capacity = action.value))
            is ClubCreateAction.RulesChanged -> updateState(state.copy(rules = action.value))
            is ClubCreateAction.VisibilityChanged -> updateState(state.copy(isPublic = action.isPublic))
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
            val clubId = inputData.clubId
            if (clubId != null) {
                editClub(clubId)
            } else {
                createClub()
            }
        }
    }

    private suspend fun createClub() {
        postEffect(ClubCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.createClub(
                name = state.name,
                about = state.about,
                location = state.location,
                ownerContact = state.ownerContact,
                capacity = state.capacity.toIntOrNull(),
                rules = state.rules,
                isPublic = state.isPublic
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(ClubCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(ClubCreateSideEffect.Loading(false))
        }
    }

    private suspend fun editClub(clubId: Int) {
        postEffect(ClubCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.editClub(
                clubId = clubId,
                name = state.name,
                about = state.about,
                location = state.location,
                ownerContact = state.ownerContact,
                capacity = state.capacity.toIntOrNull(),
                rules = state.rules,
                isPublic = state.isPublic
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(ClubCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(ClubCreateSideEffect.Loading(false))
        }
    }
}
