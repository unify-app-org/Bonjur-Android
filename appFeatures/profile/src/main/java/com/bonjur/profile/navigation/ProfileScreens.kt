package com.bonjur.profile.navigation

import kotlinx.serialization.Serializable

sealed interface ProfileScreens {
    @Serializable
    data object ProfileDetail : ProfileScreens

    @Serializable
    data object StudentCard : ProfileScreens
    @Serializable
    data class ClubsDetails(val id: Int) : ProfileScreens
    @Serializable
    data class EventsDetails(val id: String) : ProfileScreens
    @Serializable
    data class HangoutsDetails(val id: String) : ProfileScreens
    @Serializable
    object Settings : ProfileScreens
}
