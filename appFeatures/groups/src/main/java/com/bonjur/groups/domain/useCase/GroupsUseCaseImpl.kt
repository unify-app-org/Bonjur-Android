package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.commonModel.toUserActivityRole
import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.DTOs.GroupsClubResponse
import com.bonjur.groups.data.DTOs.GroupsHangoutResponse
import com.bonjur.groups.data.dataSource.GroupsDataSource
import com.bonjur.groups.data.models.GroupsPaginationQuery
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import javax.inject.Inject

class GroupsUseCaseImpl @Inject constructor(
    val dataSource: GroupsDataSource
) : GroupsUseCase {

    override suspend fun fetchClubs(query: GroupsPaginationQuery): List<ClubCardModel> {
        return dataSource.fetchJoinedClubs(query.toMap()).map { it.toCardModel() }
    }

    // Mirrors iOS: joined events are fetched once at a fixed page/size (no pagination),
    // with the optional search keyword forwarded to the server.
    override suspend fun fetchEvents(keyword: String?): List<EventsCardModel> {
        val query = GroupsPaginationQuery(page = 0, size = 50, keyword = keyword)
        return dataSource.fetchJoinedEvents(query.toMap()).map { it.toCardModel() }
    }

    override suspend fun fetchHangouts(query: GroupsPaginationQuery): List<HangoutsCardModel> {
        return dataSource.fetchJoinedHangouts(query.toMap()).map { it.toCardModel() }
    }

    private fun GroupsClubResponse.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "",
        communityName = communityName ?: "",
        logoURL = clubProfile ?: "",
        memberCount = memberCount ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "",
        members = members.map {
            AppUIEntities.Member(id = it.id?.hashCode() ?: 0, profileImage = it.url)
        },
        bgType = background.toBackgroundType(),
        accessType = visibility.toAccessType(),
        requestType = requestStatus.toRequestType(),
        role = role?.toUserActivityRole(),
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title },
        isVerified = AppUIEntities.ClubStatus.from(clubStatus)?.isVerified == true
    )

    private fun EventListResponse.toCardModel() = EventsCardModel(
        id = id ?: "",
        name = name ?: "",
        coverImageURL = background,
        memberCount = membersCount ?: 0,
        totalCapacity = capacity,
        club = EventsCardModel.Club(
            name = club?.name ?: "",
            id = club?.id ?: 0
        ),
        tags = categoryResponses.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        bgType = AppUIEntities.BackgroundType.Primary,
        requestType = requestStatus.toRequestType(),
        accessType = visibility.toAccessType()
    )

    private fun GroupsHangoutResponse.toCardModel() = HangoutsCardModel(
        id = id ?: "",
        name = name ?: "",
        description = about ?: "",
        memberCount = membersCount ?: 0,
        totalCapacity = capacity,
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        accessType = visibility.toAccessType(),
        requestType = status.toRequestType()
    )

    private fun String?.toAccessType(): AppUIEntities.AccessType =
        if (this == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE

    private fun String?.toRequestType(): AppUIEntities.RequestType =
        when (this?.uppercase()) {
            "ACCEPTED", "JOINED" -> AppUIEntities.RequestType.JOINED
            "PENDING" -> AppUIEntities.RequestType.PENDING
            "REJECTED" -> AppUIEntities.RequestType.REJECTED
            else -> AppUIEntities.RequestType.NONE
        }

    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType =
        when (this?.uppercase()) {
            "BLUE" -> AppUIEntities.BackgroundType.Secondary
            "PURPLE" -> AppUIEntities.BackgroundType.Tertiary
            else -> AppUIEntities.BackgroundType.Primary
        }
}
