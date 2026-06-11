package com.bonjur.profile.data.dataSource

import com.bonjur.network.model.PageNationResponse
import com.bonjur.profile.data.DTOs.CategorySectionResponse
import com.bonjur.profile.data.DTOs.LanguageResponse
import com.bonjur.profile.data.DTOs.MyEventResponse
import com.bonjur.profile.data.DTOs.MyHangoutResponse
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.data.DTOs.UserProfileResponse

interface ProfileDataSource {
    suspend fun getMyProfile(): UserProfileResponse
    suspend fun getUserById(userId: String): UserProfileResponse
    suspend fun updateProfile(request: ProfileUpdateRequest): UserProfileResponse
    suspend fun deleteAccount(): ByteArray
    suspend fun getCategories(): List<CategorySectionResponse>
    suspend fun getLanguages(): List<LanguageResponse>
    suspend fun getMyEvents(): PageNationResponse<List<MyEventResponse>>
    suspend fun getMyHangouts(userId: String): PageNationResponse<List<MyHangoutResponse>>
}