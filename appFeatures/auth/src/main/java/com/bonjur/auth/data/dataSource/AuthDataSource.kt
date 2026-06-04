package com.bonjur.auth.data.dataSource

import com.bonjur.auth.data.DTOs.CategoryResponse
import com.bonjur.auth.data.DTOs.CommunityResponse
import com.bonjur.auth.data.DTOs.LanguageResponse
import com.bonjur.auth.data.DTOs.LoginRequest
import com.bonjur.auth.data.DTOs.LoginResponse

interface AuthDataSource {
    suspend fun login(body: LoginRequest): LoginResponse
    suspend fun getCommunities(): List<CommunityResponse>
    suspend fun getLanguages(): List<LanguageResponse>
    suspend fun getCategories(): List<CategoryResponse>
}
