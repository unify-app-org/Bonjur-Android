package com.bonjur.events.domain.useCase

import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.events.presentation.models.EventsCardModel

interface EventsUseCase {
    suspend fun fetchEventsData(): List<EventsCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>
}

