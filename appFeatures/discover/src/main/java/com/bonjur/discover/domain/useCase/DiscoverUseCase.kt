package com.bonjur.discover.domain.useCase

import com.bonjur.clubs.presentation.models.ClubCardModel
import com.bonjur.communities.CommunityCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.discover.domain.models.UserModel
import com.bonjur.events.EventsCardModel
import com.bonjur.hangouts.HangoutsCardModel

interface DiscoverUseCase {
    suspend fun fetchUserData(): UserModel

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchCommunitiesData(): List<CommunityCardModel>

    suspend fun fetchClubsData(): List<ClubCardModel>

    suspend fun fetchEvents(): List<EventsCardModel>

    suspend fun fetchHangoutsData(): List<HangoutsCardModel>

}