package com.bonjur.hangouts.domain.useCase

import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.hangouts.presentation.model.HangoutsCardModel

interface HangoutsUseCase {
    suspend fun fetchHangoutsData(): List<HangoutsCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>
}