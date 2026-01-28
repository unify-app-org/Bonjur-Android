package com.bonjur.clubs.domain.useCase

import com.bonjur.clubs.data.dataSource.ClubsDataSource
import com.bonjur.clubs.presentation.models.ClubCardMocks
import com.bonjur.clubs.presentation.models.ClubCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.yourapp.discover.viewmodel.FilterViewModel
import javax.inject.Inject

class ClubsUseCaseImpl @Inject constructor(
    val dataSource: ClubsDataSource
): ClubsUseCase {
    override suspend fun fetchClubsData(): List<ClubCardModel> {
        return ClubCardMocks.previewData
    }

    override suspend fun fetchFilterData(): List<FilterView.Model> {
        return FilterViewMocks.mockData
    }
}