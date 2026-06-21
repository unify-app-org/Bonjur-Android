package com.bonjur.profile.domain.usecase

import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.presentation.detail.models.ProfileDetail

interface ProfileUseCase {
    // Profile is always fetched by id (own id from token for self), mirroring iOS.
    // `userId == null` → the logged-in user.
    suspend fun fetchProfileData(userId: String?): ProfileDetail.UIModel

    // My clubs (`GET api/cs/v1/clubs/{userId}/myclubs`)
    suspend fun getMyClubs(userId: String?): List<ClubCardModel>

    // My events (`GET api/es/v1/events/my`) — logged-in user's events only
    suspend fun getMyEvents(): List<EventsCardModel>

    // My activities (`GET api/hs/v1/hangouts/{userId}/myhangouts`)
    suspend fun getMyHangouts(userId: String?): List<HangoutsCardModel>

    // Category / language option lists for the edit screen pickers.
    suspend fun getCategories(): List<CategorySection>
    suspend fun getLanguages(): List<SelectableListItemModel>

    // Update profile (PUT api/us/v1/users — fields as query, avatar as multipart).
    suspend fun editProfile(request: ProfileUpdateRequest, imageBytes: ByteArray?)

    // Used by ProfileSettingsScreen
    suspend fun deleteAccount()
}
