package com.bonjur.hangouts.domain.useCase

import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import com.bonjur.hangouts.data.dataSource.HangoutsDataSource
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.list.model.HangoutsCardMocks
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import javax.inject.Inject

class HangoutsUseCaseImpl @Inject constructor(
    val dataSource: HangoutsDataSource
): HangoutsUseCase {
    override suspend fun fetchFilterData(): List<FilterView.Model> {
        return FilterViewMocks.mockData
    }

    override suspend fun fetchHangoutsData(): List<HangoutsCardModel> {
        return HangoutsCardMocks.previewMock
    }

    override suspend fun fetchDetailData(id: String): HangoutDetails.UIModel {
        return HangoutDetails.UIModel.mock
    }
}