//
//  ClubCardModel.kt
//  AppFoundation
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.clubs.presentation.models

import com.bonjur.designSystem.commonModel.AppUIEntities
import java.util.UUID

data class ClubCardModel(
    val uuid: UUID = UUID.randomUUID(),
    val id: Int,
    val name: String,
    val communityName: String,
    val logoURL: String,
    val memberCount: Int,
    val totalCapacity: Int,
    val community: String,
    val type: AppUIEntities.ActivityType = AppUIEntities.ActivityType.CLUBS,
    val members: List<AppUIEntities.Member>,
    val bgType: AppUIEntities.BackgroundType,
    val accessType: AppUIEntities.AccessType,
    val requestType: AppUIEntities.RequestType
)

// Preview/Mock Data
object ClubCardMocks {
    val previewData = listOf(
        ClubCardModel(
            id = 1,
            name = "Football club",
            communityName = "Azerbaijany French university",
            logoURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 190,
            totalCapacity = 200,
            community = "UFAZ",
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
            bgType = AppUIEntities.BackgroundType.CustomColor(
                AppUIEntities.ColorType.Orange
            ),
            accessType = AppUIEntities.AccessType.PRIVATE,
            requestType = AppUIEntities.RequestType.NONE
        ),
        ClubCardModel(
            id = 2,
            name = "Dance club",
            communityName = "Azerbaijany French university",
            logoURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 56,
            totalCapacity = 120,
            community = "UFAZ",
            members = listOf(
                AppUIEntities.Member(id = 1, profileImage = null),
                AppUIEntities.Member(
                    id = 2,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                ),
                AppUIEntities.Member(
                    id = 3,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Primary,
            accessType = AppUIEntities.AccessType.PUBLIC,
            requestType = AppUIEntities.RequestType.PENDING
        ),
        ClubCardModel(
            id = 3,
            name = "Boys club",
            communityName = "Azerbaijany French university",
            logoURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 56,
            totalCapacity = 120,
            community = "UFAZ",
            members = listOf(
                AppUIEntities.Member(id = 1, profileImage = null),
                AppUIEntities.Member(
                    id = 2,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                ),
                AppUIEntities.Member(
                    id = 3,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                )
            ),
            bgType = AppUIEntities.BackgroundType.Secondary,
            accessType = AppUIEntities.AccessType.PRIVATE,
            requestType = AppUIEntities.RequestType.NONE
        ),
        ClubCardModel(
            id = 4,
            name = "Girls club",
            communityName = "Azerbaijany French university",
            logoURL = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
            memberCount = 56,
            totalCapacity = 120,
            community = "UFAZ",
            members = listOf(
                AppUIEntities.Member(id = 1, profileImage = null),
                AppUIEntities.Member(
                    id = 2,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                ),
                AppUIEntities.Member(
                    id = 3,
                    profileImage = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png"
                )
            ),
            bgType = AppUIEntities.BackgroundType.CustomColor(
                AppUIEntities.ColorType.Red
            ),
            accessType = AppUIEntities.AccessType.PRIVATE,
            requestType = AppUIEntities.RequestType.NONE
        )
    )
}