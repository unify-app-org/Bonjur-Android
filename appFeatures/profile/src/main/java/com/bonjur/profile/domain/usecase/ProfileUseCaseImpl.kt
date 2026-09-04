package com.bonjur.profile.domain.usecase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.network.APIClient.MultipartFile
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.data.DTOs.MyClubResponse
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.data.DTOs.UserProfileResponse
import com.bonjur.profile.data.dataSource.ProfileDataSource
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.detail.models.UserCardModel
import com.bonjur.profile.presentation.editProfile.models.Gender
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import javax.inject.Inject
import com.bonjur.network.model.Page
import com.bonjur.network.model.toPage

class ProfileUseCaseImpl @Inject constructor(
    val dataSource: ProfileDataSource,
    val tokenManager: TokenManager,
    val defaultStorage: DefaultStorage
) : ProfileUseCase {

    // ── Profile (always by id; own id from token for self — mirrors iOS) ───────

    override suspend fun fetchProfileData(userId: String?, communityId: Int?): ProfileDetail.UIModel {
        val id = userId ?: tokenManager.getUserId().orEmpty()
        return dataSource.getUserById(id, communityId ?: storedCommunityId()).toUIModel()
    }

    /** The community picked at login. Used for every context except a community detail,
     *  which knows which community the profile is being viewed inside. */
    private fun storedCommunityId(): Int =
        defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0)

    /** Maps the user response to the detail UI model. Field choices mirror iOS ProfileRepo. */
    private fun UserProfileResponse.toUIModel(): ProfileDetail.UIModel =
        ProfileDetail.UIModel(
            userCardModel = UserCardModel(
                // Nullable on purpose: the user can pick "Default" (the plain white
                // card), and `fromApi` folds an unknown/absent value into Primary.
                // Club/hangout covers below are non-null by contract and keep it.
                backgroundCover = background?.let { AppUIEntities.BackgroundType.fromApi(it) },
                nameSurname = username ?: fullName ?: "-",
                speciality = specialization ?: "-",
                course = faculty ?: "-",
                community = communityName ?: "-",
                degree = degree ?: "-",
                entryYear = entryYear?.toString() ?: "-",
                email = mail ?: "",
                imageUrl = fileUrl
            ),
            about = about,
            gender = gender?.let { Gender.from(it)?.displayName ?: it },
            birthday = birthDate,
            languages = languages.map {
                SelectableListItemModel(id = it.id, title = it.name ?: "-", selected = false)
            },
            tags = categories.map {
                AppUIEntities.Tags(id = it.id, type = "", title = it.title)
            }
        )

    // ── My clubs / events / activities (parallel-fetched by ProfileDetail VM) ──

    override suspend fun getMyClubs(userId: String?, page: Int, size: Int): Page<ClubCardModel> {
        val id = userId ?: tokenManager.getUserId() ?: return Page.empty()
        val response = dataSource.getMyClubs(id, page, size)
        return response.toPage(page, size, response.content.map { it.toCardModel() })
    }

    private fun MyClubResponse.toCardModel(): ClubCardModel = ClubCardModel(
        id = id ?: 0,
        name = name ?: "-",
        communityName = communityName ?: "-",
        logoURL = clubProfile ?: "",
        memberCount = memberCount ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "-",
        members = members.map {
            AppUIEntities.Member(id = it.id?.hashCode() ?: 0, profileImage = it.url)
        },
        bgType = background.toBackgroundType(),
        accessType = visibility.toAccessType(),
        requestType = requestStatus.toRequestType(),
        role = role?.toActivityRole(),
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title },
        isVerified = AppUIEntities.ClubStatus.from(clubStatus)?.isVerified == true
    )

    override suspend fun getMyEvents(page: Int, size: Int): Page<EventsCardModel> {
        val response = dataSource.getMyEvents(page, size)
        val items = response.content.map { item ->
            val parts = item.eventDate.toDateParts()
            EventsCardModel(
                id = item.id ?: "-",
                name = item.name ?: "-",
                coverImageURL = item.background,
                memberCount = item.membersCount ?: 0,
                totalCapacity = item.capacity,
                club = EventsCardModel.Club(
                    name = item.club?.name ?: "-",
                    id = item.club?.id ?: 0
                ),
                tags = item.categoryResponses.map {
                    AppUIEntities.Tags(id = it.id, type = "", title = it.title)
                },
                bgType = AppUIEntities.BackgroundType.Primary,
                requestType = item.requestStatus.toRequestType(),
                accessType = item.visibility.toAccessType(),
                time = parts.time,
                location = item.location ?: "-",
                dateDay = parts.day,
                dateMonth = parts.month
            )
        }
        return response.toPage(page, size, items)
    }

    override suspend fun getMyHangouts(userId: String?, page: Int, size: Int): Page<HangoutsCardModel> {
        val id = userId ?: tokenManager.getUserId() ?: return Page.empty()
        val response = dataSource.getMyHangouts(id, page, size)
        val items = response.content.map { item ->
            val parts = item.hangoutDate.toDateParts()
            HangoutsCardModel(
                id = item.id ?: "-",
                name = item.name ?: "-",
                description = item.about ?: "-",
                memberCount = item.membersCount ?: 0,
                totalCapacity = item.capacity,
                tags = item.categories.map {
                    AppUIEntities.Tags(id = it.id, type = "", title = it.title)
                },
                accessType = item.visibility.toAccessType(),
                requestType = item.status.toRequestType(),
                dateDay = parts.day,
                dateMonth = parts.month,
                time = parts.time,
                location = item.location
            )
        }
        return response.toPage(page, size, items)
    }

    // ── Date display helpers (device-local; mirrors EventsUseCaseImpl) ─────────

    private data class DateParts(val day: String, val month: String, val time: String)

    private fun String?.toDateParts(): DateParts {
        val date = parseIso(this) ?: return DateParts("-", "-", "-")
        val local = { fmt: String ->
            SimpleDateFormat(fmt, Locale.US).apply {
                timeZone = TimeZone.getDefault()
            }.format(date)
        }
        return DateParts(day = local("d"), month = local("MMM").uppercase(), time = local("HH:mm"))
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

    // ── Edit option lists (categories / languages) ────────────────────────────

    override suspend fun getCategories(): List<CategorySection> =
        dataSource.getCategories().map { section ->
            CategorySection(
                type = section.type ?: "",
                title = section.title ?: "",
                categories = section.subCategories.map {
                    CategoriesChipModel(id = it.id ?: 0, title = it.title ?: "", selected = false)
                }
            )
        }

    override suspend fun getLanguages(): List<SelectableListItemModel> =
        dataSource.getLanguages().map {
            SelectableListItemModel(
                id = it.id,
                title = it.name ?: "",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            )
        }

    // ── Edit (PUT /users — fields as query, avatar as multipart; mirrors iOS) ──

    override suspend fun editProfile(request: ProfileUpdateRequest, imageBytes: ByteArray?) {
        // iOS sends the update fields as query params (ProfileDTOModel.UpdateRequest.toDictionary).
        // ⚠️ Array encoding (categoriesId/languagesId) as CSV is unverified against the backend.
        val fields = buildMap {
            request.birthDate?.let { put("birthDate", it) }
            request.gender?.let { put("gender", it) }
            request.about?.let { put("about", it) }
            if (request.categoriesId.isNotEmpty()) {
                put("categoriesId", request.categoriesId.joinToString(","))
            }
            if (request.languagesId.isNotEmpty()) {
                put("languagesId", request.languagesId.joinToString(","))
            }
            request.backgroundColour?.let { put("backgroundColour", it) }
        }
        // ⚠️ Avatar multipart part name ("file") unverified — confirm against backend.
        val image = imageBytes?.let {
            MultipartFile(name = "file", fileName = "avatar.jpg", mimeType = "image/jpeg", bytes = it)
        }
        dataSource.updateProfile(fields, image)
    }

    // ── Settings ──────────────────────────────────────────────────────────────

    override suspend fun deleteAccount() {
        dataSource.deleteAccount()
    }

    // ── Mappers (mirror the per-feature helpers in Clubs/Events use cases) ─────

    private fun String?.toAccessType(): AppUIEntities.AccessType =
        AppUIEntities.AccessType.fromApi(this)

    private fun String?.toRequestType(): AppUIEntities.RequestType = AppUIEntities.RequestType.fromApi(this)

    private fun String?.toActivityRole(): AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.fromApi(this)

    // Backend colour enum (iOS BackgroundType raw values) → UI type.
    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType = AppUIEntities.BackgroundType.fromApi(this)
}
