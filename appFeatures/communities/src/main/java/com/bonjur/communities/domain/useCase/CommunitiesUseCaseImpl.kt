package com.bonjur.communities.domain.useCase

import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.data.DTOs.CommunityClubResponse
import com.bonjur.communities.data.DTOs.CommunityDetailResponse
import com.bonjur.communities.data.DTOs.CommunityListResponse
import com.bonjur.communities.data.DTOs.RoleAssignRequest
import com.bonjur.communities.data.dataSource.CommunitiesDataSource
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.communities.data.DTOs.CommunityMemberResponse
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MemberListSectionModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MembersPage
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.commonModel.toUserActivityRole
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CommunitiesUseCaseImpl @Inject constructor(
    val dataSource: CommunitiesDataSource
) : CommunitiesUseCase {

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> =
        dataSource.fetchCommunities().map { it.toCardModel() }

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchCommunityDetails(communityId: Int): CommunityDetails.UIModel =
        dataSource.fetchCommunityDetail(communityId).toUIModel()

    override suspend fun fetchClubs(communityId: Int): List<ClubCardModel> =
        dataSource.getClubs(clubsQuery(communityId)).map { it.toCardModel() }

    override suspend fun fetchCommunityMembers(communityId: Int): GroupedMembersData {
        val users = dataSource.fetchCommunityMembers(communityId, page = 0, size = 10)
            .content.map { it.toCellModel() }
        return GroupedMembersData.from(users)
    }

    override suspend fun fetchCommunityMembersPage(
        communityId: Int,
        page: Int,
        size: Int
    ): MembersPage {
        val users = dataSource.fetchCommunityMembers(communityId, page = page, size = size)
            .content.map { it.toCellModel() }
        return MembersPage(members = users, hasMore = users.size >= size)
    }

    override suspend fun assignRole(
        communityId: Int,
        userId: String,
        role: AppUIEntities.UserActivityRole
    ) {
        dataSource.assignRole(
            communityId = communityId,
            request = RoleAssignRequest(userId = userId, role = role.toApiString())
        )
    }

    // MARK: - Detail mapping (mirrors iOS CommunityRepoImpl.fetchClubById)

    private fun CommunityDetailResponse.toUIModel() =
        CommunityDetails.UIModel(
            name = name,
            membersCount = membersCount ?: 0,
            logo = logoUrl,
            coverImage = backgroundUrl,
            coverColorType = backgroundColour.toBackgroundType(),
            userActivity = clubUserRole?.toUserActivityRole() ?: AppUIEntities.UserActivityRole.NOT_JOINED,
            tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
            infoData = buildInfoData(this),
            editPrefillData = toEditPrefill()
        )

    /** Builds the edit-screen pre-fill (form values + image URLs). Mirrors iOS `CommunityRepoImpl` prefill. */
    private fun CommunityDetailResponse.toEditPrefill() = ClubsDetails.ClubEditPrefill(
        logoUrl = logoUrl,
        coverUrl = backgroundUrl,
        values = mapOf(
            AppFieldSchema.FieldId.COVER to
                AppFieldSchema.FieldValue.Cover(backgroundColour.toBackgroundType()),
            AppFieldSchema.FieldId.VISIBILITY to AppFieldSchema.FieldValue.Radio(
                if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC
                else AppUIEntities.AccessType.PRIVATE
            ),
            AppFieldSchema.FieldId.CLUB_NAME to AppFieldSchema.FieldValue.TextValue(name),
            AppFieldSchema.FieldId.OWNER_CONTACT to
                AppFieldSchema.FieldValue.TextValue(ownerContact ?: ""),
            AppFieldSchema.FieldId.CATEGORY to AppFieldSchema.FieldValue.Tags(
                categories.map { AppFieldSchema.TagItem(id = it.id, label = it.title) }
            ),
            AppFieldSchema.FieldId.CAPACITY to
                AppFieldSchema.FieldValue.TextValue(capacity?.toString() ?: ""),
            AppFieldSchema.FieldId.LINKS to AppFieldSchema.FieldValue.Links(
                (links ?: emptyList()).map { AppFieldSchema.LinkItem(type = it.type, name = it.name, url = it.url) }
            ),
            AppFieldSchema.FieldId.LOCATION to AppFieldSchema.FieldValue.TextValue(location ?: ""),
            AppFieldSchema.FieldId.RULES to AppFieldSchema.FieldValue.TextValue(rule ?: ""),
            AppFieldSchema.FieldId.ABOUT to AppFieldSchema.FieldValue.TextValue(about ?: "")
        )
    )

    private fun buildInfoData(detail: CommunityDetailResponse): List<CommunityDetails.Info> = buildList {
        appendSection(this, "About", listOf(row(title = null, value = detail.about)))

        appendSection(
            this, "Event info", listOf(
                row(title = "Created/Updated Date", value = modifiedDate(detail.modifiedAt)),
                row(
                    title = "Owner Contact",
                    value = cleaned(detail.ownerContact),
                    phoneNumber = phoneNumber(detail.ownerContact)
                ),
                row(title = "Capacity", value = capacityText(detail.membersCount, detail.capacity)),
                row(title = "Rules", value = detail.rule),
                row(title = "Location", value = detail.location)
            )
        )

        appendSection(this, "Links", (detail.links ?: emptyList()).map { row(title = it.name, value = it.url, isLink = true) })
    }

    // MARK: - Info builders (mirror iOS private CommunityRepoImpl extensions)

    /** Trim + treat empty / "-" / "none" as absent, so empty rows are dropped. */
    private fun cleaned(value: String?): String? {
        val v = value?.trim() ?: return null
        if (v.isEmpty() || v == "-" || v.lowercase() == "none") return null
        return v
    }

    private fun row(
        title: String?,
        value: String?,
        isLink: Boolean = false,
        phoneNumber: String? = null
    ): CommunityDetails.SubInfo? {
        val cleanedValue = cleaned(value) ?: return null
        return CommunityDetails.SubInfo(
            title = title,
            description = cleanedValue,
            isLink = isLink,
            phoneNumber = phoneNumber
        )
    }

    private fun appendSection(
        sink: MutableList<CommunityDetails.Info>,
        title: String,
        rows: List<CommunityDetails.SubInfo?>
    ) {
        val items = rows.filterNotNull()
        if (items.isEmpty()) return
        sink.add(CommunityDetails.Info(title = title, subItems = items))
    }

    private fun capacityText(members: Int?, capacity: Int?): String? {
        if (capacity == null || capacity <= 0) return null
        return "${members ?: 0}/$capacity members"
    }

    /** `dd-MM-yyyy HH:mm:ss` audit stamp → date-only display; falls back to raw on parse failure. */
    private fun modifiedDate(value: String?): String? {
        val v = cleaned(value) ?: return null
        return runCatching {
            val parsed = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).parse(v)
            parsed?.let { SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it) }
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: v
    }

    /** Returns the contact only when it looks like a dialable phone number. */
    private fun phoneNumber(value: String?): String? {
        val v = cleaned(value) ?: return null
        val allowed = "+0123456789 -()".toSet()
        val digitCount = v.count { it.isDigit() }
        if (!v.all { it in allowed } || digitCount < 7) return null
        return v
    }

    // MARK: - Card mapping

    private fun CommunityClubResponse.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "-",
        communityName = communityName ?: "-",
        logoURL = clubProfile ?: "",
        memberCount = memberCount ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "-",
        members = members.map { AppUIEntities.Member(id = it.id?.hashCode() ?: 0, profileImage = it.url) },
        bgType = background.toBackgroundType(),
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        requestType = requestStatus.toRequestType(),
        role = role?.toUserActivityRole(),
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title },
        isVerified = AppUIEntities.ClubStatus.from(clubStatus)?.isVerified == true
    )

    private fun CommunityListResponse.toCardModel() = CommunityCardModel(
        id = id ?: 0,
        name = name ?: "-",
        subTitle = "Community",
        logoURL = profile ?: "",
        memberCount = membersCount ?: 0,
        members = members.mapIndexed { index, m -> AppUIEntities.Member(id = index, profileImage = m.url) },
        bgType = background.toBackgroundType(),
        clubsCount = clubCount ?: 0
    )

    private fun CommunityMemberResponse.CommunityMember.toCellModel() = MemberCellModel(
        id = userId ?: "-",
        name = fullName ?: "-",
        avatarUrl = profileUrl,
        subtitle = listOfNotNull(degree, specialization, entryYear?.toString()).joinToString(", "),
        role = role?.toUserActivityRole() ?: AppUIEntities.UserActivityRole.MEMBER
    )

    // MARK: - Faculty screens (Android-specific, grouped from members endpoint)

    override suspend fun fetchFaculties(communityId: String): List<FacultyRowModel> {
        val id = communityId.toIntOrNull() ?: return emptyList()
        return dataSource.fetchCommunityMembers(id).content
            .groupBy { it.degree ?: "Unknown" }
            .map { (degree, members) -> FacultyRowModel(id = degree, title = degree, memberCount = members.size) }
            .sortedBy { it.title }
    }

    override suspend fun fetchFacultyMembersForCommunity(
        communityId: Int,
        degree: String
    ): List<MemberListSectionModel> {
        val response = dataSource.fetchCommunityMembers(communityId)
        return response.content
            .filter { it.degree == degree }
            .groupBy { it.specialization ?: "General" }
            .map { (specialization, members) ->
                MemberListSectionModel(
                    title = specialization,
                    memberCount = members.size,
                    members = members.map { m ->
                        MemberCellModel(
                            id = m.userId ?: "",
                            name = m.fullName ?: "-",
                            avatarUrl = m.profileUrl,
                            subtitle = listOfNotNull(m.degree, m.specialization, m.entryYear?.toString())
                                .joinToString(", ")
                        )
                    }
                )
            }
    }

    // MARK: - Mappers

    private fun clubsQuery(communityId: Int): Map<String, String> = mapOf(
        "parentId" to communityId.toString(),
        "page" to "0",
        "size" to "10"
    )

    private fun AppUIEntities.UserActivityRole.toApiString(): String = when (this) {
        AppUIEntities.UserActivityRole.MEMBER -> "MEMBER"
        AppUIEntities.UserActivityRole.PRESIDENT -> "PRESIDENT"
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> "VISE_PRESIDENT"
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> "EVENT_CREATOR"
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
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

    private fun String?.toRequestType(): AppUIEntities.RequestType = when (this?.uppercase()) {
        "JOINED", "ACCEPTED" -> AppUIEntities.RequestType.JOINED
        "PENDING" -> AppUIEntities.RequestType.PENDING
        "REJECTED" -> AppUIEntities.RequestType.REJECTED
        else -> AppUIEntities.RequestType.NONE
    }
}
