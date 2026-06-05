package com.bonjur.discover.domain.useCase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.discover.data.DTOs.DiscoverClub
import com.bonjur.discover.data.DTOs.DiscoverCommunity
import com.bonjur.discover.data.DTOs.DiscoverHangout
import com.bonjur.discover.data.DTOs.DiscoverMember
import com.bonjur.discover.data.DTOs.JoinHangoutRequest
import com.bonjur.discover.data.dataSources.DiscoverDataSource
import com.bonjur.discover.domain.models.UserModel
import com.bonjur.events.presentation.list.models.EventsCardMocks
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.network.manager.TokenManager
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import javax.inject.Inject

class DiscoverUseCaseImpl @Inject constructor(
    val dataSource: DiscoverDataSource,
    val tokenManager: TokenManager,
    val defaultStorage: DefaultStorage
) : DiscoverUseCase {

    private val defaultQuery: Map<String, String>
        get() = mapOf("page" to "0", "size" to "10")

    override suspend fun fetchUserData(): UserModel {
        val userId = tokenManager.getUserId() ?: return UserModel(
            id = 0, name = "", profileImage = null, greeting = ""
        )
        val data = dataSource.getUserById(userId)
        return UserModel(
            id = 0,
            name = data.fullName ?: "-",
            profileImage = data.profileUrl,
            greeting = ""
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

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> {
        return dataSource.getCommunities(defaultQuery).map { it.toCardModel() }
    }

    override suspend fun fetchClubsData(categoryIds: List<Int>): List<ClubCardModel> {
        val query = defaultQuery.toMutableMap()
        query["parentId"] = defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0).toString()
        if (categoryIds.isNotEmpty()) query["categoryIds"] = categoryIds.joinToString(",")
        return dataSource.getClubs(query).map { it.toCardModel() }
    }

    override suspend fun fetchEvents(): List<EventsCardModel> = EventsCardMocks.previewMock

    override suspend fun fetchHangoutsData(categoryIds: List<Int>): List<HangoutsCardModel> {
        val query = defaultQuery.toMutableMap()
        if (categoryIds.isNotEmpty()) query["categoryIds"] = categoryIds.joinToString(",")
        return dataSource.getHangouts(query).map { it.toCardModel() }
    }

    override suspend fun joinHangout(hangoutId: String) {
        dataSource.joinHangout(JoinHangoutRequest(hangoutId = hangoutId))
    }

    // MARK: - Mappers

    private fun DiscoverHangout.toCardModel() = HangoutsCardModel(
        id = id ?: "",
        name = name ?: "-",
        description = about ?: "-",
        memberCount = membersCount ?: 0,
        totalCapacity = capacity,
        tags = categoryResponses.map {
            AppUIEntities.Tags(id = it.id ?: 0, type = "CATEGORY", title = it.title ?: "-")
        },
        accessType = visibility.toAccessType(),
        requestType = requestStatus.toRequestType()
    )

    private fun DiscoverClub.toCardModel() = ClubCardModel(
        id = id ?: 0,
        name = name ?: "-",
        communityName = communityName ?: "-",
        logoURL = clubProfile ?: "",
        memberCount = count ?: 0,
        totalCapacity = capacity ?: 0,
        community = communityName ?: "-",
        members = members.toMembers(),
        bgType = background.toBackgroundType(),
        accessType = visibility.toAccessType(),
        requestType = if (joined == true) AppUIEntities.RequestType.JOINED else AppUIEntities.RequestType.NONE
    )

    private fun DiscoverCommunity.toCardModel() = CommunityCardModel(
        id = id ?: 0,
        name = name ?: "-",
        subTitle = "Community",
        logoURL = profile ?: "",
        memberCount = membersCount ?: 0,
        members = members.toMembers(),
        bgType = background.toBackgroundType()
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
