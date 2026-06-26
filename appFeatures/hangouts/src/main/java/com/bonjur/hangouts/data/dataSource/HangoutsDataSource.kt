package com.bonjur.hangouts.data.dataSource

import com.bonjur.hangouts.data.DTOs.HangoutCategorySectionResponse
import com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
import com.bonjur.hangouts.data.DTOs.HangoutDetailResponse
import com.bonjur.hangouts.data.DTOs.HangoutJoinRequest
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.hangouts.data.DTOs.HangoutMembersResponse

interface HangoutsDataSource {
    suspend fun getHangouts(query: Map<String, String>): List<HangoutListResponse>
    suspend fun getHangoutById(hangoutId: String): HangoutDetailResponse
    suspend fun getHangoutMembers(hangoutId: String, page: Int = 0, size: Int = 100): HangoutMembersResponse
    suspend fun getCategories(): List<HangoutCategorySectionResponse>
    suspend fun createHangout(request: HangoutCreateRequest): ByteArray
    suspend fun editHangout(hangoutId: String, request: HangoutCreateRequest): ByteArray
    suspend fun joinHangout(request: HangoutJoinRequest): ByteArray
    suspend fun exitHangout(hangoutId: String): ByteArray
}
