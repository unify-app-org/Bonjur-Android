package com.bonjur.clubs.domain.useCase

import com.bonjur.clubs.data.DTOs.CategorySectionResponse
import com.bonjur.clubs.data.DTOs.ClubCreateRequest
import com.bonjur.clubs.data.DTOs.ClubDetailResponse
import com.bonjur.clubs.data.DTOs.ClubLinkRequest
import com.bonjur.clubs.data.DTOs.ClubListResponse
import com.bonjur.clubs.data.DTOs.RoleAssignRequest
import com.bonjur.clubs.data.dataSource.ClubsDataSource
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import javax.inject.Inject

class ClubsUseCaseImpl @Inject constructor(
    val dataSource: ClubsDataSource,
    private val defaultStorage: DefaultStorage
) : ClubsUseCase {

    override suspend fun fetchClubsData(
        size: Int,
        name: String?,
        categoryIds: List<Int>
    ): List<ClubCardModel> {
        return dataSource.getClubs(buildClubsQuery(size, name, categoryIds))
            .map { it.toCardModel() }
    }

    /**
     * Mirrors iOS `ClubsViewModel.makeQuery` + `ClubRepo.fetchClubs`:
     * page is always 0, size grows for "load more", optional name search,
     * parentId scopes to the active community, optional categoryIds for filters.
     * Built with mutableMapOf (not buildMap) to avoid `size` shadowing the map.
     */
    private fun buildClubsQuery(
        size: Int,
        name: String?,
        categoryIds: List<Int>
    ): Map<String, String> {
        val query = mutableMapOf(
            "page" to "0",
            "size" to size.toString(),
            "parentId" to defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0).toString()
        )
        name?.trim()?.takeIf { it.isNotEmpty() }?.let { query["name"] = it }
        if (categoryIds.isNotEmpty()) {
            query["categoryIds"] = categoryIds.joinToString(",")
        }
        return query
    }

    /** Real category fetch -> filter sections, mock fallback. Mirrors Discover `fetchFilterData`. */
    override suspend fun fetchFilterData(): List<FilterView.Model> {
        return try {
            dataSource.getCategories().map { section ->
                FilterView.Model(
                    title = section.title ?: "",
                    type = section.type ?: "",
                    items = section.subCategories.map { sub ->
                        FilterView.Items(title = sub.title ?: "", id = sub.id ?: 0)
                    }
                )
            }
        } catch (e: Exception) {
            FilterViewMocks.mockData
        }
    }

    override suspend fun fetchClubsDetails(clubId: Int): ClubsDetails.UIModel {
        return dataSource.getClubById(clubId).toUIModel()
    }

    override suspend fun getCategories(): List<CategorySection> =
        dataSource.getCategories().map { it.toSection() }

    override suspend fun createClub(form: ClubFormData) {
        dataSource.createClub(
            request = form.toRequest(),
            logo = form.logo,
            cover = form.cover
        )
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
            it.role?.uppercase() in setOf("VISE_PRESIDENT", "VICE_PRESIDENT")
        }

    private fun AppUIEntities.UserActivityRole.toApiString(): String = when (this) {
        AppUIEntities.UserActivityRole.MEMBER -> "MEMBER"
        AppUIEntities.UserActivityRole.PRESIDENT -> "PRESIDENT"
        AppUIEntities.UserActivityRole.VISE_PRESIDENT -> "VISE_PRESIDENT"
        AppUIEntities.UserActivityRole.EVENT_CREATOR -> "EVENT_CREATOR"
        AppUIEntities.UserActivityRole.NOT_JOINED -> ""
    }

    private fun ClubFormData.toRequest() = ClubCreateRequest(
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

    private fun AppUIEntities.BackgroundType.toRequestString(): String = when (this) {
        is AppUIEntities.BackgroundType.Primary -> "PRIMARY"
        is AppUIEntities.BackgroundType.Secondary -> "SECONDARY"
        is AppUIEntities.BackgroundType.Tertiary -> "TERTIARY"
        is AppUIEntities.BackgroundType.CustomColor -> when (colorType) {
            is AppUIEntities.ColorType.Orange -> "ORANGE"
            is AppUIEntities.ColorType.Red -> "RED"
            is AppUIEntities.ColorType.Pink -> "PURPLE"
            is AppUIEntities.ColorType.Custom -> "PRIMARY"
        }
    }

    private fun ClubListResponse.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "",
        communityName = communityName ?: "",
        logoURL = clubProfile ?: "",
        memberCount = membersCount ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "",
        members = members.map {
            AppUIEntities.Member(id = it.id?.hashCode() ?: 0, profileImage = it.url)
        },
        bgType = background.toBackgroundType(),
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        requestType = if (joined == true) AppUIEntities.RequestType.JOINED else AppUIEntities.RequestType.NONE,
        role = clubUserRole?.let { it.toActivityRole() },
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title }
    )

    private fun ClubDetailResponse.toUIModel() = ClubsDetails.UIModel(
        name = name,
        communityName = communityName,
        membersCount = membersCount ?: 0,
        logo = logoUrl,
        coverImage = backgroundUrl,
        coverColorType = backgroundColour.toBackgroundType(),
        userActivityType = clubUserRole.toActivityRole(),
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        infoData = buildInfoData(this),
        eventsData = emptyList(),
        editPrefillData = toEditPrefill()
    )

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
                title = "About",
                subItems = listOf(ClubsDetails.SubInfo(title = null, description = detail.about))
            ))
        }
        add(ClubsDetails.Info(
            title = "Event info",
            subItems = buildList {
                detail.modifiedAt?.let { add(ClubsDetails.SubInfo(title = "Updated", description = it)) }
                detail.ownerContact?.let { add(ClubsDetails.SubInfo(title = "Owner contact", description = it)) }
                detail.capacity?.let { add(ClubsDetails.SubInfo(title = "Capacity", description = "${detail.membersCount ?: 0}/$it members")) }
                detail.rule?.let { add(ClubsDetails.SubInfo(title = "Rules", description = it)) }
                detail.location?.let { add(ClubsDetails.SubInfo(title = "Location", description = it)) }
            }
        ))
        if (detail.links.isNotEmpty()) {
            add(ClubsDetails.Info(
                title = "Links",
                subItems = detail.links.map { ClubsDetails.SubInfo(title = it.name, description = it.url, isLink = true) }
            ))
        }
    }

    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType = when (this?.uppercase()) {
        "SECONDARY" -> AppUIEntities.BackgroundType.Secondary
        "GREEN" -> AppUIEntities.BackgroundType.Primary
        "BLUE" -> AppUIEntities.BackgroundType.Secondary
        "RED" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Red)
        "ORANGE" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Orange)
        "PURPLE" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Pink)
        else -> AppUIEntities.BackgroundType.Primary
    }

    private fun String?.toActivityRole(): AppUIEntities.UserActivityRole = when (this?.uppercase()) {
        "MEMBER" -> AppUIEntities.UserActivityRole.MEMBER
        "PRESIDENT" -> AppUIEntities.UserActivityRole.PRESIDENT
        "VISE_PRESIDENT", "VICE_PRESIDENT" -> AppUIEntities.UserActivityRole.VISE_PRESIDENT
        "EVENT_CREATOR" -> AppUIEntities.UserActivityRole.EVENT_CREATOR
        else -> AppUIEntities.UserActivityRole.NOT_JOINED
    }
}
