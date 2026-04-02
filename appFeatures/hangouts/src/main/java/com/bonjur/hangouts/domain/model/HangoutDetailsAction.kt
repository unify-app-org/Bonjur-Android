package com.bonjur.hangouts.domain.model

import com.bonjur.designSystem.commonModel.AppUIEntities

object HangoutDetails {

    data class UIModel(
        val name: String = "",
        val communityName: String = "",
        val membersCount: Int = 0,
        val userActivityType: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.NOT_JOINED,
        val accessType: AppUIEntities.AccessType = AppUIEntities.AccessType.PUBLIC,
        val tags: List<AppUIEntities.Tags> = emptyList(),
        val infoData: List<Info> = emptyList(),
        val membersData: List<Any>? = null
    ) {
        companion object {
            val mock by lazy {
                UIModel(
                    name = "Basketball Event",
                    communityName = "UFAZ Community",
                    membersCount = 5,
                    userActivityType = AppUIEntities.UserActivityRole.NOT_JOINED,
                    accessType = AppUIEntities.AccessType.PRIVATE,
                    tags = listOf(
                        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Messi"),
                        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Ronaldo"),
                        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Ronaldinho"),
                        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Basketball")
                    ),
                    infoData = listOf(
                        Info(
                            title = "About",
                            subItems = listOf(
                                SubInfo(
                                    title = null,
                                    description = "I want to have a coffee and then go to the film I have one free ticket to the concert for the Sunday evening if someone want just contact."
                                )
                            )
                        ),
                        Info(
                            title = "Event info",
                            subItems = listOf(
                                SubInfo(title = "Created/Updated Data", description = "30 noyabr 2025"),
                                SubInfo(title = "Owner contact", description = "+994 123 45 67"),
                                SubInfo(title = "Capacity", description = "161/200 members"),
                                SubInfo(title = "Rules", description = "Everyone can come"),
                                SubInfo(title = "Location", description = "Cafetaria, 2nd floor")
                            )
                        ),
                        Info(
                            title = "Link",
                            subItems = listOf(
                                SubInfo(title = "Whatsapp Link", description = "https://www.ufaz.az/en", isLink = true),
                                SubInfo(title = "Telegram link", description = "https://www.ufaz.az/en", isLink = true)
                            )
                        )
                    ),
                    membersData = null
                )
            }
        }
    }

    data class Info(
        val id: String = java.util.UUID.randomUUID().toString(),
        val title: String,
        val subItems: List<SubInfo>
    )

    data class SubInfo(
        val id: String = java.util.UUID.randomUUID().toString(),
        val title: String?,
        val description: String,
        val isLink: Boolean = false
    )
}