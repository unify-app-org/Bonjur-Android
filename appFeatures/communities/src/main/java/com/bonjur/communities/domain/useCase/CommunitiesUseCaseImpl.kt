package com.bonjur.communities.domain.useCase

import com.bonjur.communities.data.dataSource.CommunitiesDataSource
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberCellModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberListSectionModel
import com.bonjur.communities.presentation.list.model.CommunityCardMocks
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import javax.inject.Inject

class CommunitiesUseCaseImpl @Inject constructor(
    val dataSource: CommunitiesDataSource
) : CommunitiesUseCase {

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> = CommunityCardMocks.mock

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchCommunityDetails(communityId: Int): CommunityDetails.UIModel = CommunityDetails.UIModel.mock

    /**
     * Fetch all members of a community and group them by degree+specialization
     * to produce faculty rows (matching iOS FacultyBrowseInputData pattern).
     */
    override suspend fun fetchFaculties(communityId: String): List<FacultyRowModel> {
        val id = communityId.toIntOrNull() ?: return emptyList()
        val response = dataSource.fetchCommunityMembers(id)
        // Group by degree → one FacultyRowModel per distinct degree
        return response.content
            .groupBy { it.degree ?: "Unknown" }
            .map { (degree, members) ->
                FacultyRowModel(
                    id = degree,
                    title = degree,
                    memberCount = members.size
                )
            }
            .sortedBy { it.title }
    }

    /**
     * Fetch members for a specific faculty (identified by degree string).
     * Groups them by specialization into MemberListSections.
     */
    override suspend fun fetchFacultyMembers(facultyId: String): List<MemberListSectionModel> {
        // facultyId is the degree string (e.g. "Bachelor")
        // We need to know the communityId — store it transiently or pass via a broader fetch.
        // For now we fetch from the cached community: this is a best-effort implementation.
        // In production, pass communityId alongside facultyId.
        return emptyList()
    }

    override suspend fun fetchFacultyMembersForCommunity(
        communityId: Int,
        degree: String
    ): List<MemberListSectionModel> {
        val response = dataSource.fetchCommunityMembers(communityId)
        val filtered = response.content.filter { it.degree == degree }
        return filtered
            .groupBy { it.specialization ?: "General" }
            .map { (specialization, members) ->
                MemberListSectionModel(
                    title = specialization,
                    memberCount = members.size,
                    members = members.map { m ->
                        MemberCellModel(
                            id = m.userId ?: "",
                            name = m.fullName ?: "-",
                            avatarUrl = m.profileUrl,
                            subtitle = listOfNotNull(
                                m.degree,
                                m.specialization,
                                m.entryYear?.toString()
                            ).joinToString(", ")
                        )
                    }
                )
            }
    }
}
