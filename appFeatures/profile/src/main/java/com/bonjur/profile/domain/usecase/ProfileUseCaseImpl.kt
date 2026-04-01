package com.bonjur.profile.domain.usecase

import com.bonjur.profile.data.dataSource.ProfileDataSource
import com.bonjur.profile.presentation.models.ProfileDetail
import com.bonjur.profile.presentation.models.mock
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    val dataSource: ProfileDataSource
): ProfileUseCase {
    override suspend fun fetchProfileData(): ProfileDetail.UIModel {
        return ProfileDetail.UIModel.mock()
    }
}