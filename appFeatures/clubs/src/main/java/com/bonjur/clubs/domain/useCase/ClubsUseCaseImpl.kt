package com.bonjur.clubs.domain.useCase

import com.bonjur.designSystem.commonModel.memberOfCapacityText
import com.bonjur.designSystem.commonModel.dialablePhone
import com.bonjur.designsystem.R as DesignR
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.clubs.R
import com.bonjur.clubs.data.DTOs.CategorySectionResponse
import com.bonjur.clubs.data.DTOs.ClubCreateRequest
import com.bonjur.clubs.data.DTOs.ClubDetailResponse
import com.bonjur.clubs.data.DTOs.ClubLinkRequest
import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.clubs.data.DTOs.ClubMemberResponse
import com.bonjur.clubs.data.DTOs.RoleAssignRequest
import com.bonjur.clubs.data.dataSource.ClubsDataSource
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.MembersPage
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import javax.inject.Inject

class ClubsUseCaseImpl @Inject constructor(
    val dataSource: ClubsDataSource,
    private val defaultStorage: DefaultStorage
) : ClubsUseCase {

    override suspend fun fetchClubsData(
        size: Int,
        keyword: String?,
        categoryIds: List<Int>
    ): List<ClubCardModel> {
        return dataSource.getClubs(buildClubsQuery(size, keyword, categoryIds))
            .map { it.toCardModel() }
    }

    /**
     * Mirrors iOS `ClubsViewModel.makeQuery` + `ClubRepo.fetchClubs`:
     * page is always 0, size grows for "load more", optional keyword search,
     * parentId scopes to the active community, optional categoryIds for filters.
     * Built with mutableMapOf (not buildMap) to avoid `size` shadowing the map.
     */
    private fun buildClubsQuery(
        size: Int,
        keyword: String?,
        categoryIds: List<Int>
    ): Map<String, String> {
        val query = mutableMapOf(
            "page" to "0",
            "size" to size.toString(),
            "parentId" to defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0).toString()
        )
        keyword?.trim()?.takeIf { it.isNotEmpty() }?.let { query["keyword"] = it }
        if (categoryIds.isNotEmpty()) {
            query["categoryIds"] = categoryIds.joinToString(",")
        }
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

    override suspend fun fetchClubsDetails(clubId: Int): ClubsDetails.UIModel {
        return dataSource.getClubById(clubId).toUIModel()
    }

    override suspend fun getCategories(): List<CategorySection> =
        dataSource.getCategories().map { it.toSection() }

    override suspend fun createClub(form: ClubFormData): Int? =
        dataSource.createClub(
            request = form.toRequest(),
            logo = form.logo,
            cover = form.cover
        ).id

    override suspend fun requestVerify(clubId: Int) {
        dataSource.requestVerify(clubId)
    }

    override suspend fun editClub(clubId: Int, form: ClubFormData) {
        dataSource.editClub(
            clubId = clubId,
            request = form.toRequest(),
            logo = form.logo,
            cover = form.cover
        )
    }

    override suspend fun joinClub(clubId: Int) {
        dataSource.joinClub(clubId)
    }

    override suspend fun exitClub(clubId: Int) {
        dataSource.exitClub(clubId)
    }

    override suspend fun assignRole(
        clubId: Int,
        userId: String,
        role: AppUIEntities.UserActivityRole
    ) {
        dataSource.assignRole(
            clubId = clubId,
            request = RoleAssignRequest(userId = userId, role = role.toApiString())
        )
    }

    override suspend fun clubHasVicePresident(clubId: Int): Boolean =
        dataSource.getClubMembers(clubId).content.any {
            it.role?.uppercase() in setOf("VICE_PRESIDENT", "VICE_PRESIDENT")
        }

    override suspend fun fetchClubMembers(clubId: Int): GroupedMembersData {
        val users = dataSource.getClubMembers(clubId, page = 0, size = 10)
            .content.map { it.toCellModel() }
        return GroupedMembersData.from(users)
    }

    override suspend fun fetchClubMembersPage(clubId: Int, page: Int, size: Int, keyword: String?): MembersPage {
        val response = dataSource.getClubMembers(clubId, page, size, keyword)
        return MembersPage(
            members = response.content.map { it.toCellModel() },
            hasMore = response.hasMore,
            totalCount = response.totalElements
        )
    }

    private fun ClubMemberResponse.ClubMember.toCellModel() = MemberCellModel(
        id = userId ?: "-",
        name = fullName ?: "-",
        avatarUrl = profileUrl,
        subtitle = listOfNotNull(degree, specialization, entryYear?.toString()).joinToString(", "),
        role = role?.toActivityRole() ?: AppUIEntities.UserActivityRole.MEMBER
    )

    private fun AppUIEntities.UserActivityRole.toApiString(): String = when (this) {
        AppUIEntities.UserActivityRole.MEMBER -> "MEMBER"
        AppUIEntities.UserActivityRole.PRESIDENT -> "PRESIDENT"
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> "VICE_PRESIDENT"
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> "EVENT_CREATOR"
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
    }

    private fun ClubFormData.toRequest() = ClubCreateRequest(
        communityId = defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0),
        name = name,
        about = about,
        location = location,
        ownerContact = ownerContact,
        capacity = capacity,
        rule = rules.ifBlank { null },
        visibility = if (isPublic) "PUBLIC" else "PRIVATE",
        backgroundColour = background.toRequestString(),
        categoryIds = categoryIds,
        links = links.map { ClubLinkRequest(type = it.type, name = it.name, url = it.url) }
    )

    private fun CategorySectionResponse.toSection() = CategorySection(
        type = type ?: "",
        title = title ?: "",
        categories = subCategories.map {
            CategoriesChipModel(id = it.id ?: 0, title = it.title ?: "", selected = false)
        }
    )

    private fun AppUIEntities.BackgroundType.toRequestString(): String = apiValue

    private fun ClubListResponse.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "",
        communityName = communityName ?: "",
        logoURL = clubProfile ?: "",
        memberCount = memberCount ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "",
        members = members.map {
            AppUIEntities.Member(id = it.id?.hashCode() ?: 0, profileImage = it.url)
        },
        bgType = background.toBackgroundType(),
        accessType = AppUIEntities.AccessType.fromApi(visibility),
        requestType = requestStatus.toRequestType(),
        role = role?.let { it.toActivityRole() },
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title },
        isVerified = AppUIEntities.ClubStatus.from(clubStatus)?.isVerified == true
    )

    private fun ClubDetailResponse.toUIModel() = ClubsDetails.UIModel(
        name = name,
        communityName = communityName,
        membersCount = membersCount ?: 0,
        logo = logoUrl,
        coverImage = backgroundUrl,
        coverColorType = backgroundColour.toBackgroundType(),
        userActivityType = clubUserRole.toActivityRole(),
        accessType = AppUIEntities.AccessType.fromApi(visibility),
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        infoData = buildInfoData(this),
        eventsData = emptyList(),
        editPrefillData = toEditPrefill(),
        joinButton = toJoinButton(),
        eventsCount = eventCount,
        clubsCount = clubCount,
        clubStatus = AppUIEntities.ClubStatus.from(clubStatus)
    )

    /**
     * Bottom join/request button. Mirrors iOS `ClubRepo.mapButtonModel`: hidden once the
     * viewer is accepted, a disabled "Request sent" while pending, and "Request" after a
     * rejection. Driven by `clubUserStatus` — keying it off the role alone (what Android
     * did) made a pending requester see an enabled Join button again.
     */
    private fun ClubDetailResponse.toJoinButton(): ClubsDetails.JoinButton? {
        val request = clubUserStatus.toRequestType()
        if (request == AppUIEntities.RequestType.JOINED) return null
        if (clubUserRole.toActivityRole() != AppUIEntities.UserActivityRole.NOT_JOINED) return null
        if (request == AppUIEntities.RequestType.PENDING) {
            return ClubsDetails.JoinButton(
                title = LanguageManager.string(R.string.clubs_join_request_sent),
                disabled = true
            )
        }
        val isPublic = AppUIEntities.AccessType.fromApi(visibility) == AppUIEntities.AccessType.PUBLIC
        val title = if (request == AppUIEntities.RequestType.REJECTED || !isPublic) {
            LanguageManager.string(R.string.clubs_request)
        } else {
            LanguageManager.string(R.string.clubs_join)
        }
        return ClubsDetails.JoinButton(title = title, disabled = false)
    }

    /** Builds the edit-screen pre-fill (form values + image URLs). Mirrors iOS `mapPrefilData`. */
    private fun ClubDetailResponse.toEditPrefill() = ClubsDetails.ClubEditPrefill(
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
                links.map { AppFieldSchema.LinkItem(type = it.type, name = it.name, url = it.url) }
            ),
            AppFieldSchema.FieldId.LOCATION to AppFieldSchema.FieldValue.TextValue(location ?: ""),
            AppFieldSchema.FieldId.RULES to AppFieldSchema.FieldValue.TextValue(rule ?: ""),
            AppFieldSchema.FieldId.ABOUT to AppFieldSchema.FieldValue.TextValue(about)
        )
    )

    private fun buildInfoData(detail: ClubDetailResponse): List<ClubsDetails.Info> = buildList {
        if (detail.about.isNotBlank()) {
            add(ClubsDetails.Info(
                title = LanguageManager.string(R.string.clubs_about_label),
                subItems = listOf(ClubsDetails.SubInfo(title = null, description = detail.about))
            ))
        }
        add(ClubsDetails.Info(
            // This is a club, not an event — iOS relabelled these 2026-08-17.
            title = LanguageManager.string(R.string.clubs_info_section),
            subItems = buildList {
                detail.modifiedAt?.let { add(ClubsDetails.SubInfo(title = LanguageManager.string(DesignR.string.created_updated_date), description = it)) }
                detail.ownerContact?.let {
                    add(
                        ClubsDetails.SubInfo(
                            title = LanguageManager.string(R.string.clubs_owner_contact_label),
                            description = it,
                            phoneNumber = it.dialablePhone()
                        )
                    )
                }
                detail.capacity?.let { add(ClubsDetails.SubInfo(title = LanguageManager.string(R.string.clubs_capacity_label), description = memberOfCapacityText(detail.membersCount ?: 0, it))) }
                detail.rule?.let { add(ClubsDetails.SubInfo(title = LanguageManager.string(R.string.clubs_rules_label), description = it)) }
                detail.location?.let { add(ClubsDetails.SubInfo(title = LanguageManager.string(R.string.clubs_location_label), description = it)) }
            }
        ))
        if (detail.links.isNotEmpty()) {
            add(ClubsDetails.Info(
                title = LanguageManager.string(R.string.clubs_row_links),
                subItems = detail.links.map { ClubsDetails.SubInfo(title = it.name, description = it.url, isLink = true) }
            ))
        }
    }

    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType = AppUIEntities.BackgroundType.fromApi(this)

    private fun String?.toActivityRole(): AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.fromApi(this)

    private fun String?.toRequestType(): AppUIEntities.RequestType = AppUIEntities.RequestType.fromApi(this)
}
