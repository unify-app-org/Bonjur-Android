package com.bonjur.communities.domain.model

import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.designSystem.commonModel.AppUIEntities
import java.util.UUID

object CommunityDetails {

    data class UIModel(
        val name: String = "",
        val membersCount: Int = 0,
        val logo: String? = null,
        val coverImage: String? = null,
        val coverColorType: AppUIEntities.BackgroundType = AppUIEntities.BackgroundType.Primary,
        val userActivity: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.NOT_JOINED,
        val tags: List<AppUIEntities.Tags> = emptyList(),
        val infoData: List<Info> = emptyList(),
        val editPrefillData: ClubsDetails.ClubEditPrefill? = null
    ) {
        companion object {
            val mock by lazy {
                UIModel(
                    name = "UFAZ Community",
                    membersCount = 123,
                    logo = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
                    coverColorType = AppUIEntities.BackgroundType.Secondary,
                    tags = listOf(
                        AppUIEntities.Tags(id = 1, type = "EDUCATION", title = "University"),
                        AppUIEntities.Tags(id = 2, type = "EDUCATION", title = "Students"),
                        AppUIEntities.Tags(id = 3, type = "SOCIAL", title = "Networking")
                    ),
                    infoData = listOf(
                        Info(
                            title = "About",
                            subItems = listOf(
                                SubInfo(
                                    title = null,
                                    description = "A community for all UFAZ students to connect, share resources, and organize activities together."
                                )
                            )
                        ),
                        Info(
                            title = "Community info",
                            subItems = listOf(
                                SubInfo(title = "Created", description = "15 January 2026"),
                                SubInfo(title = "Contact", description = "+994 123 45 67"),
                                SubInfo(title = "Members", description = "123/500 members"),
                                SubInfo(title = "Rules", description = "Be respectful and supportive")
                            )
                        ),
                        Info(
                            title = "Links",
                            subItems = listOf(
                                SubInfo(title = "Website", description = "https://www.ufaz.az/en", isLink = true),
                                SubInfo(title = "Telegram", description = "https://t.me/ufaz", isLink = true)
                            )
                        )
                    )
                )
            }
        }
    }

    data class Info(
        val id: String = UUID.randomUUID().toString(),
        val title: String,
        val subItems: List<SubInfo>
    )

    data class SubInfo(
        val id: String = UUID.randomUUID().toString(),
        val title: String?,
        val description: String,
        val isLink: Boolean = false,
        val phoneNumber: String? = null
    )
}
