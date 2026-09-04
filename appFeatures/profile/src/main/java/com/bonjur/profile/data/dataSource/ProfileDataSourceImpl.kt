package com.bonjur.profile.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), ProfileDataSource {

    override suspend fun getMyProfile(): com.bonjur.profile.data.DTOs.UserProfileResponse =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetMyProfile)

    override suspend fun getUserById(userId: String, clubId: Int): com.bonjur.profile.data.DTOs.UserProfileResponse =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetUserById(userId, clubId))

    override suspend fun updateProfile(
        fields: Map<String, String>,
        imageFile: com.bonjur.network.APIClient.MultipartFile?
    ): ByteArray =
        fetchRawData(
            com.bonjur.profile.data.endPoint.ProfileEndpoints.UpdateProfile(fields, imageFile)
        )

    override suspend fun deleteAccount(): ByteArray =
        fetchRawData(com.bonjur.profile.data.endPoint.ProfileEndpoints.DeleteAccount)

    override suspend fun getCategories(): List<com.bonjur.profile.data.DTOs.CategorySectionResponse> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetCategories)

    override suspend fun getLanguages(): List<com.bonjur.profile.data.DTOs.LanguageResponse> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.GetLanguages)

    override suspend fun getMyClubs(userId: String, page: Int, size: Int): com.bonjur.network.model.PageNationResponse<List<com.bonjur.profile.data.DTOs.MyClubResponse>> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.MyClubs(userId, page, size))

    override suspend fun getMyEvents(page: Int, size: Int): com.bonjur.network.model.PageNationResponse<List<com.bonjur.profile.data.DTOs.MyEventResponse>> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.MyEvents(page, size))

    override suspend fun getMyHangouts(userId: String, page: Int, size: Int): com.bonjur.network.model.PageNationResponse<List<com.bonjur.profile.data.DTOs.MyHangoutResponse>> =
        fetch(com.bonjur.profile.data.endPoint.ProfileEndpoints.MyHangouts(userId, page, size))
}