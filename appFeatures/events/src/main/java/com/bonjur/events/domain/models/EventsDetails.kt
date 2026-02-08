package com.bonjur.events.domain.models

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.attachments.AttachmentItemModel
import java.util.UUID

object EventsDetails {

    data class UIModel(
        val name: String,
        val communityName: String,
        val membersCount: Int,
        val coverImage: String?,
        val coverColorType: AppUIEntities.BackgroundType,
        val userActivityType: AppUIEntities.UserActivityRole,
        val accessType: AppUIEntities.AccessType,
        val tags: List<AppUIEntities.Tags>,
        val infoData: List<Info>,
        val attachments: List<AttachmentItemModel>
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

val EventsDetailsMockData = EventsDetails.UIModel(
    name = "Basketball Event",
    communityName = "Sports Club",
    membersCount = 12,
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
        EventsDetails.Info(
            title = "About",
            subItems = listOf(
                EventsDetails.SubInfo(
                    title = null,
                    description = "I want to have a coffee and then go to the film I have one free ticket to the concert for the Sunday evening if someone want just contact."
                )
            )
        ),
        EventsDetails.Info(
            title = "Event info",
            subItems = listOf(
                EventsDetails.SubInfo(
                    title = "Created/Updated Data",
                    description = "30 noyabr 2025"
                ),
                EventsDetails.SubInfo(
                    title = "Owner contact",
                    description = "+994 123 45 67"
                ),
                EventsDetails.SubInfo(
                    title = "Capacity",
                    description = "161/200 members"
                ),
                EventsDetails.SubInfo(
                    title = "Rules",
                    description = "Everyone can come"
                ),
                EventsDetails.SubInfo(
                    title = "Location",
                    description = "Cafetaria, 2nd floor"
                )
            )
        ),
        EventsDetails.Info(
            title = "Link",
            subItems = listOf(
                EventsDetails.SubInfo(
                    title = "Whatsapp Link",
                    description = "https://www.ufaz.az/en",
                    isLink = true
                ),
                EventsDetails.SubInfo(
                    title = "Telegram link",
                    description = "https://www.ufaz.az/en",
                    isLink = true
                )
            )
        )
    ),
    attachments = listOf(
        AttachmentItemModel(
            id = 1,
            name = "Career_fair_2025.pdf",
            size = "16 kb",
            type = AttachmentItemModel.AttachmentType.PDF
        ),
        AttachmentItemModel(
            id = 2,
            name = "Career_fair_2025.image",
            size = "14 mb",
            type = AttachmentItemModel.AttachmentType.PDF
        )
    )
)