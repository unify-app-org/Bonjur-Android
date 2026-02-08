package com.bonjur.events.domain.useCase

import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.events.presentation.list.models.EventsCardModel

interface EventsUseCase {
    suspend fun fetchEventsData(): List<EventsCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchDetailsData(id: String): EventsDetails.UIModel
}

