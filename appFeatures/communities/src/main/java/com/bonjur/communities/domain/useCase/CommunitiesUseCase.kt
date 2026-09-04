package com.bonjur.communities.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.member.model.MemberListSectionModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MembersPage
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.network.model.Page

interface CommunitiesUseCase {
    suspend fun fetchCommunitiesData(): List<CommunityCardModel>
    suspend fun fetchCommunityDetails(communityId: Int): CommunityDetails.UIModel

    /**
     * One page of a community's sub-clubs. Mirrors iOS `fetchClubs`.
     *
     * `api/ds/v1/clubs` answers with a bare array — discover-service has no page
     * envelope — so `hasMore` can only be "this page came back full".
     */
    suspend fun fetchClubs(communityId: Int, page: Int, size: Int): Page<ClubCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>

    /** Members grouped into role sections (preview). Mirrors iOS `fetchCommunityMembers`. */
    suspend fun fetchCommunityMembers(communityId: Int): GroupedMembersData

    /** One page of members for the paginated list. Mirrors iOS `fetchCommunityMembersPage`. */
    suspend fun fetchCommunityMembersPage(communityId: Int, page: Int, size: Int, keyword: String?): MembersPage

    /** Assign a role to a member. Mirrors iOS `assignRole`. */
    suspend fun assignRole(
        communityId: Int,
        userId: String,
        role: AppUIEntities.UserActivityRole
    )

    // Faculty screens
    suspend fun fetchFaculties(communityId: String): List<FacultyRowModel>
    suspend fun fetchFacultyMembersForCommunity(communityId: Int, degree: String): List<MemberListSectionModel>
}
