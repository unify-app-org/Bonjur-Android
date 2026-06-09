package com.bonjur.events.domain.useCase

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.attachments.AttachmentItemModel
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.events.data.DTOs.EventCategorySectionResponse
import com.bonjur.events.data.DTOs.EventCreateRequest
import com.bonjur.events.data.DTOs.EventDetailResponse
import com.bonjur.events.data.DTOs.EventLinkDTO
import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.events.data.dataSource.EventsDataSource
import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.events.presentation.create.models.EventSelectableClub
import com.bonjur.events.presentation.list.models.EventsCardModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class EventsUseCaseImpl @Inject constructor(
    val dataSource: EventsDataSource
) : EventsUseCase {

    private val defaultQuery: Map<String, String> = mapOf("page" to "0", "size" to "10")

    override suspend fun fetchEventsData(): List<EventsCardModel> =
        dataSource.getEvents(defaultQuery).map { it.toCardModel() }

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchDetailsData(id: String): EventsDetails.UIModel {
        val detail = dataSource.getEventById(id)
        val members = runCatching {
            dataSource.getEventMembers(id, mapOf("page" to "0", "size" to "100"))
        }.getOrNull()
        return detail.toUIModel(membersCount = members?.totalElements ?: detail.membersCount)
    }

    override suspend fun fetchClubsForEvents(): List<EventSelectableClub> =
        dataSource.getClubsForEvents().map {
            EventSelectableClub(
                clubId = it.clubId,
                clubName = it.clubName ?: "",
                profileUrl = it.profileUrl
            )
        }

    override suspend fun getCategories(): List<CategorySection> =
        dataSource.getCategories().map { it.toSection() }

    override suspend fun createEvent(form: EventFormData) {
        dataSource.createEvent(
            request = form.toRequest(),
            background = form.background,
            attachments = form.attachments
        )
    }

    override suspend fun editEvent(eventId: String, form: EventFormData) {
        dataSource.editEvent(
            eventId = eventId,
            request = form.toRequest(),
            background = form.background,
            attachments = form.attachments
        )
    }

    // MARK: - Mappers

    private fun EventFormData.toRequest() = EventCreateRequest(
        clubId = clubId,
        name = name,
        about = about,
        location = location,
        ownerContact = ownerContact,
        capacity = capacity,
        rule = rule.ifBlank { null },
        visibility = if (isPublic) "PUBLIC" else "PRIVATE",
        eventDate = eventDate.toIsoDate(),
        reminderTime = reminderTime,
        categoryIds = categoryIds,
        links = links.map { EventLinkDTO(type = it.type, name = it.name, url = it.url) },
        userIds = emptyList()
    )

    private fun EventCategorySectionResponse.toSection() = CategorySection(
        type = type ?: "",
        title = title ?: "",
        categories = subCategories.map {
            CategoriesChipModel(id = it.id ?: 0, title = it.title ?: "", selected = false)
        }
    )

    private fun EventListResponse.toCardModel() = EventsCardModel(
        id = id ?: "",
        name = name ?: "-",
        coverImageURL = background,
        memberCount = membersCount ?: 0,
        totalCapacity = capacity,
        club = EventsCardModel.Club(name = "", id = 0),
        tags = categoryResponses.map {
            AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title)
        },
        bgType = AppUIEntities.BackgroundType.Primary,
        accessType = visibility.toAccessType(),
        requestType = requestStatus.toRequestType()
    )

    private fun EventDetailResponse.toUIModel(membersCount: Int?) = EventsDetails.UIModel(
        name = name,
        communityName = club?.name ?: "-",
        membersCount = membersCount ?: 0,
        coverImage = backgroundUrl,
        coverColorType = AppUIEntities.BackgroundType.Primary,
        userActivityType = eventUserRole.toActivityRole(),
        accessType = visibility.toAccessType(),
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        infoData = buildInfoData(this),
        attachments = attachments.mapIndexed { index, url ->
            AttachmentItemModel(
                id = index,
                name = url.substringAfterLast('/').ifBlank { "Attachment" },
                size = ""
            )
        }
    )

    private fun buildInfoData(detail: EventDetailResponse): List<EventsDetails.Info> = buildList {
        if (!detail.about.isNullOrBlank()) {
            add(
                EventsDetails.Info(
                    title = "About",
                    subItems = listOf(EventsDetails.SubInfo(title = null, description = detail.about!!))
                )
            )
        }
        add(
            EventsDetails.Info(
                title = "Event info",
                subItems = buildList {
                    detail.modifiedAt?.let { add(EventsDetails.SubInfo(title = "Updated", description = it)) }
                    detail.ownerContact?.let { add(EventsDetails.SubInfo(title = "Owner contact", description = it)) }
                    detail.capacity?.let {
                        add(EventsDetails.SubInfo(title = "Capacity", description = "${detail.membersCount ?: 0}/$it members"))
                    }
                    detail.rule?.let { add(EventsDetails.SubInfo(title = "Rules", description = it)) }
                    detail.location?.let { add(EventsDetails.SubInfo(title = "Location", description = it)) }
                }
            )
        )
        if (detail.links.isNotEmpty()) {
            add(
                EventsDetails.Info(
                    title = "Links",
                    subItems = detail.links.map {
                        EventsDetails.SubInfo(title = it.name, description = it.url, isLink = true)
                    }
                )
            )
        }
    }

    /** Converts the picker's `yyyy-MM-dd HH:mm` (UTC) to backend ISO 8601. */
    private fun String.toIsoDate(): String {
        if (isBlank()) return ""
        return runCatching {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val iso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            iso.format(parser.parse(this) ?: return this)
        }.getOrDefault(this)
    }

    private fun String?.toAccessType(): AppUIEntities.AccessType =
        if (this?.uppercase() == "PUBLIC") AppUIEntities.AccessType.PUBLIC
        else AppUIEntities.AccessType.PRIVATE

    private fun String?.toRequestType(): AppUIEntities.RequestType = when (this?.uppercase()) {
        "JOINED", "ACCEPTED" -> AppUIEntities.RequestType.JOINED
        "PENDING" -> AppUIEntities.RequestType.PENDING
        "REJECTED" -> AppUIEntities.RequestType.REJECTED
        else -> AppUIEntities.RequestType.NONE
    }

    private fun String?.toActivityRole(): AppUIEntities.UserActivityRole = when (this?.uppercase()) {
        "MEMBER" -> AppUIEntities.UserActivityRole.MEMBER
        "PRESIDENT" -> AppUIEntities.UserActivityRole.PRESIDENT
        "VISE_PRESIDENT", "VICE_PRESIDENT" -> AppUIEntities.UserActivityRole.VISE_PRESIDENT
        "EVENT_CREATOR" -> AppUIEntities.UserActivityRole.EVENT_CREATOR
        else -> AppUIEntities.UserActivityRole.NOT_JOINED
    }
}
