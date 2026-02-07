package com.bonjur.clubs.domain.useCase

import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.components.filter.FilterView

interface ClubsUseCase {
    suspend fun fetchClubsData(): List<ClubCardModel>

    suspend fun fetchClubsDetails(clubId: Int): ClubsDetails.UIModel

    suspend fun fetchFilterData(): List<FilterView.Model>
}