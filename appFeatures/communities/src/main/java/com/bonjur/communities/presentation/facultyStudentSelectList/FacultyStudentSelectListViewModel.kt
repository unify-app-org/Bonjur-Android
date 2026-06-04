package com.bonjur.communities.presentation.facultyStudentSelectList

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListAction
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListInputData
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListSideEffect
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultyStudentSelectListViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<FacultyStudentSelectListViewState, FacultyStudentSelectListAction, FacultyStudentSelectListSideEffect>(
    FacultyStudentSelectListViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase
    )

    private lateinit var inputData: FacultyStudentSelectListInputData
    private lateinit var navigator: Navigator

    fun init(inputData: FacultyStudentSelectListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(title = inputData.title))
        handle(FacultyStudentSelectListAction.OnAppear)
    }

    override fun handle(action: FacultyStudentSelectListAction) {
        when (action) {
            FacultyStudentSelectListAction.OnAppear -> fetchData()
            FacultyStudentSelectListAction.BackTapped -> navigateBack()
            FacultyStudentSelectListAction.DoneTapped -> navigateBack()
            is FacultyStudentSelectListAction.MemberToggled -> {
                val updated = state.selectedMemberIds.toMutableSet()
                if (updated.contains(action.member.id)) {
                    updated.remove(action.member.id)
                } else {
                    updated.add(action.member.id)
                }
                updateState(state.copy(selectedMemberIds = updated))
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postEffect(FacultyStudentSelectListSideEffect.Loading(true))
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
                postEffect(FacultyStudentSelectListSideEffect.Loading(false))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }
}
