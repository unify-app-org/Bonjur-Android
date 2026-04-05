package com.bonjur.profile.domain.usecase

import com.bonjur.profile.presentation.detail.models.ProfileDetail

interface ProfileUseCase {
    suspend fun fetchProfileData(): ProfileDetail.UIModel
}