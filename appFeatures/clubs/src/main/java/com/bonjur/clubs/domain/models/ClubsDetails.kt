package com.bonjur.clubs.domain.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.clubs.R
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.events.presentation.list.models.EventsCardMocks
import com.bonjur.events.presentation.list.models.EventsCardModel
import java.util.UUID

object ClubsDetails {

    data class UIModel(
        val name: String,
        val communityName: String,
        val membersCount: Int,
        /** Rendered beside the member count when present, mirroring iOS. */
        val eventsCount: Int? = null,
        val clubsCount: Int? = null,
        val logo: String?,
        val coverImage: String?,
        val coverColorType: AppUIEntities.BackgroundType,
        val userActivityType: AppUIEntities.UserActivityRole,
        val accessType: AppUIEntities.AccessType,
        val tags: List<AppUIEntities.Tags>,
        val infoData: List<Info>,
        val eventsData: List<EventsCardModel>,
        val editPrefillData: ClubEditPrefill,
        /**
         * Bottom join/request button. `null` hides it (already joined). Mirrors iOS
         * `ClubsDetailsModel.JoinButton` — the state comes from `clubUserStatus`, not
         * from the role alone, so a pending requester keeps a disabled "Request sent".
         */
        val joinButton: JoinButton? = null,
        /** nil/unverified → request-verify button (admins); verified → badge. Mirrors iOS. */
        val clubStatus: AppUIEntities.ClubStatus? = null
    )

    /** Form values + existing image URLs to pre-fill the edit screen. Mirrors iOS `ClubsCreate.PrefillData`. */
    data class ClubEditPrefill(
        val logoUrl: String?,
        val coverUrl: String?,
        val values: Map<AppFieldSchema.FieldId, AppFieldSchema.FieldValue>
    )

    /** Bottom join/request button state. Mirrors iOS `ClubsDetailsModel.JoinButton`. */
    data class JoinButton(
        val title: String,
        val disabled: Boolean
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
        val isLink: Boolean = false,
        /** When set, the row is tappable and offers call/copy. Mirrors iOS. */
        val phoneNumber: String? = null
    )
}

val ClubsDetailsMockData = ClubsDetails.UIModel(
    name = LanguageManager.string(R.string.clubs_name_ph),
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
            title = LanguageManager.string(R.string.clubs_about_label),
            subItems = listOf(
                ClubsDetails.SubInfo(
                    title = null,
                    description = "I want to have a coffee and then go to the film I have one free ticket to the concert for the Sunday evening if someone want just contact."
                )
            )
        ),
        ClubsDetails.Info(
            title = LanguageManager.string(R.string.clubs_info_section),
            subItems = listOf(
                ClubsDetails.SubInfo(
                    title = "Created/Updated Data",
                    description = "30 noyabr 2025"
                ),
                ClubsDetails.SubInfo(
                    title = LanguageManager.string(R.string.clubs_owner_contact_label),
                    description = "+994 123 45 67"
                ),
                ClubsDetails.SubInfo(
                    title = LanguageManager.string(R.string.clubs_capacity_label),
                    description = "161/200 members"
                ),
                ClubsDetails.SubInfo(
                    title = LanguageManager.string(R.string.clubs_rules_label),
                    description = "Everyone can come"
                ),
                ClubsDetails.SubInfo(
                    title = LanguageManager.string(R.string.clubs_location_label),
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
    eventsData = EventsCardMocks.previewMock,
    editPrefillData = ClubsDetails.ClubEditPrefill(logoUrl = null, coverUrl = null, values = emptyMap())
)
