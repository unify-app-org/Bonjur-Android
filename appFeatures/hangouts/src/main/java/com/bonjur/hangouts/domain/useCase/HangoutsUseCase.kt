package com.bonjur.hangouts.domain.useCase

import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel

interface HangoutsUseCase {
    suspend fun fetchHangoutsData(): List<HangoutsCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchDetailData(id: String): HangoutDetails.UIModel
}