package com.bonjur.communities.presentation.facultySelection.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel

// MARK: - FacultySelection input
data class FacultySelectionInputData(
    val communityId: String = "",
    val title: String = "Select Faculty"
)

// MARK: - Side effects
sealed class FacultySelectionSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : FacultySelectionSideEffect()
}

// MARK: - View State
data class FacultySelectionViewState(
    val title: String = "Select Faculty",
    val faculties: List<FacultyRowModel> = emptyList(),
    val selectedFacultyId: String? = null,
    val isLoading: Boolean = false
) : FeatureState

// MARK: - Feature Action
sealed class FacultySelectionAction : FeatureAction {
    object OnAppear : FacultySelectionAction()
    object BackTapped : FacultySelectionAction()
    data class FacultySelected(val faculty: FacultyRowModel) : FacultySelectionAction()
    object DoneTapped : FacultySelectionAction()
}
