package com.bonjur.auth.domain.useCase

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.domain.models.AuthInterestsModel
import com.bonjur.auth.domain.models.OnboardingUIModel
import com.bonjur.auth.domain.models.RegisterModel
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel

interface AuthUseCase {
    suspend fun register(
        request: RegisterRequest
    ): RegisterModel

    fun onboarding(): List<OnboardingUIModel>

    fun welcome(name: String): OnboardingUIModel

    suspend fun chooseUniversity(): List<SelectableListItemModel>

    suspend fun genders(): List<SelectableListItemModel>

    suspend fun languages(): List<SelectableListItemModel>

    suspend fun interests(): List<AuthInterestsModel>
}