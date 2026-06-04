package com.bonjur.communities.presentation.facultyStudentList

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListAction
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListInputData
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListSideEffect
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultyStudentListViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<FacultyStudentListViewState, FacultyStudentListAction, FacultyStudentListSideEffect>(
    FacultyStudentListViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase
    )

    private lateinit var inputData: FacultyStudentListInputData
    private lateinit var navigator: Navigator

    fun init(inputData: FacultyStudentListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(title = inputData.title))
        handle(FacultyStudentListAction.OnAppear)
    }

    override fun handle(action: FacultyStudentListAction) {
        when (action) {
            FacultyStudentListAction.OnAppear -> fetchData()
            FacultyStudentListAction.BackTapped -> navigateBack()
            is FacultyStudentListAction.MemberTapped -> { /* Navigate to member profile */ }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postEffect(FacultyStudentListSideEffect.Loading(true))
            try {
                val communityId = inputData.communityId.toIntOrNull() ?: return@launch
                val sections = dependencies.useCase.fetchFacultyMembersForCommunity(
                    communityId = communityId,
                    degree = inputData.facultyId
                )
                updateState(state.copy(sections = sections))
            } catch (e: Exception) {
                // Keep empty state
            } finally {
                postEffect(FacultyStudentListSideEffect.Loading(false))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }
}
