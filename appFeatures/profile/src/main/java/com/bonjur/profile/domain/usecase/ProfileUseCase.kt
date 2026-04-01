package com.bonjur.profile.domain.usecase

import com.bonjur.profile.presentation.models.ProfileDetail

interface ProfileUseCase {
    suspend fun fetchProfileData(): ProfileDetail.UIModel
}