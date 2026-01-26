//
//  EventsCardModel.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.events

import com.bonjur.designSystem.commonModel.AppUIEntities
import java.util.UUID

data class EventsCardModel(
    val uuid: UUID = UUID.randomUUID(),
    val id: String,
    val name: String,
    val coverImageURL: String?,
    val memberCount: Int,
    val totalCapacity: Int?,
    val club: Club,
    val tags: List<AppUIEntities.Tags>,
    val bgType: AppUIEntities.BackgroundType,
    val requestType: AppUIEntities.RequestType,
    val accessType: AppUIEntities.AccessType
) {
    val buttonTitle: String
        get() = when (requestType) {
            AppUIEntities.RequestType.JOINED -> ""
            AppUIEntities.RequestType.REJECTED -> "Rejected"
            AppUIEntities.RequestType.PENDING -> "Request sent"
            AppUIEntities.RequestType.NONE -> when (accessType) {
                AppUIEntities.AccessType.PUBLIC -> "Join"
                AppUIEntities.AccessType.PRIVATE -> "Request"
            }
        }
    
    val memberCountText: String
        get() = if (totalCapacity != null) {
            "$memberCount/$totalCapacity members"
        } else {
            "$memberCount members"
        }
    
    data class Club(
        val name: String,
        val id: Int
    )
}

// Mock data
object EventsCardMocks {
    val previewMock = listOf(
        EventsCardModel(
            id = UUID.randomUUID().toString(),
            name = "Fan events",
            coverImageURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 21,
            totalCapacity = 40,
            club = EventsCardModel.Club(
                name = "Football club",
                id = 2
            ),
            tags = listOf(
                AppUIEntities.Tags(
                    id = 1,
                    type = "SPORT",
                    title = "Football"
                ),
                AppUIEntities.Tags(
                    id = 2,
                    type = "SPORT",
                    title = "Voleyball"
                ),
                AppUIEntities.Tags(
                    id = 3,
                    type = "SPORT",
                    title = "Basketball"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Primary,
            requestType = AppUIEntities.RequestType.NONE,
            accessType = AppUIEntities.AccessType.PUBLIC
        ),
        EventsCardModel(
            id = UUID.randomUUID().toString(),
            name = "Messi events",
            coverImageURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 15,
            totalCapacity = 34,
            club = EventsCardModel.Club(
                name = "Football club",
                id = 2
            ),
            tags = listOf(
                AppUIEntities.Tags(
                    id = 1,
                    type = "SPORT",
                    title = "Football"
                ),
                AppUIEntities.Tags(
                    id = 2,
                    type = "SPORT",
                    title = "Voleyball"
                ),
                AppUIEntities.Tags(
                    id = 3,
                    type = "SPORT",
                    title = "Basketball"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Secondary,
            requestType = AppUIEntities.RequestType.NONE,
            accessType = AppUIEntities.AccessType.PRIVATE
        ),
        EventsCardModel(
            id = UUID.randomUUID().toString(),
            name = "Chess events",
            coverImageURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 15,
            totalCapacity = 34,
            club = EventsCardModel.Club(
                name = "Chess club",
                id = 2
            ),
            tags = listOf(
                AppUIEntities.Tags(
                    id = 1,
                    type = "SPORT",
                    title = "Chess"
                ),
                AppUIEntities.Tags(
                    id = 2,
                    type = "SPORT",
                    title = "Voleyball"
                ),
                AppUIEntities.Tags(
                    id = 3,
                    type = "SPORT",
                    title = "Basketball"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Tertiary,
            requestType = AppUIEntities.RequestType.PENDING,
            accessType = AppUIEntities.AccessType.PUBLIC
        )
    )
}