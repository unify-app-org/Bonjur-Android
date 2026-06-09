package com.bonjur.clubs.data.DTOs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubCreateRequest(
    val communityId: Int = 0,
    val name: String,
    val ownerContact: String = "",
    val about: String = "",
    val visibility: String = "PUBLIC",
    val location: String = "",
    @SerialName("backgroundColour") val backgroundColour: String = "PRIMARY",
    val capacity: Int? = null,
    val categoryIds: List<Int> = emptyList(),
    val links: List<ClubLinkRequest> = emptyList(),
    val rule: String? = null
)

@Serializable
data class ClubLinkRequest(
    val type: String,
    val name: String,
    val url: String
)

@Serializable
data class CategorySectionResponse(
    val type: String? = null,
    val title: String? = null,
    val subCategories: List<CategoryItemResponse> = emptyList()
)

@Serializable
data class CategoryItemResponse(
    val id: Int? = null,
    val title: String? = null
)

@Serializable
data class ClubListResponse(
    val id: Int? = null,
    val name: String? = null,
    val communityName: String? = null,
    @SerialName("background") val background: String? = null,
    val visibility: String? = null,
    val clubProfile: String? = null,
    val backgroundUrl: String? = null,
    val about: String? = null,
    val count: Int? = null,
    val capacity: Int? = null,
    val joined: Boolean? = null
)

@Serializable
data class ClubDetailResponse(
    val id: Int? = null,
    val communityId: Int? = null,
    val membersCount: Int? = null,
    val visibility: String? = null,
    val name: String = "",
    val ownerContact: String? = null,
    val location: String? = null,
    val about: String = "",
    val rule: String? = null,
    @SerialName("backgroundColour") val backgroundColour: String? = null,
    val backgroundUrl: String? = null,
    val logoUrl: String? = null,
    val modifiedAt: String? = null,
    val communityName: String = "",
    val clubUserRole: String? = null,
    val capacity: Int? = null,
    val categories: List<ClubCategoryResponse> = emptyList(),
    val links: List<ClubLinkResponse> = emptyList()
)

@Serializable
data class ClubCategoryResponse(
    val id: Int = 0,
    val title: String = ""
)

@Serializable
data class ClubLinkResponse(
    val type: String = "",
    val name: String = "",
    val url: String = ""
)

@Serializable
data class ClubMemberResponse(
    val content: List<ClubMember> = emptyList()
) {
    @Serializable
    data class ClubMember(
        val userId: String? = null,
        val role: String? = null,
        val fullName: String? = null,
        val profileUrl: String? = null,
        val degree: String? = null,
        val specialization: String? = null,
        val entryYear: Int? = null
    )
}
