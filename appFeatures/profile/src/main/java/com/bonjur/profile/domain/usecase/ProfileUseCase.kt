package com.bonjur.profile.domain.usecase

import android.net.Uri
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState

interface ProfileUseCase {
    // Existing — used by ProfileDetailScreen (logged-in user)
    suspend fun fetchProfileData(): ProfileDetail.UIModel

    // Another user's profile by id. Mirrors iOS profile-by-id fetch.
    suspend fun fetchProfileData(userId: String): ProfileDetail.UIModel

    // My events (`GET api/es/v1/events/my`) — logged-in user's events
    suspend fun getMyEvents(): List<EventsCardModel>

    // My activities (`GET api/hs/v1/hangouts/{userId}/myhangouts`)
    suspend fun getMyHangouts(): List<HangoutsCardModel>

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