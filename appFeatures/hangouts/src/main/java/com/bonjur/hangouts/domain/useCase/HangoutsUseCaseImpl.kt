package com.bonjur.hangouts.domain.useCase

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.hangouts.data.DTOs.HangoutCategorySectionResponse
import com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
import com.bonjur.hangouts.data.DTOs.HangoutDetailResponse
import com.bonjur.hangouts.data.DTOs.HangoutJoinRequest
import com.bonjur.hangouts.data.DTOs.HangoutLinkDTO
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.hangouts.data.DTOs.HangoutMemberResponse
import com.bonjur.hangouts.data.dataSource.HangoutsDataSource
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MembersPage
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.create.models.HangoutCreatePrefillData
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class HangoutsUseCaseImpl @Inject constructor(
    val dataSource: HangoutsDataSource
) : HangoutsUseCase {

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

    override suspend fun fetchHangoutsData(
        categoryIds: List<Int>,
        keyword: String?,
        page: Int,
        size: Int
    ): List<HangoutsCardModel> =
        dataSource.getHangouts(buildHangoutsQuery(categoryIds, keyword, page, size))
            .map { it.toCardModel() }

    /**
     * Discover hangouts query (GET api/ds/v1/hangouts). Mirrors iOS:
     * page/size always sent, categoryIds comma-joined when present, keyword when non-blank.
     */
    private fun buildHangoutsQuery(
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

    override suspend fun fetchDetailData(id: String): HangoutDetails.UIModel =
        dataSource.getHangoutById(id).toUIModel()

    override suspend fun getCategories(): List<CategorySection> =
        dataSource.getCategories().map { it.toSection() }

    override suspend fun createHangout(form: HangoutFormData) {
        dataSource.createHangout(form.toRequest())
    }

    override suspend fun editHangout(hangoutId: String, form: HangoutFormData) {
        dataSource.editHangout(hangoutId, form.toRequest())
    }

    override suspend fun joinHangout(hangoutId: String) {
        dataSource.joinHangout(HangoutJoinRequest(hangoutId = hangoutId))
    }

    override suspend fun exitHangout(hangoutId: String) {
        dataSource.exitHangout(hangoutId)
    }

    override suspend fun fetchHangoutMembers(hangoutId: String): GroupedMembersData {
        val users = dataSource.getHangoutMembers(hangoutId, page = 0, size = 10)
            .content.map { it.toCellModel() }
        return GroupedMembersData.from(users)
    }

    override suspend fun fetchHangoutMembersPage(hangoutId: String, page: Int, size: Int, keyword: String?): MembersPage {
        val users = dataSource.getHangoutMembers(hangoutId, page, size, keyword).content.map { it.toCellModel() }
        return MembersPage(members = users, hasMore = users.size >= size)
    }

    private fun HangoutMemberResponse.toCellModel() = MemberCellModel(
        id = userId ?: "-",
        name = fullName ?: "-",
        avatarUrl = profileUrl,
        subtitle = listOfNotNull(degree, specialization, entryYear?.toString()).joinToString(", "),
        role = role?.toActivityRole() ?: AppUIEntities.UserActivityRole.MEMBER
    )

    // MARK: - Mappers

    private fun HangoutFormData.toRequest() = HangoutCreateRequest(
        visibility = if (isPublic) "PUBLIC" else "PRIVATE",
        name = name,
        ownerContact = ownerContact,
        categoriesId = categoryIds,
        capacity = capacity ?: 0,
        links = links.map { HangoutLinkDTO(type = it.type, name = it.name, url = it.url) },
        rules = rules,
        location = location,
        about = about,
        hangoutDate = hangoutDate.toIsoDate()
    )

    private fun HangoutCategorySectionResponse.toSection() = CategorySection(
        type = type ?: "",
        title = title ?: "",
        categories = subCategories.map {
            CategoriesChipModel(id = it.id ?: 0, title = it.title ?: "", selected = false)
        }
    )

    private fun HangoutListResponse.toCardModel(): HangoutsCardModel {
        val parts = hangoutDate.toDateParts()
        return HangoutsCardModel(
            id = id ?: "",
            name = name ?: "",
            description = about ?: "",
            memberCount = membersCount ?: 0,
            totalCapacity = capacity,
            tags = categoryResponses.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
            accessType = visibility.toAccessType(),
            requestType = requestStatus.toRequestType(),
            dateDay = parts?.day,
            dateMonth = parts?.month,
            time = parts?.time,
            location = location
        )
    }

    private fun HangoutDetailResponse.toUIModel() = HangoutDetails.UIModel(
        name = name,
        communityName = community?.name ?: "",
        membersCount = membersCount ?: 0,
        userActivityType = role.toActivityRole(),
        accessType = visibility.toAccessType(),
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        infoData = buildInfoData(this),
        joinButton = toJoinButton(),
        editPrefillData = toEditPrefill()
    )

    /**
     * Bottom join/request button. Mirrors iOS `mapButtonModel`: hidden once
     * joined/accepted; a pending request shows a disabled "Request sent" button.
     */
    private fun HangoutDetailResponse.toJoinButton(): HangoutDetails.JoinButton? {
        val role = role.toActivityRole()
        val request = requestStatus.toRequestType()
        if (role != AppUIEntities.UserActivityRole.NOT_JOINED ||
            request == AppUIEntities.RequestType.JOINED
        ) return null
        if (request == AppUIEntities.RequestType.PENDING) {
            return HangoutDetails.JoinButton(title = "Request sent", disabled = true)
        }
        val title = if (visibility?.uppercase() == "PUBLIC") "Join" else "Request"
        return HangoutDetails.JoinButton(title = title, disabled = false)
    }

    /** Edit-screen pre-fill from the detail response. Mirrors iOS `editPrefillData`. */
    private fun HangoutDetailResponse.toEditPrefill() = HangoutCreatePrefillData(
        values = mapOf(
            AppFieldSchema.FieldId.VISIBILITY to AppFieldSchema.FieldValue.Radio(visibility.toAccessType()),
            AppFieldSchema.FieldId.HANGOUT_NAME to AppFieldSchema.FieldValue.TextValue(name),
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
            AppFieldSchema.FieldId.HANGOUT_DATE to
                AppFieldSchema.FieldValue.DateValue(hangoutDate.toPickerDate()),
            AppFieldSchema.FieldId.RULES to AppFieldSchema.FieldValue.TextValue(rules ?: ""),
            AppFieldSchema.FieldId.ABOUT to AppFieldSchema.FieldValue.TextValue(about ?: "")
        )
    )

    /** Mirrors iOS `HangoutRepoImpl.fetchDetailHangout` info sections (no reminders/cover). */
    private fun buildInfoData(detail: HangoutDetailResponse): List<HangoutDetails.Info> = buildList {
        appendSection("About", listOf(infoRow(title = null, value = detail.about)))

        appendSection(
            "Event info",
            listOf(
                infoRow(title = "Date", value = detail.hangoutDate.meetupDate()),
                infoRow(
                    title = "Owner Contact",
                    value = cleaned(detail.ownerContact),
                    phoneNumber = detail.ownerContact.dialablePhone()
                ),
                infoRow(title = "Capacity", value = capacityText(detail.membersCount, detail.capacity)),
                infoRow(title = "Rules", value = detail.rules),
                infoRow(title = "Location", value = detail.location)
            )
        )

        appendSection("Links", detail.links.map {
            infoRow(title = it.name, value = it.url, isLink = true)
        })
    }

    private fun MutableList<HangoutDetails.Info>.appendSection(
        title: String,
        rows: List<HangoutDetails.SubInfo?>
    ) {
        val items = rows.filterNotNull()
        if (items.isNotEmpty()) add(HangoutDetails.Info(title = title, subItems = items))
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
    ): HangoutDetails.SubInfo? {
        val v = cleaned(value) ?: return null
        return HangoutDetails.SubInfo(title = title, description = v, isLink = isLink, phoneNumber = phoneNumber)
    }

    private fun capacityText(members: Int?, capacity: Int?): String? {
        if (capacity == null || capacity <= 0) return null
        return "${members ?: 0}/$capacity members"
    }

    /** Returns the contact only when it looks dialable. Mirrors iOS `phoneNumber`. */
    private fun String?.dialablePhone(): String? {
        val v = cleaned(this) ?: return null
        val digits = v.count { it.isDigit() }
        val allowed = v.all { it.isDigit() || it in "+ -()" }
        return if (allowed && digits >= 7) v else null
    }

    // MARK: - Date helpers

    private data class DateParts(val day: String, val month: String, val time: String)

    /** Card date badge parts (day / MMM / HH:mm) in device-local time. */
    private fun String?.toDateParts(): DateParts? {
        val date = parseIso(this) ?: return null
        val local = { fmt: String ->
            SimpleDateFormat(fmt, Locale.US).apply { timeZone = TimeZone.getDefault() }.format(date)
        }
        return DateParts(day = local("d"), month = local("MMM").uppercase(), time = local("HH:mm"))
    }

    /** Meetup date+time in device-local time, e.g. "14 June 2026 18:00". */
    private fun String?.meetupDate(): String? {
        val date = parseIso(this) ?: return null
        return SimpleDateFormat("d MMMM yyyy HH:mm", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }.format(date)
    }

    /** ISO hangout date → the create date picker's stored `yyyy-MM-dd HH:mm` (UTC) format. */
    private fun String?.toPickerDate(): String {
        val date = parseIso(this) ?: return ""
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(date)
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
