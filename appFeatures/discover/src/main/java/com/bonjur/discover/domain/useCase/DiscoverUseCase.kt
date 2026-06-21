package com.bonjur.discover.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.discover.domain.models.UserModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel

interface DiscoverUseCase {
    suspend fun fetchUserData(): UserModel

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchCommunitiesData(
        page: Int = 0,
        size: Int = 10,
        categoryIds: List<Int> = emptyList()
    ): List<CommunityCardModel>

    suspend fun fetchClubsData(
        page: Int = 0,
        size: Int = 10,
        categoryIds: List<Int> = emptyList()
    ): List<ClubCardModel>

    suspend fun fetchEvents(
        page: Int = 0,
        size: Int = 10,
        categoryIds: List<Int> = emptyList()
    ): List<EventsCardModel>

    suspend fun fetchHangoutsData(
        page: Int = 0,
        size: Int = 10,
        categoryIds: List<Int> = emptyList()
    ): List<HangoutsCardModel>

    suspend fun joinHangout(hangoutId: String)
}
