package com.bonjur.profile.domain.usecase

import android.net.Uri
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState

interface ProfileUseCase {
    // Existing — used by ProfileDetailScreen
    suspend fun fetchProfileData(): ProfileDetail.UIModel

    // Used by EditProfileScreen
    suspend fun fetchProfile(): EditProfileViewState?
    suspend fun editProfile(
        about: String,
        gender: String,
        birthDate: String,
        imageUri: Uri?
    )

    // Used by ProfileSettingsScreen
    suspend fun deleteAccount()
}