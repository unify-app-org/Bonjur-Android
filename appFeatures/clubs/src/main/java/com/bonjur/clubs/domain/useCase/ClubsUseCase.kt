package com.bonjur.clubs.domain.useCase

import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MembersPage

/** All values gathered by the club create/edit form. Mirrors iOS `buildRequest()` inputs. */
data class ClubFormData(
    val name: String,
    val about: String,
    val location: String,
    val ownerContact: String,
    val capacity: Int?,
    val rules: String,
    val isPublic: Boolean,
    val background: AppUIEntities.BackgroundType,
    val categoryIds: List<Int>,
    val links: List<AppFieldSchema.LinkItem>,
    val logo: ByteArray?,
    val cover: ByteArray?
)

interface ClubsUseCase {
    suspend fun fetchClubsData(
        size: Int,
        keyword: String?,
        categoryIds: List<Int>
    ): List<ClubCardModel>

    suspend fun fetchClubsDetails(clubId: Int): ClubsDetails.UIModel

    suspend fun fetchFilterData(): List<FilterView.Model>

    suspend fun getCategories(): List<CategorySection>

    suspend fun createClub(form: ClubFormData)

    suspend fun editClub(clubId: Int, form: ClubFormData)

    suspend fun joinClub(clubId: Int)

    suspend fun exitClub(clubId: Int)

    suspend fun assignRole(clubId: Int, userId: String, role: AppUIEntities.UserActivityRole)

    /** Members preview (page 0) for the detail Members tab. */
    suspend fun fetchClubMembers(clubId: Int): GroupedMembersData

    /** One page of members for the shared see-all screen. */
    suspend fun fetchClubMembersPage(clubId: Int, page: Int, size: Int, keyword: String?): MembersPage

    /** Exit gate for presidents: an owner can only leave once a VP exists. */
    suspend fun clubHasVicePresident(clubId: Int): Boolean
}