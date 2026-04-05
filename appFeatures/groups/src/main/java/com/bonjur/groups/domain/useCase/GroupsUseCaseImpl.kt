package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardMocks
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.presentation.list.model.CommunityCardMocks
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.events.presentation.list.models.EventsCardMocks
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.dataSource.GroupsDataSource
import com.bonjur.hangouts.presentation.list.model.HangoutsCardMocks
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import javax.inject.Inject

class GroupsUseCaseImpl @Inject constructor(
    val dataSource: GroupsDataSource
): GroupsUseCase {

    override suspend fun fetchFilterData(): List<FilterView.Model> {
        return FilterViewMocks.mockData
    }

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> {
        return CommunityCardMocks.mock
    }

    override suspend fun fetchClubsData(): List<ClubCardModel> {
        return ClubCardMocks.previewData
    }

    override suspend fun fetchEvents(): List<EventsCardModel> {
        return EventsCardMocks.previewMock
    }

    override suspend fun fetchHangoutsData(): List<HangoutsCardModel> {
        return HangoutsCardMocks.previewMock
    }
}