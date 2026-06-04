package com.bonjur.profile.domain.usecase

import android.net.Uri
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.data.dataSource.ProfileDataSource
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.detail.models.mock
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    val dataSource: ProfileDataSource
) : ProfileUseCase {

    // ── Existing ──────────────────────────────────────────────────────────────

    override suspend fun fetchProfileData(): ProfileDetail.UIModel {
        return try {
            val dto = dataSource.getMyProfile()
            ProfileDetail.UIModel(
                userCardModel = com.bonjur.profile.presentation.detail.models.UserCardModel(
                    nameSurname = dto.fullName ?: "-",
                    speciality = dto.specialization ?: "-",
                    community = dto.communityName ?: "-",
                    entryYear = dto.entryYear?.toString() ?: "-",
                    course = "-",
                    imageUrl = dto.profileUrl
                ),
                about = dto.about,
                gender = dto.gender,
                birthday = dto.birthDate,
                tags = dto.categories.map {
                    com.bonjur.designSystem.commonModel.AppUIEntities.Tags(
                        id = it.id,
                        type = "",
                        title = it.title
                    )
                },
                cardCover = null
            )
        } catch (e: Exception) {
            ProfileDetail.UIModel.mock()
        }
    }

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
}