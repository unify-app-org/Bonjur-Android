package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.dataSource.GroupsDataSource
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import javax.inject.Inject

class GroupsUseCaseImpl @Inject constructor(
    val dataSource: GroupsDataSource
) : GroupsUseCase {

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> = emptyList()

    override suspend fun fetchClubsData(): List<ClubCardModel> {
        return dataSource.fetchJoinedClubs().map { it.toCardModel() }
    }

    override suspend fun fetchEvents(): List<EventsCardModel> = emptyList()

    override suspend fun fetchHangoutsData(): List<HangoutsCardModel> {
        return dataSource.fetchJoinedHangouts().map { it.toCardModel() }
    }

    private fun ClubListResponse.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "",
        communityName = communityName ?: "",
        logoURL = clubProfile ?: "",
        memberCount = count ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "",
        members = emptyList(),
        bgType = when (background?.uppercase()) {
            "SECONDARY" -> AppUIEntities.BackgroundType.Secondary
            else -> AppUIEntities.BackgroundType.Primary
        },
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        requestType = if (joined == true) AppUIEntities.RequestType.JOINED else AppUIEntities.RequestType.NONE
    )

    private fun HangoutListResponse.toCardModel() = HangoutsCardModel(
        id = id ?: "",
        name = name ?: "",
        description = about ?: "",
        memberCount = membersCount ?: 0,
        totalCapacity = capacity,
        tags = categoryResponses.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        requestType = when (requestStatus?.uppercase()) {
            "ACCEPTED", "JOINED" -> AppUIEntities.RequestType.JOINED
            "PENDING" -> AppUIEntities.RequestType.PENDING
            "REJECTED" -> AppUIEntities.RequestType.REJECTED
            "JOINED" -> AppUIEntities.RequestType.JOINED
            else -> AppUIEntities.RequestType.NONE
        }
    )
}
