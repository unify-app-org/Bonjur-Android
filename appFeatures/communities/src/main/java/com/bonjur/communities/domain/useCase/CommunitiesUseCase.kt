package com.bonjur.communities.domain.useCase

import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberListSectionModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.components.filter.FilterView

interface CommunitiesUseCase {
    suspend fun fetchCommunitiesData(): List<CommunityCardModel>
    suspend fun fetchCommunityDetails(communityId: Int): CommunityDetails.UIModel
    suspend fun fetchFilterData(): List<FilterView.Model>

    // Faculty screens
    suspend fun fetchFaculties(communityId: String): List<FacultyRowModel>
    suspend fun fetchFacultyMembers(facultyId: String): List<MemberListSectionModel>
    suspend fun fetchFacultyMembersForCommunity(communityId: Int, degree: String): List<MemberListSectionModel>
}
