package com.bonjur.communities.presentation.facultySelection

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionAction
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionInputData
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionSideEffect
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultySelectionViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<FacultySelectionViewState, FacultySelectionAction, FacultySelectionSideEffect>(
    FacultySelectionViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase
    )

    private lateinit var inputData: FacultySelectionInputData
    private lateinit var navigator: Navigator

    fun init(inputData: FacultySelectionInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(title = inputData.title))
        handle(FacultySelectionAction.OnAppear)
    }

    override fun handle(action: FacultySelectionAction) {
        when (action) {
            FacultySelectionAction.OnAppear -> fetchData()
            FacultySelectionAction.BackTapped -> navigateBack()
            FacultySelectionAction.DoneTapped -> navigateBack()
            is FacultySelectionAction.FacultySelected ->
                updateState(state.copy(selectedFacultyId = action.faculty.id))
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postEffect(FacultySelectionSideEffect.Loading(true))
            try {
                val faculties = dependencies.useCase.fetchFaculties(inputData.communityId)
                updateState(state.copy(faculties = faculties))
            } catch (e: Exception) {
                // Keep empty state
            } finally {
                postEffect(FacultySelectionSideEffect.Loading(false))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }
}
