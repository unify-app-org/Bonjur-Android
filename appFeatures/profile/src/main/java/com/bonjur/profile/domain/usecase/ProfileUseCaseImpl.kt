package com.bonjur.profile.domain.usecase

import android.net.Uri
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.data.dataSource.ProfileDataSource
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.detail.models.mock
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    val dataSource: ProfileDataSource,
    val tokenManager: TokenManager
) : ProfileUseCase {

    // ── Existing ──────────────────────────────────────────────────────────────

    override suspend fun fetchProfileData(): ProfileDetail.UIModel {
        return try {
            dataSource.getMyProfile().toUIModel()
        } catch (e: Exception) {
            ProfileDetail.UIModel.mock()
        }
    }

    override suspend fun fetchProfileData(userId: String): ProfileDetail.UIModel {
        return try {
            dataSource.getUserById(userId).toUIModel()
        } catch (e: Exception) {
            ProfileDetail.UIModel.mock()
        }
    }

    /** Maps a user profile response to the detail UI model. Shared by own + by-id fetch. */
    private fun com.bonjur.profile.data.DTOs.UserProfileResponse.toUIModel(): ProfileDetail.UIModel =
        ProfileDetail.UIModel(
            userCardModel = com.bonjur.profile.presentation.detail.models.UserCardModel(
                nameSurname = fullName ?: "-",
                speciality = specialization ?: "-",
                community = communityName ?: "-",
                entryYear = entryYear?.toString() ?: "-",
                course = "-",
                imageUrl = profileUrl
            ),
            about = about,
            gender = gender,
            birthday = birthDate,
            tags = categories.map {
                com.bonjur.designSystem.commonModel.AppUIEntities.Tags(
                    id = it.id,
                    type = "",
                    title = it.title
                )
            },
            cardCover = null
        )

    // ── EditProfile ───────────────────────────────────────────────────────────

    override suspend fun fetchProfile(): EditProfileViewState? {
        return try {
            val dto = dataSource.getMyProfile()
            EditProfileViewState(
                name = dto.fullName ?: "-",
                faculty = dto.specialization ?: "-",
                community = dto.communityName ?: "-",
                entry = dto.entryYear?.toString() ?: "-",
                course = "-",
                about = dto.about ?: "",
                birthDateText = dto.birthDate ?: "",
                avatarUrl = dto.profileUrl
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun editProfile(
        about: String,
        gender: String,
        birthDate: String,
        imageUri: Uri?
    ) {
        dataSource.updateProfile(
            ProfileUpdateRequest(
                about = about.ifBlank { null },
                gender = gender.ifBlank { null },
                birthDate = birthDate.ifBlank { null }
            )
        )
        // Note: image upload requires multipart support — add when ApiClient supports it
    }

    // ── Settings ──────────────────────────────────────────────────────────────

    override suspend fun deleteAccount() {
        dataSource.deleteAccount()
    }

    // ── My events / my activities (mirror iOS ProfileRepo) ───────────────────

    override suspend fun getMyEvents(): List<EventsCardModel> =
        dataSource.getMyEvents().content.map { item ->
            EventsCardModel(
                id = item.id ?: "-",
                name = item.name ?: "-",
                coverImageURL = item.background,
                memberCount = item.membersCount ?: 0,
                totalCapacity = item.capacity,
                club = EventsCardModel.Club(name = "-", id = 0),
                tags = item.categoryResponses.map {
                    AppUIEntities.Tags(id = it.id, type = "", title = it.title)
                },
                bgType = AppUIEntities.BackgroundType.Primary,
                requestType = item.requestStatus.toRequestType(),
                accessType = item.visibility.toAccessType()
            )
        }

    override suspend fun getMyHangouts(): List<HangoutsCardModel> {
        val userId = tokenManager.getUserId() ?: return emptyList()
        return dataSource.getMyHangouts(userId).content.map { item ->
            HangoutsCardModel(
                id = item.id ?: "-",
                name = item.name ?: "-",
                description = item.about ?: "-",
                memberCount = item.membersCount ?: 0,
                totalCapacity = item.capacity,
                tags = item.categories.map {
                    AppUIEntities.Tags(id = it.id, type = "", title = it.title)
                },
                accessType = item.visibility.toAccessType(),
                requestType = item.status.toRequestType()
            )
        }
    }

    private fun String?.toAccessType(): AppUIEntities.AccessType =
        if (this?.uppercase() == "PUBLIC") AppUIEntities.AccessType.PUBLIC
        else AppUIEntities.AccessType.PRIVATE

    private fun String?.toRequestType(): AppUIEntities.RequestType = when (this?.uppercase()) {
        "JOINED", "ACCEPTED" -> AppUIEntities.RequestType.JOINED
        "PENDING" -> AppUIEntities.RequestType.PENDING
        "REJECTED" -> AppUIEntities.RequestType.REJECTED
        else -> AppUIEntities.RequestType.NONE
    }
}