package com.bonjur.hangouts.domain.useCase

import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MembersPage

/** Create/edit payload. Mirrors iOS `HangoutsDTOModel.Request` inputs. */
data class HangoutFormData(
    val name: String,
    val about: String,
    val location: String,
    val ownerContact: String,
    val capacity: Int?,
    val rules: String,
    val isPublic: Boolean,
    val hangoutDate: String,
    val categoryIds: List<Int>,
    val links: List<AppFieldSchema.LinkItem>
)

interface HangoutsUseCase {
    suspend fun fetchHangoutsData(
        categoryIds: List<Int>,
        keyword: String?,
        page: Int,
        size: Int
    ): List<HangoutsCardModel>

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun fetchDetailData(id: String): HangoutDetails.UIModel

    suspend fun getCategories(): List<CategorySection>

    suspend fun createHangout(form: HangoutFormData)

    suspend fun editHangout(hangoutId: String, form: HangoutFormData)

    suspend fun joinHangout(hangoutId: String)

    suspend fun exitHangout(hangoutId: String)

    suspend fun fetchHangoutMembers(hangoutId: String): GroupedMembersData

    suspend fun fetchHangoutMembersPage(hangoutId: String, page: Int, size: Int): MembersPage
}
