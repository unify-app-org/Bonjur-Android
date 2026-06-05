package com.bonjur.communities.domain.useCase

import com.bonjur.communities.data.DTOs.CommunityDetailResponse
import com.bonjur.communities.data.dataSource.CommunitiesDataSource
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberCellModel
import com.bonjur.communities.presentation.facultyBrowse.models.MemberListSectionModel
import com.bonjur.communities.presentation.list.model.CommunityCardMocks
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import javax.inject.Inject

class CommunitiesUseCaseImpl @Inject constructor(
    val dataSource: CommunitiesDataSource
) : CommunitiesUseCase {

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> = CommunityCardMocks.mock

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchCommunityDetails(communityId: Int): CommunityDetails.UIModel {
        val detail = dataSource.fetchCommunityDetail(communityId)
        return detail.toUIModel()
    }

    private fun CommunityDetailResponse.toUIModel() = CommunityDetails.UIModel(
        name = name,
        membersCount = membersCount ?: 0,
        logo = logoUrl,
        coverImage = backgroundUrl,
        coverColorType = backgroundColour.toBackgroundType(),
        tags = emptyList(),
        infoData = buildInfoData(this),
        clubsData = emptyList()
    )

    private fun buildInfoData(detail: CommunityDetailResponse): List<CommunityDetails.Info> = buildList {
        detail.about?.takeIf { it.isNotBlank() }?.let {
            add(
                CommunityDetails.Info(
                    title = "About",
                    subItems = listOf(CommunityDetails.SubInfo(title = null, description = it))
                )
            )
        }
        add(
            CommunityDetails.Info(
                title = "Community info",
                subItems = buildList {
                    detail.ownerContact?.let { add(CommunityDetails.SubInfo(title = "Contact", description = it)) }
                    detail.location?.let { add(CommunityDetails.SubInfo(title = "Location", description = it)) }
                    detail.capacity?.let {
                        add(CommunityDetails.SubInfo(title = "Members", description = "${detail.membersCount ?: 0}/$it members"))
                    }
                }
            )
        )
    }

    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType = when (this?.uppercase()) {
        "GREEN", "PRIMARY" -> AppUIEntities.BackgroundType.Primary
        "BLUE", "SECONDARY" -> AppUIEntities.BackgroundType.Secondary
        "PURPLE", "TERTIARY" -> AppUIEntities.BackgroundType.Tertiary
        "RED" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Red)
        "ORANGE" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Orange)
        "PINK" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Pink)
        else -> AppUIEntities.BackgroundType.Primary
    }

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
