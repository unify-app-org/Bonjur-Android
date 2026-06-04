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
) : EventsUseCase {

    override suspend fun fetchEventsData(): List<EventsCardModel> = EventsCardMocks.previewMock

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchDetailsData(id: String): EventsDetails.UIModel = EventsDetailsMockData

    override suspend fun createEvent(
        name: String,
        about: String,
        location: String,
        ownerContact: String,
        capacity: Int?,
        rules: String,
        isPublic: Boolean,
        eventDate: String
    ) {
        dataSource.createEvent(
            com.bonjur.events.data.DTOs.EventCreateRequest(
                name = name,
                about = about,
                location = location,
                ownerContact = ownerContact,
                capacity = capacity,
                rules = rules.ifBlank { null },
                visibility = if (isPublic) "PUBLIC" else "PRIVATE",
                eventDate = eventDate
            )
        )
    }

    override suspend fun editEvent(
        eventId: String,
        name: String,
        about: String,
        location: String,
        ownerContact: String,
        capacity: Int?,
        rules: String,
        isPublic: Boolean,
        eventDate: String
    ) {
        dataSource.editEvent(
            eventId = eventId,
            request = com.bonjur.events.data.DTOs.EventCreateRequest(
                name = name,
                about = about,
                location = location,
                ownerContact = ownerContact,
                capacity = capacity,
                rules = rules.ifBlank { null },
                visibility = if (isPublic) "PUBLIC" else "PRIVATE",
                eventDate = eventDate
            )
        )
    }
}