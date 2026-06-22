package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.models.GroupsPaginationQuery
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel

interface GroupsUseCase {

    suspend fun fetchClubs(query: GroupsPaginationQuery): List<ClubCardModel>

    suspend fun fetchEvents(): List<EventsCardModel>

    suspend fun fetchHangouts(query: GroupsPaginationQuery): List<HangoutsCardModel>
}
