package com.bonjur.events.domain.useCase

import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.events.presentation.list.models.EventsCardMocks
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.events.data.dataSource.EventsDataSource
import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.events.domain.models.EventsDetailsMockData
import javax.inject.Inject

class EventsUseCaseImpl @Inject constructor(
    val dataSource: EventsDataSource
): EventsUseCase {
    override suspend fun fetchEventsData(): List<EventsCardModel> {
        return EventsCardMocks.previewMock
    }

    override suspend fun fetchFilterData(): List<FilterView.Model> {
        return FilterViewMocks.mockData
    }

    override suspend fun fetchDetailsData(id: String): EventsDetails.UIModel {
        return EventsDetailsMockData
    }
}