package com.bonjur.events.domain.useCase

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.attachments.AttachmentItemModel
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.events.data.DTOs.EventCategorySectionResponse
import com.bonjur.events.data.DTOs.EventCreateRequest
import com.bonjur.events.data.DTOs.EventDetailResponse
import com.bonjur.events.data.DTOs.EventLinkDTO
import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.events.data.DTOs.EventMembersResponse
import com.bonjur.events.data.dataSource.EventsDataSource
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MembersPage
import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.events.presentation.create.models.EventCreatePrefillData
import com.bonjur.events.presentation.create.models.EventSelectableClub
import com.bonjur.events.presentation.list.models.EventsCardModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class EventsUseCaseImpl @Inject constructor(
    val dataSource: EventsDataSource
) : EventsUseCase {

    override suspend fun fetchEventsData(
        categoryIds: List<Int>,
        keyword: String?,
        page: Int,
        size: Int
    ): List<EventsCardModel> =
        dataSource.getEvents(buildEventsQuery(categoryIds, keyword, page, size))
            .map { it.toCardModel() }

    /**
     * Discover events query (GET api/ds/v1/events). Mirrors iOS `EventsRepo.fetchEvents`:
     * page/size always sent, categoryIds comma-joined when present, keyword when non-blank.
     */
    private fun buildEventsQuery(
        categoryIds: List<Int>,
        keyword: String?,
        page: Int,
        size: Int
    ): Map<String, String> {
        val query = mutableMapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )
        if (categoryIds.isNotEmpty()) {
            query["categoryIds"] = categoryIds.joinToString(",")
        }
        keyword?.trim()?.takeIf { it.isNotEmpty() }?.let { query["keyword"] = it }
        return query
    }

    /** Real category fetch -> filter sections. Mirrors iOS `getFilterCategories`. */
    override suspend fun fetchFilterData(): List<FilterView.Model> =
        dataSource.getCategories().map { section ->
            FilterView.Model(
                title = section.title ?: "",
                type = section.type ?: "",
                items = section.subCategories.map { sub ->
                    FilterView.Items(title = sub.title ?: "", id = sub.id ?: 0)
                }
            )
        }

    override suspend fun fetchDetailsData(id: String): EventsDetails.UIModel {
        val detail = dataSource.getEventById(id)
        val members = runCatching {
            dataSource.getEventMembers(id, mapOf("page" to "0", "size" to "100"))
        }.getOrNull()
        return detail.toUIModel(membersCount = members?.totalElements ?: detail.membersCount)
    }

    override suspend fun fetchClubsForEvents(): List<EventSelectableClub> =
        dataSource.getClubsForEvents().content.map {
            EventSelectableClub(
                clubId = it.id,
                clubName = it.name ?: "",
                profileUrl = it.clubProfile,
                backgroundUrl = it.backgroundUrl,
                role = it.role.toActivityRole(),
                background = it.background.toBackgroundType()
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

    override suspend fun joinEvent(eventId: String) {
        dataSource.joinEvent(eventId)
    }

    override suspend fun exitEvent(eventId: String) {
        dataSource.exitEvent(eventId)
    }

    override suspend fun fetchEventMembers(eventId: String): GroupedMembersData {
        val users = dataSource.getEventMembers(eventId, mapOf("page" to "0", "size" to "10"))
            .content.map { it.toCellModel() }
        return GroupedMembersData.from(users)
    }

    override suspend fun fetchEventMembersPage(eventId: String, page: Int, size: Int): MembersPage {
        val users = dataSource.getEventMembers(
            eventId,
            mapOf("page" to page.toString(), "size" to size.toString())
        ).content.map { it.toCellModel() }
        return MembersPage(members = users, hasMore = users.size >= size)
    }

    private fun EventMembersResponse.EventMember.toCellModel() = MemberCellModel(
        id = userId ?: "-",
        name = fullName ?: "-",
        avatarUrl = profileUrl,
        subtitle = listOfNotNull(degree, specialization, entryYear?.toString()).joinToString(", "),
        role = role?.toActivityRole() ?: AppUIEntities.UserActivityRole.MEMBER
    )

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
        reminderTimes = reminderTimes.ifEmpty { listOf("NONE") },
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

    private fun EventListResponse.toCardModel(): EventsCardModel {
        val parts = eventDate.toDateParts()
        return EventsCardModel(
            id = id ?: "",
            name = name ?: "-",
            coverImageURL = background,
            memberCount = membersCount ?: 0,
            totalCapacity = capacity,
            club = EventsCardModel.Club(name = club?.name ?: "-", id = club?.id ?: 0),
            tags = categoryResponses.map {
                AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title)
            },
            bgType = AppUIEntities.BackgroundType.Primary,
            accessType = visibility.toAccessType(),
            requestType = requestStatus.toRequestType(),
            time = parts.time,
            location = location ?: "-",
            dateDay = parts.day,
            dateMonth = parts.month
        )
    }

    private fun EventDetailResponse.toUIModel(membersCount: Int?) = EventsDetails.UIModel(
        name = name,
        communityName = club?.name ?: "-",
        clubId = club?.id ?: 0,
        membersCount = membersCount ?: 0,
        coverImage = backgroundUrl,
        coverColorType = AppUIEntities.BackgroundType.Primary,
        userActivityType = eventUserRole.toActivityRole(),
        accessType = visibility.toAccessType(),
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        infoData = buildInfoData(this),
        attachments = attachments.mapIndexed { index, item ->
            AttachmentItemModel(
                id = index,
                name = item.name ?: item.url?.substringAfterLast('/').orEmpty().ifBlank { "Attachment" },
                size = item.size ?: ""
            )
        },
        joinButton = toJoinButton(),
        editPrefillData = toEditPrefill()
    )

    /**
     * Bottom join/request button. Mirrors iOS `mapButtonModel`: hidden once
     * joined/accepted; a pending request shows a disabled "Request sent" button.
     */
    private fun EventDetailResponse.toJoinButton(): EventsDetails.JoinButton? {
        val role = eventUserRole.toActivityRole()
        val request = requestStatus.toRequestType()
        if (role != AppUIEntities.UserActivityRole.NOT_JOINED ||
            request == AppUIEntities.RequestType.JOINED
        ) return null
        if (request == AppUIEntities.RequestType.PENDING) {
            return EventsDetails.JoinButton(title = "Request sent", disabled = true)
        }
        val title = if (visibility?.uppercase() == "PUBLIC") "Join" else "Request"
        return EventsDetails.JoinButton(title = title, disabled = false)
    }

    /**
     * Builds the edit-screen pre-fill from the detail response. Mirrors iOS
     * `mapPrefilData` (and the Clubs port). The detail endpoint returns no
     * eventDate/reminder, so those fields stay at their create defaults.
     */
    private fun EventDetailResponse.toEditPrefill() = EventCreatePrefillData(
        selectedClubId = club?.id ?: 0,
        clubName = club?.name,
        clubCoverUrl = backgroundUrl,
        clubBackground = AppUIEntities.BackgroundType.Primary,
        values = mapOf(
            AppFieldSchema.FieldId.VISIBILITY to AppFieldSchema.FieldValue.Radio(
                if (visibility?.uppercase() == "PUBLIC") AppUIEntities.AccessType.PUBLIC
                else AppUIEntities.AccessType.PRIVATE
            ),
            AppFieldSchema.FieldId.EVENT_NAME to AppFieldSchema.FieldValue.TextValue(name),
            AppFieldSchema.FieldId.OWNER_CONTACT to
                AppFieldSchema.FieldValue.TextValue(ownerContact ?: ""),
            AppFieldSchema.FieldId.CATEGORY to AppFieldSchema.FieldValue.Tags(
                categories.map { AppFieldSchema.TagItem(id = it.id, label = it.title) }
            ),
            AppFieldSchema.FieldId.CAPACITY to
                AppFieldSchema.FieldValue.TextValue(capacity?.toString() ?: ""),
            AppFieldSchema.FieldId.LINKS to AppFieldSchema.FieldValue.Links(
                links.map { AppFieldSchema.LinkItem(type = it.type, name = it.name, url = it.url) }
            ),
            AppFieldSchema.FieldId.LOCATION to AppFieldSchema.FieldValue.TextValue(location ?: ""),
            AppFieldSchema.FieldId.RULES to AppFieldSchema.FieldValue.TextValue(rule ?: ""),
            AppFieldSchema.FieldId.ABOUT to AppFieldSchema.FieldValue.TextValue(about ?: ""),
            AppFieldSchema.FieldId.EVENT_DATE to
                AppFieldSchema.FieldValue.DateValue(eventDate.toPickerDate()),
            AppFieldSchema.FieldId.REMINDER to
                AppFieldSchema.FieldValue.Reminders(reminderTimes.toReminderOptions()),
            AppFieldSchema.FieldId.ATTACHMENT to AppFieldSchema.FieldValue.Attachments(
                attachments.map {
                    AppFieldSchema.AttachmentItem(
                        name = it.name ?: it.url?.substringAfterLast('/').orEmpty(),
                        uri = it.url ?: "",
                        size = it.size.toByteSize()
                    )
                }
            )
        )
    )

    /** ISO event date → the create date picker's stored `yyyy-MM-dd HH:mm` (UTC) format. */
    private fun String?.toPickerDate(): String {
        val date = parseIso(this) ?: return ""
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(date)
    }

    /** Detail reminder strings → create's multi-select options (drops NONE/unknown). */
    private fun List<String>.toReminderOptions(): List<AppFieldSchema.ReminderOption> =
        mapNotNull { raw ->
            if (raw.isBlank() || raw.uppercase() == "NONE") return@mapNotNull null
            runCatching { AppFieldSchema.ReminderOption.valueOf(raw.uppercase()) }.getOrNull()
        }

    /** Detail attachment size string ("16 kb", "14 MB", "1024") → bytes for `formatBytes`. */
    private fun String?.toByteSize(): Long {
        val v = this?.trim()?.lowercase() ?: return 0L
        if (v.isEmpty()) return 0L
        val number = v.takeWhile { it.isDigit() || it == '.' }.toDoubleOrNull() ?: return 0L
        val multiplier = when {
            "gb" in v -> 1024L * 1024 * 1024
            "mb" in v -> 1024L * 1024
            "kb" in v -> 1024L
            else -> 1L
        }
        return (number * multiplier).toLong()
    }

    /** Mirrors iOS `EventsRepoImpl.mapInfo` field-by-field. */
    private fun buildInfoData(detail: EventDetailResponse): List<EventsDetails.Info> = buildList {
        appendSection("About", listOf(infoRow(title = null, value = detail.about)))

        val eventRows = mutableListOf<EventsDetails.SubInfo?>(
            infoRow(title = "Date", value = detail.eventDate.meetupDate()),
            infoRow(title = "Created/Updated Date", value = detail.modifiedAt.modifiedDate()),
            infoRow(
                title = "Owner Contact",
                value = cleaned(detail.ownerContact),
                phoneNumber = detail.ownerContact.dialablePhone()
            ),
            infoRow(title = "Capacity", value = capacityText(detail.membersCount, detail.capacity)),
            infoRow(title = "Rules", value = detail.rule),
            infoRow(title = "Location", value = detail.location)
        )
        // Reminders are an organiser broadcast config — only organisers see them.
        if (isOrganizer(detail.eventUserRole.toActivityRole())) {
            reminderText(detail.reminderTimes)?.let {
                eventRows.add(infoRow(title = "Reminders", value = it))
            }
        }
        appendSection("Event info", eventRows)

        appendSection("Links", detail.links.map {
            infoRow(title = it.name, value = it.url, isLink = true)
        })
    }

    private fun MutableList<EventsDetails.Info>.appendSection(
        title: String,
        rows: List<EventsDetails.SubInfo?>
    ) {
        val items = rows.filterNotNull()
        if (items.isNotEmpty()) add(EventsDetails.Info(title = title, subItems = items))
    }

    /** Drops empty / "-" / "none" values, mirroring iOS `cleaned`. */
    private fun cleaned(value: String?): String? {
        val v = value?.trim() ?: return null
        return if (v.isEmpty() || v == "-" || v.lowercase() == "none") null else v
    }

    private fun infoRow(
        title: String?,
        value: String?,
        isLink: Boolean = false,
        phoneNumber: String? = null
    ): EventsDetails.SubInfo? {
        val v = cleaned(value) ?: return null
        return EventsDetails.SubInfo(title = title, description = v, isLink = isLink, phoneNumber = phoneNumber)
    }

    private fun capacityText(members: Int?, capacity: Int?): String? {
        if (capacity == null || capacity <= 0) return null
        return "${members ?: 0}/$capacity members"
    }

    private fun isOrganizer(role: AppUIEntities.UserActivityRole): Boolean = role in setOf(
        AppUIEntities.UserActivityRole.PRESIDENT,
        AppUIEntities.UserActivityRole.VISE_PRESIDENT,
        AppUIEntities.UserActivityRole.EVENT_CREATOR
    )

    private fun reminderText(values: List<String>): String? {
        val labels = values
            .filter { it.isNotBlank() && it.uppercase() != "NONE" }
            .map { raw ->
                runCatching { AppFieldSchema.ReminderOption.valueOf(raw.uppercase()).label }
                    .getOrDefault(raw)
            }
        return if (labels.isEmpty()) null else labels.joinToString(", ")
    }

    /** Returns the contact only when it looks dialable. Mirrors iOS `phoneNumber`. */
    private fun String?.dialablePhone(): String? {
        val v = cleaned(this) ?: return null
        val digits = v.count { it.isDigit() }
        val allowed = v.all { it.isDigit() || it in "+ -()" }
        return if (allowed && digits >= 7) v else null
    }

    // MARK: - Date display helpers

    private data class DateParts(val day: String, val month: String, val time: String)

    private fun String?.toDateParts(): DateParts {
        val date = parseIso(this) ?: return DateParts("-", "-", "-")
        val local = { fmt: String -> SimpleDateFormat(fmt, Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }.format(date) }
        return DateParts(day = local("d"), month = local("MMM").uppercase(), time = local("HH:mm"))
    }

    /** Meetup date+time in device-local time, e.g. "14 June 2026 18:00". */
    private fun String?.meetupDate(): String? {
        val date = parseIso(this) ?: return null
        return SimpleDateFormat("d MMMM yyyy HH:mm", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }.format(date)
    }

    /** Audit stamp → date-only display. */
    private fun String?.modifiedDate(): String? {
        val v = cleaned(this) ?: return null
        val parsed = runCatching {
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(v)
        }.getOrNull() ?: parseIso(v) ?: return v
        return SimpleDateFormat("d MMMM yyyy", Locale.US).format(parsed)
    }

    private fun parseIso(value: String?): java.util.Date? {
        val v = value?.trim().orEmpty()
        if (v.isEmpty()) return null
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
        for (p in patterns) {
            runCatching {
                SimpleDateFormat(p, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.parse(v)
            }.getOrNull()?.let { return it }
        }
        return null
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

    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType = when (this?.uppercase()) {
        "GREEN", "PRIMARY" -> AppUIEntities.BackgroundType.Primary
        "BLUE", "SECONDARY" -> AppUIEntities.BackgroundType.Secondary
        "PURPLE", "TERTIARY" -> AppUIEntities.BackgroundType.Tertiary
        "RED" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Red)
        "ORANGE" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Orange)
        "PINK" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Pink)
        else -> AppUIEntities.BackgroundType.Primary
    }
}
