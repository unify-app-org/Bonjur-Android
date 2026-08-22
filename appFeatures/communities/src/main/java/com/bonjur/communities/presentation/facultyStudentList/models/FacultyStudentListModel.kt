package com.bonjur.communities.presentation.facultyStudentList.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R as DesignR
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MemberListSectionModel

// MARK: - FacultyStudentList input
data class FacultyStudentListInputData(
    val communityId: String = "",
    val facultyId: String = "",   // degree string used to filter
    val title: String = LanguageManager.string(DesignR.string.common_members)
)

// MARK: - Side effects
sealed class FacultyStudentListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : FacultyStudentListSideEffect()
}

// MARK: - View State
data class FacultyStudentListViewState(
    val title: String = LanguageManager.string(DesignR.string.common_members),
    val sections: List<MemberListSectionModel> = emptyList(),
    val isLoading: Boolean = false
) : FeatureState

// MARK: - Feature Action
sealed class FacultyStudentListAction : FeatureAction {
    object OnAppear : FacultyStudentListAction()
    object BackTapped : FacultyStudentListAction()
    data class MemberTapped(val member: MemberCellModel) : FacultyStudentListAction()
}
