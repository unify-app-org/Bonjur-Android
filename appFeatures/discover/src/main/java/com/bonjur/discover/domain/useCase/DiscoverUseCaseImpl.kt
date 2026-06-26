package com.bonjur.discover.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.commonModel.toUserActivityRole
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.discover.data.DTOs.DiscoverClub
import com.bonjur.discover.data.DTOs.DiscoverCommunity
import com.bonjur.discover.data.DTOs.DiscoverEvent
import com.bonjur.discover.data.DTOs.DiscoverHangout
import com.bonjur.discover.data.DTOs.DiscoverMember
import com.bonjur.discover.data.DTOs.JoinHangoutRequest
import com.bonjur.discover.data.dataSources.DiscoverDataSource
import com.bonjur.discover.domain.models.UserModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.network.manager.TokenManager
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class DiscoverUseCaseImpl @Inject constructor(
    val dataSource: DiscoverDataSource,
    val tokenManager: TokenManager,
    val defaultStorage: DefaultStorage
) : DiscoverUseCase {

    private fun paginationQuery(
        page: Int,
        size: Int,
        categoryIds: List<Int>
    ): MutableMap<String, String> {
        val query = mutableMapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )
        if (categoryIds.isNotEmpty()) {
            query["categoryIds"] = categoryIds.joinToString(",")
        }
        return query
    }

    override suspend fun fetchUserData(): UserModel {
        val userId = tokenManager.getUserId() ?: return UserModel(
            id = 0, name = "", profileImage = null, greeting = ""
        )
        val data = dataSource.getUserById(userId)
        return UserModel(
            id = 0,
            name = data.fullName ?: "-",
            profileImage = data.fileUrl ?: data.profileUrl,
            greeting = data.greeting ?: ""
        )
    }

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

    override suspend fun fetchCommunitiesData(
        page: Int,
        size: Int,
        categoryIds: List<Int>
    ): List<CommunityCardModel> {
        return dataSource.getCommunities(paginationQuery(page, size, categoryIds))
            .map { it.toCardModel() }
    }

    override suspend fun fetchClubsData(
        page: Int,
        size: Int,
        categoryIds: List<Int>
    ): List<ClubCardModel> {
        val query = paginationQuery(page, size, categoryIds)
        query["parentId"] = defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0).toString()
        return dataSource.getClubs(query).map { it.toCardModel() }
    }

    override suspend fun fetchEvents(
        page: Int,
        size: Int,
        categoryIds: List<Int>
    ): List<EventsCardModel> {
        return dataSource.getEvents(paginationQuery(page, size, categoryIds))
            .map { it.toCardModel() }
    }

    override suspend fun fetchHangoutsData(
        page: Int,
        size: Int,
        categoryIds: List<Int>
    ): List<HangoutsCardModel> {
        return dataSource.getHangouts(paginationQuery(page, size, categoryIds))
            .map { it.toCardModel() }
    }

    override suspend fun joinHangout(hangoutId: String) {
        dataSource.joinHangout(JoinHangoutRequest(hangoutId = hangoutId))
    }

    // MARK: - Mappers

    private fun DiscoverHangout.toCardModel(): HangoutsCardModel {
        val parts = hangoutDate.toDateParts()
        return HangoutsCardModel(
            id = id ?: "",
            name = name ?: "-",
            description = about ?: "-",
            memberCount = membersCount ?: 0,
            totalCapacity = capacity,
            tags = categoryResponses.map {
                AppUIEntities.Tags(id = it.id ?: 0, type = "CATEGORY", title = it.title ?: "-")
            },
            accessType = visibility.toAccessType(),
            requestType = requestStatus.toRequestType(),
            dateDay = parts?.day,
            dateMonth = parts?.month,
            time = parts?.time,
            location = location
        )
    }

    private fun DiscoverEvent.toCardModel(): EventsCardModel {
        val parts = eventDate.toDateParts()
        return EventsCardModel(
            id = id ?: "",
            name = name ?: "-",
            coverImageURL = background,
            memberCount = membersCount ?: 0,
            totalCapacity = capacity,
            club = EventsCardModel.Club(
                name = club?.name ?: "-",
                id = club?.id ?: 0
            ),
            tags = categoryResponses.map {
                AppUIEntities.Tags(id = it.id ?: 0, type = "", title = it.title ?: "-")
            },
            bgType = AppUIEntities.BackgroundType.Primary,
            requestType = requestStatus.toRequestType(),
            accessType = visibility.toAccessType(),
            // Real event date/location replace the model's hardcoded placeholder defaults.
            dateDay = parts?.day ?: "",
            dateMonth = parts?.month ?: "",
            time = parts?.time ?: "",
            location = location ?: ""
        )
    }

    private fun DiscoverClub.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "-",
        communityName = communityName ?: "-",
        logoURL = clubProfile ?: "",
        memberCount = memberCount ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "-",
        members = members.toMembers(),
        bgType = background.toBackgroundType(),
        accessType = visibility.toAccessType(),
        requestType = requestStatus.toRequestType(),
        role = role?.toUserActivityRole(),
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title },
        isVerified = AppUIEntities.ClubStatus.from(clubStatus)?.isVerified == true
    )

    private fun DiscoverCommunity.toCardModel() = CommunityCardModel(
        id = id ?: 0,
        name = name ?: "-",
        subTitle = "Community",
        logoURL = profile ?: "",
        memberCount = membersCount ?: 0,
        members = members.toMembers(),
        bgType = background.toBackgroundType(),
        clubsCount = clubCount ?: 0
    )

    private fun List<DiscoverMember>?.toMembers(): List<AppUIEntities.Member> =
        this?.mapIndexed { index, member ->
            AppUIEntities.Member(id = index, profileImage = member.url)
        } ?: emptyList()

    private fun String?.toAccessType(): AppUIEntities.AccessType =
        if (this?.uppercase() == "PUBLIC") AppUIEntities.AccessType.PUBLIC
        else AppUIEntities.AccessType.PRIVATE

    private fun String?.toRequestType(): AppUIEntities.RequestType = when (this?.uppercase()) {
        "JOINED", "ACCEPTED" -> AppUIEntities.RequestType.JOINED
        "PENDING" -> AppUIEntities.RequestType.PENDING
        "REJECTED" -> AppUIEntities.RequestType.REJECTED
        else -> AppUIEntities.RequestType.NONE
    }

    // MARK: - Date helpers (mirrors HangoutsUseCaseImpl; card date badge: day / MMM / HH:mm)

    private data class DateParts(val day: String, val month: String, val time: String)

    private fun String?.toDateParts(): DateParts? {
        val date = parseIso(this) ?: return null
        val local = { fmt: String ->
            SimpleDateFormat(fmt, Locale.US).apply { timeZone = TimeZone.getDefault() }.format(date)
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
