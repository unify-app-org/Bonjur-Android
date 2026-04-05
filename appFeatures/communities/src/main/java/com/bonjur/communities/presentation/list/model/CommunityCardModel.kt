//
//  CommunityCardModel.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.communities.presentation.list.model

import com.bonjur.designSystem.commonModel.AppUIEntities
import java.util.UUID

data class CommunityCardModel(
    val uuid: UUID = UUID.randomUUID(),
    val id: Int,
    val name: String,
    val subTitle: String,
    val logoURL: String,
    val memberCount: Int,
    val type: AppUIEntities.ActivityType = AppUIEntities.ActivityType.COMMUNITY,
    val members: List<AppUIEntities.Member>,
    val bgType: AppUIEntities.BackgroundType
)

// Mock data
object CommunityCardMocks {
    val mock = listOf(
        CommunityCardModel(
            id = 1,
            name = "UFAZ",
            subTitle = "Community",
            logoURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 123,
            members = listOf(
                AppUIEntities.Member(
                    id = 1,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg"
                ),
                AppUIEntities.Member(
                    id = 2,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                ),
                AppUIEntities.Member(
                    id = 3,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Secondary
        ),
        CommunityCardModel(
            id = 2,
            name = "Bonjur",
            subTitle = "Community",
            logoURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 1675,
            members = listOf(
                AppUIEntities.Member(
                    id = 1,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg"
                ),
                AppUIEntities.Member(
                    id = 2,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                ),
                AppUIEntities.Member(
                    id = 3,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Primary
        )
    )
}