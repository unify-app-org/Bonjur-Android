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
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    val dataSource: ProfileDataSource,
    val tokenManager: TokenManager
) : ProfileUseCase {

    // ── Profile (always by id; own id from token for self — mirrors iOS) ───────

    override suspend fun fetchProfileData(userId: String?): ProfileDetail.UIModel {
        val id = userId ?: tokenManager.getUserId().orEmpty()
        return dataSource.getUserById(id).toUIModel()
    }

    /** Maps the user response to the detail UI model. Field choices mirror iOS ProfileRepo. */
    private fun UserProfileResponse.toUIModel(): ProfileDetail.UIModel =
        ProfileDetail.UIModel(
            userCardModel = UserCardModel(
                backgroundCover = background.toBackgroundType(),
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

    override suspend fun getMyClubs(userId: String?): List<ClubCardModel> {
        val id = userId ?: tokenManager.getUserId() ?: return emptyList()
        return dataSource.getMyClubs(id).content.map { it.toCardModel() }
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

    override suspend fun getMyEvents(): List<EventsCardModel> =
        dataSource.getMyEvents().content.map { item ->
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

    override suspend fun getMyHangouts(userId: String?): List<HangoutsCardModel> {
        val id = userId ?: tokenManager.getUserId() ?: return emptyList()
        return dataSource.getMyHangouts(id).content.map { item ->
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
        "VICE_PRESIDENT" -> AppUIEntities.UserActivityRole.VISE_PRESIDENT
        "EVENT_CREATOR" -> AppUIEntities.UserActivityRole.EVENT_CREATOR
        else -> AppUIEntities.UserActivityRole.NOT_JOINED
    }

    // Backend colour enum (iOS BackgroundType raw values) → UI type.
    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType = when (this?.uppercase()) {
        "GREEN" -> AppUIEntities.BackgroundType.Primary
        "BLUE" -> AppUIEntities.BackgroundType.Secondary
        "PURPLE" -> AppUIEntities.BackgroundType.Tertiary
        "RED" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Red)
        "ORANGE" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Orange)
        "PINK" -> AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Pink)
        else -> AppUIEntities.BackgroundType.Primary
    }
}
