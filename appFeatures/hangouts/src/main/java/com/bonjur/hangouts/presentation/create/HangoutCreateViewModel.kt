package com.bonjur.hangouts.presentation.create

import android.R.attr.name
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.hangouts.domain.useCase.HangoutsUseCase
import com.bonjur.hangouts.presentation.create.models.HangoutCreateAction
import com.bonjur.hangouts.presentation.create.models.HangoutCreateInputData
import com.bonjur.hangouts.presentation.create.models.HangoutCreateSideEffect
import com.bonjur.hangouts.presentation.create.models.HangoutCreateViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier.isPublic
import javax.inject.Inject

@HiltViewModel
class HangoutCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<HangoutCreateViewState, HangoutCreateAction, HangoutCreateSideEffect>(
    HangoutCreateViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: HangoutsUseCase
    )

    private lateinit var inputData: HangoutCreateInputData
    private lateinit var navigator: Navigator

    fun init(inputData: HangoutCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.hangoutId != null) {
            updateState(state.copy(isEdit = true))
        }
        fetchData()
    }

    override fun handle(action: HangoutCreateAction) {
        when (action) {
            HangoutCreateAction.FetchData -> fetchData()
            HangoutCreateAction.BackTapped -> navigateBack()
            HangoutCreateAction.ContinueTapped -> continueTapped()
            is HangoutCreateAction.NameChanged -> updateState(state.copy(name = action.value))
            is HangoutCreateAction.AboutChanged -> updateState(state.copy(about = action.value))
            is HangoutCreateAction.LocationChanged -> updateState(state.copy(location = action.value))
            is HangoutCreateAction.OwnerContactChanged -> updateState(state.copy(ownerContact = action.value))
            is HangoutCreateAction.CapacityChanged -> updateState(state.copy(capacity = action.value))
            is HangoutCreateAction.RulesChanged -> updateState(state.copy(rules = action.value))
            is HangoutCreateAction.VisibilityChanged -> updateState(state.copy(isPublic = action.isPublic))
            is HangoutCreateAction.HangoutDateChanged -> updateState(state.copy(hangoutDate = action.value))
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
            if (inputData.hangoutId != null) {
                editHangout(inputData.hangoutId)
            } else {
                createHangout()
            }
        }
    }

    private suspend fun createHangout() {
        postEffect(HangoutCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.createHangout(
                name = state.name,
                about = state.about,
                location = state.location,
                ownerContact = state.ownerContact,
                capacity = state.capacity.toIntOrNull(),
                rules = state.rules,
                isPublic = state.isPublic,
                hangoutDate = state.hangoutDate
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(HangoutCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(HangoutCreateSideEffect.Loading(false))
        }
    }

    private suspend fun editHangout(hangoutId: String?) {
        postEffect(HangoutCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.editHangout(
                hangoutId = hangoutId ?: "",
                name = state.name,
                about = state.about,
                location = state.location,
                ownerContact = state.ownerContact,
                capacity = state.capacity.toIntOrNull(),
                rules = state.rules,
                isPublic = state.isPublic,
                hangoutDate = state.hangoutDate
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(HangoutCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(HangoutCreateSideEffect.Loading(false))
        }
    }
}
