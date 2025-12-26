package com.bonjur.auth.domain.useCase

import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.domain.models.ChooseUniversityUIModel
import com.bonjur.auth.domain.models.OnboardingUIModel
import com.bonjur.auth.domain.models.RegisterModel

interface AuthUseCase {
    suspend fun register(
        request: RegisterRequest
    ): RegisterModel

    fun onboarding(): List<OnboardingUIModel>

    suspend fun chooseUniversity(): List<ChooseUniversityUIModel>

    fun welcome(name: String): OnboardingUIModel

}