package com.bonjur.hangouts.data.dataSource

import com.bonjur.hangouts.data.DTOs.HangoutCreateRequest
import com.bonjur.hangouts.data.DTOs.HangoutDetailResponse
import com.bonjur.hangouts.data.DTOs.HangoutListResponse
import com.bonjur.hangouts.data.DTOs.HangoutMemberResponse

interface HangoutsDataSource {
    suspend fun getHangouts(query: Map<String, String>): List<HangoutListResponse>
    suspend fun getHangoutById(hangoutId: String): HangoutDetailResponse
    suspend fun getHangoutMembers(hangoutId: String): List<HangoutMemberResponse>
    suspend fun createHangout(request: HangoutCreateRequest): HangoutDetailResponse
    suspend fun editHangout(hangoutId: String, request: HangoutCreateRequest): HangoutDetailResponse
}