package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.CommunityCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.events.presentation.models.EventsCardModel
import com.bonjur.hangouts.presentation.model.HangoutsCardModel

interface GroupsUseCase {

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchCommunitiesData(): List<CommunityCardModel>

    suspend fun fetchClubsData(): List<ClubCardModel>

    suspend fun fetchEvents(): List<EventsCardModel>

    suspend fun fetchHangoutsData(): List<HangoutsCardModel>

}