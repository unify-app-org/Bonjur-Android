package com.bonjur.profile.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), ProfileDataSource {

    override suspend fun getMyProfile(): com.bonjur.profile.data.DTOs.UserProfileResponse =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetMyProfile)

    override suspend fun getUserById(userId: String): com.bonjur.profile.data.DTOs.UserProfileResponse =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetUserById(userId))

    override suspend fun updateProfile(request: com.bonjur.profile.data.DTOs.ProfileUpdateRequest): com.bonjur.profile.data.DTOs.UserProfileResponse =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.UpdateProfile(request))

    override suspend fun deleteAccount(): ByteArray =
        fetchRawData(com.bonjur.profile.data.endPoint.ProfileEndpoints.DeleteAccount)

    override suspend fun getCategories(): List<com.bonjur.profile.data.DTOs.CategorySectionResponse> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetCategories)

    override suspend fun getLanguages(): List<com.bonjur.profile.data.DTOs.LanguageResponse> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetLanguages)

    override suspend fun getMyEvents(): com.bonjur.network.model.PageNationResponse<List<com.bonjur.profile.data.DTOs.MyEventResponse>> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.MyEvents)

    override suspend fun getMyHangouts(userId: String): com.bonjur.network.model.PageNationResponse<List<com.bonjur.profile.data.DTOs.MyHangoutResponse>> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.MyHangouts(userId))
}