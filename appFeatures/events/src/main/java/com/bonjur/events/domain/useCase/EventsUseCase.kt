package com.bonjur.events.domain.useCase

import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.events.data.dataSource.EventAttachmentFile
import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.events.presentation.create.models.EventSelectableClub
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MembersPage

/** Fully-assembled create payload built by the view model. */
data class EventFormData(
    val clubId: Int,
    val name: String,
    val about: String,
    val location: String,
    val ownerContact: String,
    val capacity: Int?,
    val rule: String,
    val isPublic: Boolean,
    val eventDate: String,
    val reminderTimes: List<String>,
    val categoryIds: List<Int>,
    val links: List<AppFieldSchema.LinkItem>,
    val background: ByteArray?,
    val attachments: List<EventAttachmentFile>
)

interface EventsUseCase {
    suspend fun fetchEventsData(
        categoryIds: List<Int>,
        keyword: String?,
        page: Int,
        size: Int
    ): List<EventsCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchDetailsData(id: String): EventsDetails.UIModel

    suspend fun fetchClubsForEvents(): List<EventSelectableClub>

    suspend fun getCategories(): List<CategorySection>

    suspend fun createEvent(form: EventFormData)

    suspend fun editEvent(eventId: String, form: EventFormData)

    suspend fun joinEvent(eventId: String)

    suspend fun exitEvent(eventId: String)

    suspend fun fetchEventMembers(eventId: String): GroupedMembersData

    suspend fun fetchEventMembersPage(eventId: String, page: Int, size: Int): MembersPage
}
