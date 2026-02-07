package com.bonjur.clubs.domain.models

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.events.presentation.models.EventsCardMocks
import com.bonjur.events.presentation.models.EventsCardModel
import java.net.URL
import java.util.UUID

object ClubsDetails {

    data class UIModel(
        val name: String,
        val communityName: String,
        val membersCount: Int,
        val logo: String?,
        val coverImage: String?,
        val coverColorType: AppUIEntities.BackgroundType,
        val userActivityType: AppUIEntities.UserActivityRole,
        val accessType: AppUIEntities.AccessType,
        val tags: List<AppUIEntities.Tags>,
        val infoData: List<Info>,
        val eventsData: List<EventsCardModel>
    )

    data class Info(
        val id: String = UUID.randomUUID().toString(),
        val title: String,
        val subItems: List<SubInfo>
    )

    data class SubInfo(
        val id: String = UUID.randomUUID().toString(),
        val title: String?,
        val description: String,
        val isLink: Boolean = false
    )
}

val ClubsDetailsMockData = ClubsDetails.UIModel(
    name = "Football Club",
    communityName = "UFAZ community",
    membersCount = 12,
    logo = null,
    coverImage = null,
    coverColorType = AppUIEntities.BackgroundType.Secondary,
    userActivityType = AppUIEntities.UserActivityRole.NOT_JOINED,
    accessType = AppUIEntities.AccessType.PRIVATE,
    tags = listOf(
        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Messi"),
        AppUIEntities.Tags(id = 2, type = "SPORT", title = "Ronaldo"),
        AppUIEntities.Tags(id = 3, type = "SPORT", title = "Ronaldinho"),
        AppUIEntities.Tags(id = 4, type = "SPORT", title = "Basketball")
    ),
    infoData = listOf(
        ClubsDetails.Info(
            title = "About",
            subItems = listOf(
                ClubsDetails.SubInfo(
                    title = null,
                    description = "I want to have a coffee and then go to the film I have one free ticket to the concert for the Sunday evening if someone want just contact."
                )
            )
        ),
        ClubsDetails.Info(
            title = "Event info",
            subItems = listOf(
                ClubsDetails.SubInfo(
                    title = "Created/Updated Data",
                    description = "30 noyabr 2025"
                ),
                ClubsDetails.SubInfo(
                    title = "Owner contact",
                    description = "+994 123 45 67"
                ),
                ClubsDetails.SubInfo(
                    title = "Capacity",
                    description = "161/200 members"
                ),
                ClubsDetails.SubInfo(
                    title = "Rules",
                    description = "Everyone can come"
                ),
                ClubsDetails.SubInfo(
                    title = "Location",
                    description = "Cafetaria, 2nd floor"
                )
            )
        ),
        ClubsDetails.Info(
            title = "Link",
            subItems = listOf(
                ClubsDetails.SubInfo(
                    title = "Whatsapp Link",
                    description = "https://www.ufaz.az/en",
                    isLink = true
                ),
                ClubsDetails.SubInfo(
                    title = "Telegram link",
                    description = "https://www.ufaz.az/en",
                    isLink = true
                )
            )
        )
    ),
    eventsData = EventsCardMocks.previewMock
)
