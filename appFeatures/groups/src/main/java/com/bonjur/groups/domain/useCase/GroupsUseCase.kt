package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.models.GroupsPaginationQuery
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.network.model.Page

interface GroupsUseCase {

    suspend fun fetchClubs(query: GroupsPaginationQuery): Page<ClubCardModel>

    suspend fun fetchEvents(query: GroupsPaginationQuery): Page<EventsCardModel>

    suspend fun fetchHangouts(query: GroupsPaginationQuery): Page<HangoutsCardModel>
}
