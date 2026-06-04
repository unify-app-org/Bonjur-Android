package com.bonjur.clubs.domain.useCase

import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.components.filter.FilterView

interface ClubsUseCase {
    suspend fun fetchClubsData(): List<ClubCardModel>

    suspend fun fetchClubsDetails(clubId: Int): ClubsDetails.UIModel

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun createClub(
        name: String,
        about: String,
        location: String,
        ownerContact: String,
        capacity: Int?,
        rules: String,
        isPublic: Boolean
    )

    suspend fun editClub(
        clubId: Int,
        name: String,
        about: String,
        location: String,
        ownerContact: String,
        capacity: Int?,
        rules: String,
        isPublic: Boolean
    )

    suspend fun joinClub(clubId: Int)
}