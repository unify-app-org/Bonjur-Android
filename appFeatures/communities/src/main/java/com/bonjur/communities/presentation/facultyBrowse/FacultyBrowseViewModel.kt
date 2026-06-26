package com.bonjur.communities.presentation.facultyBrowse

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseAction
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseInputData
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseSideEffect
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseViewState
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultyBrowseViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<FacultyBrowseViewState, FacultyBrowseAction, FacultyBrowseSideEffect>(
    FacultyBrowseViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase
    )

    private lateinit var inputData: FacultyBrowseInputData
    private lateinit var navigator: Navigator

    fun init(inputData: FacultyBrowseInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(
            state.copy(
                title = inputData.title,
                sectionTitle = inputData.sectionTitle
            )
        )
        handle(FacultyBrowseAction.OnAppear)
    }

    override fun handle(action: FacultyBrowseAction) {
        when (action) {
            FacultyBrowseAction.OnAppear -> fetchData()
            is FacultyBrowseAction.FacultyTapped -> navigateToStudentList(action.faculty.id)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postEffect(FacultyBrowseSideEffect.Loading(true))
            try {
                val faculties = dependencies.useCase.fetchFaculties(inputData.communityId)
                updateState(state.copy(faculties = faculties))
            } catch (e: Exception) {
                // Keep empty state
            } finally {
                postEffect(FacultyBrowseSideEffect.Loading(false))
            }
        }
    }

    private fun navigateToStudentList(facultyId: String) {
        viewModelScope.launch {
            navigator.navigateTo(
                com.bonjur.communities.navigation.CommunitiesScreens.FacultyStudentList.route,
                com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListInputData(
                    communityId = inputData.communityId,
                    facultyId = facultyId,
                    title = state.title
                )
            )
        }
    }
}
