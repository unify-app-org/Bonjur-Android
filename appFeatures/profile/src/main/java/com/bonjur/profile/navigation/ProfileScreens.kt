package com.bonjur.profile.navigation

import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.profile.presentation.models.UserCardModel
import kotlinx.serialization.Serializable

sealed class ProfileDetailRoute {
    @Serializable
    data class ClubsDetails(val id: Int) : ProfileDetailRoute()
    @Serializable
    data class EventsDetails(val id: String) : ProfileDetailRoute()
    @Serializable
    data class HangoutsDetails(val id: String) : ProfileDetailRoute()
    @Serializable
    object Settings : ProfileDetailRoute()
}