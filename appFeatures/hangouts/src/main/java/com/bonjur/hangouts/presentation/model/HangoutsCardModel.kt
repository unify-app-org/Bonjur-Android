//
//  HangoutsCardModel.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.hangouts.presentation.model

import com.bonjur.designSystem.commonModel.AppUIEntities
import java.util.UUID

data class HangoutsCardModel(
    val uuid: UUID = UUID.randomUUID(),
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val totalCapacity: Int?,
    val tags: List<AppUIEntities.Tags>,
    val accessType: AppUIEntities.AccessType,
    val requestType: AppUIEntities.RequestType
) {
    val memberCountText: String
        get() = if (totalCapacity != null) {
            "$memberCount/$totalCapacity members"
        } else {
            "$memberCount members"
        }
    
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
}

// Mock data
object HangoutsCardMocks {
    val previewMock = listOf(
        HangoutsCardModel(
            id = UUID.randomUUID().toString(),
            name = "Study night at cafe",
            description = "I want to have a coffee and then go to evening if someone want just",
            memberCount = 27,
            totalCapacity = 35,
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
            accessType = AppUIEntities.AccessType.PUBLIC,
            requestType = AppUIEntities.RequestType.NONE
        ),
        HangoutsCardModel(
            id = UUID.randomUUID().toString(),
            name = "Exam preparation",
            description = "I want to have a coffee and then go to evening if someone want just",
            memberCount = 27,
            totalCapacity = 35,
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
            accessType = AppUIEntities.AccessType.PUBLIC,
            requestType = AppUIEntities.RequestType.NONE
        ),
        HangoutsCardModel(
            id = UUID.randomUUID().toString(),
            name = "To find new peoples",
            description = "I want to have a coffee and then go to evening if someone want just",
            memberCount = 27,
            totalCapacity = 35,
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
            accessType = AppUIEntities.AccessType.PUBLIC,
            requestType = AppUIEntities.RequestType.NONE
        )
    )
}