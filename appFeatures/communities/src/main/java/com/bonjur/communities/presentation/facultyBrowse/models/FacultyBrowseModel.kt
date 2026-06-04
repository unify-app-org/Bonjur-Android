package com.bonjur.communities.presentation.facultyBrowse.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

data class FacultyRowModel(
    val id: String,
    val title: String,
    val memberCount: Int = 0
)

data class MemberCellModel(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val subtitle: String = ""
)

data class MemberListSectionModel(
    val title: String,
    val memberCount: Int,
    val members: List<MemberCellModel>
)

// MARK: - FacultyBrowse input
data class FacultyBrowseInputData(
    val title: String = "All members",
    val sectionTitle: String = "Faculty",
    val communityId: String = ""
)

// MARK: - Side effects
sealed class FacultyBrowseSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : FacultyBrowseSideEffect()
}

// MARK: - View State
data class FacultyBrowseViewState(
    val title: String = "All members",
    val sectionTitle: String = "Faculty",
    val faculties: List<FacultyRowModel> = emptyList(),
    val isLoading: Boolean = false
) : FeatureState

// MARK: - Feature Action
sealed class FacultyBrowseAction : FeatureAction {
    object OnAppear : FacultyBrowseAction()
    data class FacultyTapped(val faculty: FacultyRowModel) : FacultyBrowseAction()
}
