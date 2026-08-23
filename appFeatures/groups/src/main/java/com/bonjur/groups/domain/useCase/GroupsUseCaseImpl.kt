package com.bonjur.groups.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.commonModel.toUserActivityRole
import com.bonjur.events.data.DTOs.EventListResponse
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.data.DTOs.GroupsClubResponse
import com.bonjur.groups.data.DTOs.GroupsHangoutResponse
import com.bonjur.groups.data.dataSource.GroupsDataSource
import com.bonjur.groups.data.models.GroupsPaginationQuery
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class GroupsUseCaseImpl @Inject constructor(
    val dataSource: GroupsDataSource
) : GroupsUseCase {

    override suspend fun fetchClubs(query: GroupsPaginationQuery): List<ClubCardModel> {
        return dataSource.fetchJoinedClubs(query.toMap()).map { it.toCardModel() }
    }

    // Mirrors iOS: joined events are fetched once at a fixed page/size (no pagination),
    // with the optional search keyword forwarded to the server.
    override suspend fun fetchEvents(keyword: String?): List<EventsCardModel> {
        val query = GroupsPaginationQuery(page = 0, size = 50, keyword = keyword)
        return dataSource.fetchJoinedEvents(query.toMap()).map { it.toCardModel() }
    }

    override suspend fun fetchHangouts(query: GroupsPaginationQuery): List<HangoutsCardModel> {
        return dataSource.fetchJoinedHangouts(query.toMap()).map { it.toCardModel() }
    }

    private fun GroupsClubResponse.toCardModel() = ClubCardModel(
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
        accessType = visibility.toAccessType(),
        requestType = requestStatus.toRequestType(),
        role = role?.toUserActivityRole(),
        upcomingEventsCount = eventCount ?: 0,
        categories = categoryResponses.map { it.title },
        isVerified = AppUIEntities.ClubStatus.from(clubStatus)?.isVerified == true
    )

    private fun EventListResponse.toCardModel(): EventsCardModel {
        // EventsCardModel still defaults date/time/location to placeholders ("14 JUN",
        // "18:00", "Campus, Room 204"). Leaving them unset here is what made every joined
        // event render 14 June — map the real payload like EventsUseCaseImpl does.
        val parts = eventDate.toDateParts()
        return EventsCardModel(
            id = id ?: "",
            name = name ?: "",
            coverImageURL = background,
            memberCount = membersCount ?: 0,
            totalCapacity = capacity,
            club = EventsCardModel.Club(
                name = club?.name ?: "",
                id = club?.id ?: 0
            ),
            tags = categoryResponses.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
            bgType = AppUIEntities.BackgroundType.Primary,
            requestType = requestStatus.toRequestType(),
            accessType = visibility.toAccessType(),
            time = parts?.time ?: "-",
            location = location ?: "-",
            dateDay = parts?.day ?: "-",
            dateMonth = parts?.month ?: "-"
        )
    }

    private fun GroupsHangoutResponse.toCardModel(): HangoutsCardModel {
        val parts = hangoutDate.toDateParts()
        return HangoutsCardModel(
            id = id ?: "",
            name = name ?: "",
            description = about ?: "",
            memberCount = membersCount ?: 0,
            totalCapacity = capacity,
            tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
            accessType = visibility.toAccessType(),
            requestType = status.toRequestType(),
            dateDay = parts?.day,
            dateMonth = parts?.month,
            time = parts?.time,
            location = location
        )
    }

    private fun String?.toAccessType(): AppUIEntities.AccessType =
        AppUIEntities.AccessType.fromApi(this)

    private fun String?.toRequestType(): AppUIEntities.RequestType =
        AppUIEntities.RequestType.fromApi(this)

    private fun String?.toBackgroundType(): AppUIEntities.BackgroundType =
        AppUIEntities.BackgroundType.fromApi(this)

    // MARK: - Date display helpers
    // Same shape as EventsUseCaseImpl / HangoutsUseCaseImpl: parse the backend's UTC ISO
    // stamp, render the badge parts in device-local time.

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
}
