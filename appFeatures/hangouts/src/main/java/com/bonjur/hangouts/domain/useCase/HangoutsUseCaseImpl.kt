package com.bonjur.hangouts.domain.useCase

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
import com.bonjur.hangouts.data.DTOs.HangoutDetailResponse
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.hangouts.data.dataSource.HangoutsDataSource
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import javax.inject.Inject

// Unused import removed

class HangoutsUseCaseImpl @Inject constructor(
    val dataSource: HangoutsDataSource
) : HangoutsUseCase {

    override suspend fun fetchFilterData(): List<FilterView.Model> = FilterViewMocks.mockData

    override suspend fun fetchHangoutsData(): List<HangoutsCardModel> {
        return dataSource.getHangouts(emptyMap()).map { it.toCardModel() }
    }

    override suspend fun fetchDetailData(id: String): HangoutDetails.UIModel {
        return dataSource.getHangoutById(id).toUIModel()
    }

    override suspend fun createHangout(
        name: String, about: String, location: String, ownerContact: String,
        capacity: Int?, rules: String, isPublic: Boolean, hangoutDate: String
    ) {
        dataSource.createHangout(
            HangoutCreateRequest(
                name = name, about = about, location = location,
                ownerContact = ownerContact, capacity = capacity ?: 0,
                rules = rules, visibility = if (isPublic) "PUBLIC" else "PRIVATE",
                hangoutDate = hangoutDate
            )
        )
    }

    override suspend fun editHangout(
        hangoutId: String, name: String, about: String, location: String,
        ownerContact: String, capacity: Int?, rules: String, isPublic: Boolean, hangoutDate: String
    ) {
        dataSource.editHangout(
            hangoutId = hangoutId,
            request = HangoutCreateRequest(
                name = name, about = about, location = location,
                ownerContact = ownerContact, capacity = capacity ?: 0,
                rules = rules, visibility = if (isPublic) "PUBLIC" else "PRIVATE",
                hangoutDate = hangoutDate
            )
        )
    }

    private fun HangoutListResponse.toCardModel() = HangoutsCardModel(
        id = id ?: "",
        name = name ?: "",
        description = about ?: "",
        memberCount = membersCount ?: 0,
        totalCapacity = capacity,
        tags = categoryResponses.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        requestType = when (requestStatus?.uppercase()) {
            "ACCEPTED", "JOINED" -> AppUIEntities.RequestType.JOINED
            "PENDING" -> AppUIEntities.RequestType.PENDING
            "REJECTED" -> AppUIEntities.RequestType.REJECTED
            "JOINED" -> AppUIEntities.RequestType.JOINED
            else -> AppUIEntities.RequestType.NONE
        }
    )

    private fun HangoutDetailResponse.toUIModel() = HangoutDetails.UIModel(
        name = name,
        communityName = community?.name ?: "",
        membersCount = membersCount ?: 0,
        userActivityType = when (role?.uppercase()) {
            "MEMBER" -> AppUIEntities.UserActivityRole.MEMBER
            "PRESIDENT" -> AppUIEntities.UserActivityRole.PRESIDENT
            else -> AppUIEntities.UserActivityRole.NOT_JOINED
        },
        accessType = if (visibility == "PUBLIC") AppUIEntities.AccessType.PUBLIC else AppUIEntities.AccessType.PRIVATE,
        tags = categories.map { AppUIEntities.Tags(id = it.id, type = "CATEGORY", title = it.title) },
        infoData = buildList {
            if (!about.isNullOrBlank()) add(HangoutDetails.Info(
                title = "About",
                subItems = listOf(HangoutDetails.SubInfo(title = null, description = about))
            ))
            add(HangoutDetails.Info(
                title = "Event info",
                subItems = buildList {
                    ownerContact?.let { add(HangoutDetails.SubInfo(title = "Owner contact", description = it)) }
                    capacity?.let { add(HangoutDetails.SubInfo(title = "Capacity", description = "${membersCount ?: 0}/$it members")) }
                    rules?.let { add(HangoutDetails.SubInfo(title = "Rules", description = it)) }
                    location?.let { add(HangoutDetails.SubInfo(title = "Location", description = it)) }
                    hangoutDate?.let { add(HangoutDetails.SubInfo(title = "Date", description = it)) }
                }
            ))
            if (links.isNotEmpty()) add(HangoutDetails.Info(
                title = "Links",
                subItems = links.map { HangoutDetails.SubInfo(title = it.name, description = it.url, isLink = true) }
            ))
        }
    )
}
