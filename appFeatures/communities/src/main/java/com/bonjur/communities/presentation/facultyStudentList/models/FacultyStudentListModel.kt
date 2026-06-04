package com.bonjur.communities.presentation.facultyStudentList.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.communities.presentation.facultyBrowse.models.MemberCellModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberListSectionModel

// MARK: - FacultyStudentList input
data class FacultyStudentListInputData(
    val communityId: String = "",
    val facultyId: String = "",   // degree string used to filter
    val title: String = "Members"
)

// MARK: - Side effects
sealed class FacultyStudentListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : FacultyStudentListSideEffect()
}

// MARK: - View State
data class FacultyStudentListViewState(
    val title: String = "Members",
    val sections: List<MemberListSectionModel> = emptyList(),
    val isLoading: Boolean = false
) : FeatureState

// MARK: - Feature Action
sealed class FacultyStudentListAction : FeatureAction {
    object OnAppear : FacultyStudentListAction()
    object BackTapped : FacultyStudentListAction()
    data class MemberTapped(val member: MemberCellModel) : FacultyStudentListAction()
}
