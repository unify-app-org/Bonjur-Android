package com.bonjur.clubs.domain.useCase

import com.bonjur.clubs.presentation.models.ClubCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.yourapp.discover.viewmodel.FilterViewModel

interface ClubsUseCase {
    suspend fun fetchClubsData(): List<ClubCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>
}