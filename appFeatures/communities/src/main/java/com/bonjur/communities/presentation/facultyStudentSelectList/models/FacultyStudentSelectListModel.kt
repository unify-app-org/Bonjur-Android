package com.bonjur.communities.presentation.facultyStudentSelectList.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.communities.presentation.facultyBrowse.models.MemberCellModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberListSectionModel

// MARK: - FacultyStudentSelectList input
data class FacultyStudentSelectListInputData(
    val communityId: String = "",
    val facultyId: String = "",
    val title: String = "Select Member"
)

// MARK: - Side effects
sealed class FacultyStudentSelectListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : FacultyStudentSelectListSideEffect()
}

// MARK: - View State
data class FacultyStudentSelectListViewState(
    val title: String = "Select Member",
    val sections: List<MemberListSectionModel> = emptyList(),
    val selectedMemberIds: Set<String> = emptySet(),
    val isLoading: Boolean = false
) : FeatureState

// MARK: - Feature Action
sealed class FacultyStudentSelectListAction : FeatureAction {
    object OnAppear : FacultyStudentSelectListAction()
    object BackTapped : FacultyStudentSelectListAction()
    object DoneTapped : FacultyStudentSelectListAction()
    data class MemberToggled(val member: MemberCellModel) : FacultyStudentSelectListAction()
}
